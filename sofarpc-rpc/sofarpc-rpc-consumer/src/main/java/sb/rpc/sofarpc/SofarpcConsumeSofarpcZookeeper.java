package sb.rpc.sofarpc;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;

public class SofarpcConsumeSofarpcZookeeper {

    public static void main(String[] args) {
        // 指定注册中心
        RegistryConfig registryConfig = new RegistryConfig()
                .setProtocol("zookeeper")
                .setAddress("59.110.143.226:2181");

        // 引用一个服务
        ConsumerConfig<SofarpcInterface> consumerConfig = new ConsumerConfig<SofarpcInterface>()
                .setInterfaceId(SofarpcInterface.class.getName())
                .setProtocol("bolt")
                .setRegistry(registryConfig)
                .setVersion("1.0");

        SofarpcInterface service = consumerConfig.refer();

        System.out.println(service.getResellOrderDetail("3order"));

    }
}