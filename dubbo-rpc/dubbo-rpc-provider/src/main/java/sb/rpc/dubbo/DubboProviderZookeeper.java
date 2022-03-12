package sb.rpc.dubbo;


import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;

public class DubboProviderZookeeper {
    public static void main(String[] args) {
        // 服务实现
        DubboInterface testService = new DubboImpl();

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("dubbo-interface");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("59.110.143.226:2181");

        // 服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(13002);
        protocol.setThreads(100);

        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        // 服务提供者暴露服务配置
        ServiceConfig<DubboInterface> serviceConfig = new ServiceConfig<>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        serviceConfig.setApplication(application);
        serviceConfig.setRegistry(registry);  // 多个注册中心可以用setRegistries()
        serviceConfig.setProtocol(protocol);  // 多个协议可以用setProtocols()
        serviceConfig.setInterface(DubboInterface.class);
        serviceConfig.setRef(testService);
        serviceConfig.setVersion("1.0");

        // 暴露及注册服务
        serviceConfig.export();

        System.out.println(serviceConfig.isExported());

        ServiceConfig<DubboInterface2> serviceConfig2 = new ServiceConfig<>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        serviceConfig2.setApplication(application);
        serviceConfig2.setRegistry(registry);  // 多个注册中心可以用setRegistries()
        serviceConfig2.setProtocol(protocol);  // 多个协议可以用setProtocols()
        serviceConfig2.setInterface(DubboInterface2.class);
        serviceConfig2.setRef(new DubboImpl2());
        serviceConfig2.setVersion("1.0");
        // 暴露及注册服务
        serviceConfig2.export();

        System.out.println(serviceConfig2.isExported());

        while (true) {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}