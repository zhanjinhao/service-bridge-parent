package sb.expose.grpc;

import net.sf.cglib.proxy.Enhancer;

public class DynamicProxyFactory {
    public static Object dynamicProxy(Class clazz, Object service){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new GrpcMethodInterceptor(service));
        return enhancer.create();
    }
}