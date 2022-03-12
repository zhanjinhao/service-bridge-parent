package sb.pull.sofarpc;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import sb.pull.ProvidedServiceKey;
import sb.pull.ProviderSystemConfig;
import sb.pull.PullServiceDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullServiceDriverSofarpc implements PullServiceDriver {

    private Map<ProviderSystemConfig, CuratorFramework> clientMap = new HashMap<>();

    @Override
    public Object buildStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {
        // 指定注册中心
        RegistryConfig registryConfig = new RegistryConfig()
                .setProtocol(providerSystemConfig.getRegistryType())
                .setAddress(providerSystemConfig.getRegistryIp() + ":" + providerSystemConfig.getRegistryPort());

        // 引用一个服务
        ConsumerConfig consumerConfig = new ConsumerConfig<>()
                .setInterfaceId(interfaceClazz.getName())
                .setProtocol("bolt")
                .setRegistry(registryConfig).setVersion(version);

        // 拿到代理类
        return consumerConfig.refer();
    }

    @Override
    public Object removeStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {
        return null;
    }

    @Override
    public String getPullServiceDriverId() {
        return "pull-service-driver-sofarpc-zookeeper";
    }


    @Override
    public Boolean checkServiceAlive(Class interfaceClazz, String serviceName, String version) {
        return null;
    }

    @Override
    public Void updateService() {
        return null;
    }

    @Override
    public Boolean checkRegistryAlive(ProviderSystemConfig providerSystemConfig) {
        return null;
    }

    @Override
    public List<ProvidedServiceKey> getAllServiceKey(ProviderSystemConfig providerSystemConfig) {

        CuratorFramework client;

        if (clientMap.get(providerSystemConfig) == null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            System.out.println(providerSystemConfig.getRegistryIp() + ":" + providerSystemConfig.getRegistryPort());
            client = CuratorFrameworkFactory.builder()
                    .connectString(providerSystemConfig.getRegistryIp() + ":" + providerSystemConfig.getRegistryPort())
                    .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                    .build();
            client.start();
            clientMap.put(providerSystemConfig, client);
        } else {
            client = clientMap.get(providerSystemConfig);
        }

        List<ProvidedServiceKey> serviceKeys = new ArrayList<>();

        try {
            List<String> childrenLevel1 = client.getChildren().forPath("/sofa-rpc");
            for (String str : childrenLevel1) {
                List<String> childrenLevel2 = client.getChildren().forPath("/sofa-rpc/" + str + "/providers");
                for (String s : childrenLevel2) {
                    String decode = decode(s);

                    String[] split = decode.split("&");
                    String version = split[1].split("=")[1];

                    serviceKeys.add(new ProvidedServiceKey(str, version));
                }
            }
            System.out.println(serviceKeys);
            return serviceKeys;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return getPullServiceDriverId();
    }

}
