package sb.rpc.sofarpc;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import sb.rest.springcloud.OrderDetailControllerApi;

public class SofarpcConsumeSpringCloudEureka {

    public static void main(String[] args) {
        // 指定注册中心
        RegistryConfig registryConfig = new RegistryConfig()
                .setProtocol("zookeeper")
                .setAddress("59.110.143.226:2181");

        // 引用一个服务
        ConsumerConfig<OrderDetailControllerApi> consumerConfig = new ConsumerConfig<OrderDetailControllerApi>()
                .setInterfaceId(OrderDetailControllerApi.class.getName())
                .setProtocol("bolt")
                .setRegistry(registryConfig)
                .setVersion("1.0");

        OrderDetailControllerApi service = consumerConfig.refer();

        for (int i = 0; i < 10000; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(service.getOrderDetail(i + "order"));
        }


    }
}