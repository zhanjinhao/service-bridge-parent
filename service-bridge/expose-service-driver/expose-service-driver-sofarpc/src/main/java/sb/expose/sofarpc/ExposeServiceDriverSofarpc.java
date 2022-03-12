package sb.expose.sofarpc;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import sb.expose.ConsumerSystemConfig;
import sb.expose.ExposeServiceDriver;

import java.util.HashMap;
import java.util.Map;

public class ExposeServiceDriverSofarpc implements ExposeServiceDriver {

    private static final Map<ConsumerSystemConfig, RegistryConfig> registryConfigMap = new HashMap<>();

    private static final Map<ConsumerSystemConfig, ServerConfig> serverConfigMap = new HashMap<>();

    @Override
    public Object buildSkeleton(Class interfaceClazz, Object stub, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {

        if (!validateServiceClazz(interfaceClazz, stub)) {
            return false;
        }

        try {
            RegistryConfig registryConfig;
            ServerConfig serverConfig;
            if (registryConfigMap.get(consumerSystemConfig) != null) {
                System.out.println("---------------------");
                registryConfig = registryConfigMap.get(consumerSystemConfig);
                serverConfig = serverConfigMap.get(consumerSystemConfig);
            } else {
                // 指定注册中心
                registryConfig = new RegistryConfig()
                        .setProtocol(consumerSystemConfig.getRegistryType())
                        .setAddress(consumerSystemConfig.getRegistryIp() + ":" + consumerSystemConfig.getRegistryPort());

                // 指定服务端协议和地址
                serverConfig = new ServerConfig()
                        .setProtocol(consumerSystemConfig.getMsgProtocol())
                        .setPort(Integer.parseInt(consumerSystemConfig.getServerPort()))
                        .setDaemon(false);

                registryConfigMap.put(consumerSystemConfig, registryConfig);
                serverConfigMap.put(consumerSystemConfig, serverConfig);
            }

            // 发布一个服务
            ProviderConfig providerConfig = new ProviderConfig<>()
                    .setInterfaceId(interfaceClazz.getName())
                    .setRef(stub)
                    .setRegistry(registryConfig)
                    .setServer(serverConfig)
                    .setVersion(version);
            providerConfig.export();

            System.out.println(".........");
            return providerConfig;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean removeSkeleton(Class interfaceClazz, Object skeletonObj, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {
        ProviderConfig providerConfig = (ProviderConfig)skeletonObj;
        if (providerConfig == null)
            return false;
        providerConfig.unExport();
        return true;
    }

    @Override
    public String getExposeServiceDriverId() {
        return "expose-service-driver-sofarpc-zookeeper";
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
