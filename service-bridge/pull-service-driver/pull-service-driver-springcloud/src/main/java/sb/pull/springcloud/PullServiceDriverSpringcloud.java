package sb.pull.springcloud;

import sb.pull.ProvidedServiceKey;
import sb.pull.ProviderSystemConfig;
import sb.pull.PullServiceDriver;

import java.util.ArrayList;
import java.util.List;

public class PullServiceDriverSpringcloud implements PullServiceDriver {

    private List<ProvidedServiceKey> providedServiceKeyList = new ArrayList<>();

    @Override
    public Object buildStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {

        String appsUrl = providerSystemConfig.getRegistryIp() + ":" + providerSystemConfig.getRegistryPort();

        StubClientProxy stubClientProxy = new StubClientProxy("http://" + appsUrl + "/eureka/apps");

        try {
            providedServiceKeyList.add(new ProvidedServiceKey(serviceName, version));
            return stubClientProxy.createHttpClient(interfaceClazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object removeStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) {

        providedServiceKeyList.remove(new ProvidedServiceKey(serviceName, version));

        return true;
    }


    @Override
    public String getPullServiceDriverId() {
        return "pull-service-driver-springcloud-eureka";
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
        return providedServiceKeyList;
    }
}
