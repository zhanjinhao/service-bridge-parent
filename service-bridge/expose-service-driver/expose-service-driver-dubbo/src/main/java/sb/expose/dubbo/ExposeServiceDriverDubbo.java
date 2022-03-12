package sb.expose.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import sb.expose.ConsumerSystemConfig;
import sb.expose.ExposeServiceDriver;

import java.util.HashMap;
import java.util.Map;

public class ExposeServiceDriverDubbo implements ExposeServiceDriver {

    private Map<ConsumerSystemConfig, ApplicationConfig> applicationConfigMap = new HashMap<>();
    private Map<ConsumerSystemConfig, RegistryConfig> registryConfigMap = new HashMap<>();
    private Map<ConsumerSystemConfig, ProtocolConfig> protocolConfigMap = new HashMap<>();

    @Override
    public Object buildSkeleton(Class interfaceClazz, Object stub, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {

        if (!validateServiceClazz(interfaceClazz, stub)) {
            return false;
        }
        ApplicationConfig application;
        RegistryConfig registry;
        ProtocolConfig protocol;

        if (applicationConfigMap.get(consumerSystemConfig) != null) {
            application = applicationConfigMap.get(consumerSystemConfig);
            registry = registryConfigMap.get(consumerSystemConfig);
            protocol = protocolConfigMap.get(consumerSystemConfig);
        } else {
            // 当前应用配置
            application = new ApplicationConfig();
            application.setName(serviceName);

            // 连接注册中心配置
            registry = new RegistryConfig();
            registry.setProtocol(consumerSystemConfig.getRegistryType());
            registry.setAddress(consumerSystemConfig.getRegistryIp() + ":" + consumerSystemConfig.getRegistryPort());

            // 服务提供者协议配置
            protocol = new ProtocolConfig();
            protocol.setName(consumerSystemConfig.getMsgProtocol());
            protocol.setPort(Integer.valueOf(consumerSystemConfig.getServerPort()));
            protocol.setThreads(100);
            applicationConfigMap.put(consumerSystemConfig, application);
            registryConfigMap.put(consumerSystemConfig, registry);
            protocolConfigMap.put(consumerSystemConfig, protocol);
        }

        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        // 服务提供者暴露服务配置
        ServiceConfig serviceConfig = new ServiceConfig<>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        serviceConfig.setApplication(application);
        serviceConfig.setRegistry(registry);        // 多个注册中心可以用setRegistries()
        serviceConfig.setProtocol(protocol);        // 多个协议可以用setProtocols()
        serviceConfig.setInterface(interfaceClazz);
        serviceConfig.setRef(stub);
        serviceConfig.setVersion(version);
        // 暴露及注册服务
        serviceConfig.export();

        return true;
    }

    @Override
    public Boolean removeSkeleton(Class interfaceClazz, Object skeletonObj, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {

        ServiceConfig serviceConfig = (ServiceConfig)skeletonObj;
        if (serviceConfig == null)
            return false;
        serviceConfig.unexport();
        System.out.println("removeSkeleton -- driver  ..............");
        return true;
    }

    @Override
    public String getExposeServiceDriverId() {
        return "expose-service-driver-dubbo-zookeeper";
    }

    @Override
    public Boolean checkRegistryAlive(ConsumerSystemConfig consumerSystemConfig) {
        return null;
    }

    @Override
    public String toString() {
        return getExposeServiceDriverId();
    }
}