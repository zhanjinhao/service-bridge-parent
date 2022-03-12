package sb.rpc.sofarpc;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;

public class SofarpcProviderZookeeper {
    public static void main(String[] args) {
        // 指定注册中心
        RegistryConfig registryConfig = new RegistryConfig()
                .setProtocol("zookeeper")
                .setAddress("59.110.143.226:2181");

        // 指定服务端协议和地址
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt")
                .setPort(13001)
                .setDaemon(false);

        // 发布一个服务
        ProviderConfig<SofarpcInterface> providerConfig = new ProviderConfig<SofarpcInterface>()
                .setInterfaceId(SofarpcInterface.class.getName())
                .setRef(new SofarpcImpl())
                .setRegistry(registryConfig)
                .setServer(serverConfig)
                .setVersion("1.0");
        providerConfig.export();


//        // 指定注册中心
//        RegistryConfig registryConfig2 = new RegistryConfig()
//                .setProtocol("zookeeper")
//                .setAddress("59.110.143.226:2181");
//
//        // 指定服务端协议和地址
//        ServerConfig serverConfig2 = new ServerConfig()
//                .setProtocol("bolt")
//                .setPort(13001)
//                .setDaemon(false);
        // 发布一个服务
        ProviderConfig<SofarpcInterface2> providerConfig2 = new ProviderConfig<SofarpcInterface2>()
                .setInterfaceId(SofarpcInterface2.class.getName())
                .setRef(new SofarpcImpl2())
                .setRegistry(registryConfig)
                .setServer(serverConfig)
                .setVersion("1.0");
        providerConfig2.export();

    }
}