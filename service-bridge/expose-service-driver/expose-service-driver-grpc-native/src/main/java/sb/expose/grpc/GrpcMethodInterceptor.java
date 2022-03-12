package sb.expose.grpc;

import io.grpc.stub.StreamObserver;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class GrpcMethodInterceptor implements MethodInterceptor {
    private Object grpcStub;

    public GrpcMethodInterceptor(Object grpcStub) {
        this.grpcStub = grpcStub;
    }

    private static Class<StreamObserver> streamObserverClass = StreamObserver.class;
    private static Method onCompleted;
    private static Method onNext;

    static {
        try {
            onCompleted = streamObserverClass.getMethod("onCompleted", null);
            onNext = streamObserverClass.getMethod("onNext", Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Class<?> aClass = grpcStub.getClass();

        Class<?>[] parameterTypes = method.getParameterTypes();

        int serverParameterLength = parameterTypes.length;
        int clientParameterLength = serverParameterLength - 1;

        Class[] classes = new Class[clientParameterLength];

        for(int i = 0; i < clientParameterLength; i++){
            classes[i] = parameterTypes[i];
        }

        Object[] parameterData = new Object[clientParameterLength];
        for(int i = 0; i < clientParameterLength; i++){
            parameterData[i] = objects[i];
        }

        Method method1 = aClass.getMethod(method.getName(), classes);

        Object invoke = method1.invoke(grpcStub, parameterData);

        onNext.invoke(objects[clientParameterLength], invoke);
        onCompleted.invoke(objects[clientParameterLength], null);

        return invoke;
    }
}