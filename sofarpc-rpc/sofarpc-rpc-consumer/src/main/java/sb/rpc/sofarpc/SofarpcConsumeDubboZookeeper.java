package sb.rpc.sofarpc;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import sb.rpc.dubbo.DubboInterface;

public class SofarpcConsumeDubboZookeeper {

    public static void main(String[] args) {
        // 指定注册中心
        RegistryConfig registryConfig = new RegistryConfig()
                .setProtocol("zookeeper")
                .setAddress("59.110.143.226:2181");

        // 引用一个服务
        ConsumerConfig<DubboInterface> consumerConfig = new ConsumerConfig<DubboInterface>()
                .setInterfaceId(DubboInterface.class.getName())
                .setProtocol("bolt")
                .setRegistry(registryConfig)
                .setVersion("1.0");

        // 拿到代理类
        DubboInterface service = consumerConfig.refer();

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