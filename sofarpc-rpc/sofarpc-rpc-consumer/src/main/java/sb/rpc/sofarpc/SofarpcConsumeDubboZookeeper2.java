package sb.rpc.sofarpc;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import sb.rpc.dubbo.DubboInterface;
import sb.rpc.dubbo.DubboInterface2;

public class SofarpcConsumeDubboZookeeper2 {

    public static void main(String[] args) {
        // 指定注册中心
        RegistryConfig registryConfig = new RegistryConfig()
                .setProtocol("zookeeper")
                .setAddress("59.110.143.226:2181");

        // 引用一个服务
        ConsumerConfig<DubboInterface2> consumerConfig = new ConsumerConfig<DubboInterface2>()
                .setInterfaceId(DubboInterface2.class.getName())
                .setProtocol("bolt")
                .setRegistry(registryConfig)
                .setVersion("1.0");

        // 拿到代理类
        DubboInterface2 service = consumerConfig.refer();

        for (int i = 0; i < 10000; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.sayHello();
        }

    }
}