package sb.rest.springcloud;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClientConfig;

public class MyDiscoveryClient extends DiscoveryClient {
    public MyDiscoveryClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, AbstractDiscoveryClientOptionalArgs args) {
        super(applicationInfoManager, config, args);
    }


    public static void main(String[] args) {


    }

}
