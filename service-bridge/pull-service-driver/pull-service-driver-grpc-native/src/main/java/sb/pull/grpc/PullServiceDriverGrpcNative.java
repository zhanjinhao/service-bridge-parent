package sb.pull.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sb.pull.ProvidedServiceKey;
import sb.pull.ProviderSystemConfig;
import sb.pull.PullServiceDriver;
import sb.rpc.grpc.ZookeeperUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullServiceDriverGrpcNative implements PullServiceDriver {

    // namespace zookeeperUtil
    private Map<ZookeeperUtilKey, ZookeeperUtil> zookeeperUtilMap = new HashMap<>();

    @Override
    public Object buildStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {

        String namespace = providerSystemConfig.getOthers();
        String connectUrl = providerSystemConfig.getRegistryIp() + ":" + providerSystemConfig.getRegistryPort();
        System.out.println(connectUrl);
        ZookeeperUtil zookeeperUtil = zookeeperUtilMap.get(new ZookeeperUtilKey(namespace, connectUrl));

        if (zookeeperUtil == null) {
            zookeeperUtil = new ZookeeperUtil(namespace, connectUrl);
        } else {
            zookeeperUtil = zookeeperUtilMap.get(new ZookeeperUtilKey(namespace, connectUrl));
        }

        String ip = null;
        int port = 0;

        try {
            String get = zookeeperUtil.get(serviceName, version);
            String[] split = get.split("&");
            ip = split[0].split("=")[1];
            port = Integer.parseInt(split[1].split("=")[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().usePlaintext().build();

        try {
            Method newBlockingStub = interfaceClazz.getMethod("newBlockingStub", Channel.class);
            return newBlockingStub.invoke(null, managedChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object removeStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {
        return null;
    }

    @Override
    public String getPullServiceDriverId() {
        return "pull-service-driver-grpc-native";
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

        String namespace = providerSystemConfig.getOthers();
        String connectUrl = providerSystemConfig.getRegistryIp() + ":" + providerSystemConfig.getRegistryPort();
        System.out.println(connectUrl);
        ZookeeperUtil zookeeperUtil = zookeeperUtilMap.get(new ZookeeperUtilKey(namespace, connectUrl));

        if (zookeeperUtil == null) {
            zookeeperUtil = new ZookeeperUtil(namespace, connectUrl);
        } else {
            zookeeperUtil = zookeeperUtilMap.get(new ZookeeperUtilKey(namespace, connectUrl));
        }

        List<ProvidedServiceKey> providedServiceKeys = new ArrayList<>();

        try {
            String serviceKey = zookeeperUtil.getServiceKey();

            if(serviceKey == null)
                return providedServiceKeys;
            String[] split = serviceKey.split("&");

            int length = split.length;

            for(int i = 0; i < length; i++){
                String[] split1 = split[i].split("`");
                ProvidedServiceKey providedServiceKey = new ProvidedServiceKey(split1[0], split1[1]);
                providedServiceKeys.add(providedServiceKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return providedServiceKeys;
    }
}
