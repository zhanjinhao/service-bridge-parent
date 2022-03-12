package sb.pull.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
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

public class PullServiceDriverDubbo implements PullServiceDriver {

    private Map<ProviderSystemConfig, CuratorFramework> clientMap = new HashMap<>();
    private Map<ProviderSystemConfig, ApplicationConfig> applicationConfigMap = new HashMap<>();
    private Map<ProviderSystemConfig, RegistryConfig> registryConfigMap = new HashMap<>();

    @Override
    public Object buildStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {

        ApplicationConfig application;
        RegistryConfig registry;

        if (applicationConfigMap.get(providerSystemConfig) == null) {
            // 当前应用配置
            application = new ApplicationConfig();
            application.setName(serviceName);

            // 连接注册中心配置
            registry = new RegistryConfig();
            registry.setProtocol(providerSystemConfig.getRegistryType());
            registry.setAddress(providerSystemConfig.getRegistryIp() + ":" + providerSystemConfig.getRegistryPort());
            applicationConfigMap.put(providerSystemConfig, application);
            registryConfigMap.put(providerSystemConfig, registry);
        } else {
            application = applicationConfigMap.get(providerSystemConfig);
            registry = registryConfigMap.get(providerSystemConfig);
        }

        // 引用远程服务
        ReferenceConfig reference = new ReferenceConfig();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(interfaceClazz);
        reference.setVersion(version);
        reference.setLoadbalance(providerSystemConfig.getLoadBalance());
        return reference.get();
    }

    @Override
    public Object removeStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {
        return null;
    }

    @Override
    public String getPullServiceDriverId() {
        return "pull-service-driver-dubbo-zookeeper";
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
            List<String> childrenLevel1 = client.getChildren().forPath("/dubbo");
            for (String str : childrenLevel1) {
                List<String> childrenLevel2 = client.getChildren().forPath("/dubbo/" + str + "/providers");
                for (String s : childrenLevel2) {
                    String decode = decode(s);
                    int i = decode.lastIndexOf("&");
                    String versionEntry = decode.substring(i + 1);
                    int i1 = versionEntry.indexOf("=");
                    String version = versionEntry.substring(i1 + 1);
                    serviceKeys.add(new ProvidedServiceKey(str, version));
                }
            }
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
