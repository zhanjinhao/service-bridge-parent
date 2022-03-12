package sb.core.operation.read;

import sb.core.server.Result;
import sb.core.buffer.ProviderSystem;
import sb.core.buffer.ProviderSystemFactory;
import sb.core.operation.read.io.CurrentThreadLoaderObjectInputStream;

import javax.servlet.http.HttpServletRequest;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

public class JavaSerializationInvokeService implements InvokeServiceInterface {

    public Object invoke(String spsid, String serviceName, String version, String fullMethodName, HttpServletRequest request) throws Exception {

        try {

            ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
            if (providerSystem == null)
                return new Result(402, "ps 不存在");

            Object stubObj = providerSystem.getStub(serviceName, version);
            if(stubObj == null)
                return new Result(411, "stub is null");

            URLClassLoader loader = providerSystem.getUrlClassLoader(serviceName, version);

            // 设置线程上下文类加载器
            Thread.currentThread().setContextClassLoader(loader);

            Method method = providerSystem.getMethod(serviceName, version, fullMethodName.replaceAll("-", " "));

            return method.invoke(stubObj, handleRequest(request));

        } catch (Exception e) {
            e.printStackTrace();
            return Result.getException();
        }
    }

    private static Class<?> listClazz;
    private static Class<?> iteratorClass;
    private static Method iteratorMethod;
    private static Method sizeMethod;
    private static Method hasNext;
    private static Method next;

    static {
        try {
            listClazz = Class.forName("java.util.List");
            iteratorClass = Class.forName("java.util.Iterator");
            iteratorMethod = listClazz.getMethod("iterator");
            sizeMethod = listClazz.getMethod("size");
            hasNext = iteratorClass.getMethod("hasNext");
            next = iteratorClass.getMethod("next");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Object[] handleRequest(HttpServletRequest request) throws Exception {
        ObjectInputStream ois = new CurrentThreadLoaderObjectInputStream(request.getInputStream());
        Object requestData = ois.readObject();
        Integer size = (Integer) sizeMethod.invoke(requestData);
        Object iteratorObj = iteratorMethod.invoke(requestData);

        Object[] objects = new Object[size];

        int i = 0;
        while (Boolean.valueOf(hasNext.invoke(iteratorObj).toString())) {
            objects[i++] = next.invoke(iteratorObj);
        }

        return objects;
    }
}
