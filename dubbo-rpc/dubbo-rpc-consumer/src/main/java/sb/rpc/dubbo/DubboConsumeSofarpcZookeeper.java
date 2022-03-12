package sb.rpc.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import sb.rpc.sofarpc.SofarpcInterface;

import java.util.concurrent.TimeUnit;

public class DubboConsumeSofarpcZookeeper {

    public static void main(String[] args) throws ClassNotFoundException {

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("dubbo-interface-2");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("59.110.143.226:2181");

        // 引用远程服务
        ReferenceConfig<SofarpcInterface> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(SofarpcInterface.class);
        reference.setVersion("1.0");
        reference.setLoadbalance("roundrobin");
        SofarpcInterface Object = reference.get();

        for (int i = 0; i < 10000; i++) {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
////
////            System.out.println(contextClassLoader);
////
////            Class<?> aClass = contextClassLoader.loadClass("sb.rpc.dubbo.OrderDetail");
////            System.out.println(aClass);
//
//            Class<?> aClass1 = contextClassLoader.loadClass("sb.rpc.dubbo.OrderDetail");
//            System.out.println(aClass1);
//
//            Thread.currentThread().setContextClassLoader(contextClassLoader);

            System.out.println(Object.getResellOrderDetail(i + "order"));


        }

    }
}