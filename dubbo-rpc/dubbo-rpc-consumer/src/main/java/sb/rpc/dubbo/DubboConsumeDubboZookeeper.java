package sb.rpc.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

public class DubboConsumeDubboZookeeper {

    public static void main(String[] args) throws ClassNotFoundException {

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("dubbo-interface----2");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("59.110.143.226:2181");

        // 引用远程服务
        ReferenceConfig<DubboInterface> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(DubboInterface.class);
        reference.setVersion("1.0");
        reference.setLoadbalance("roundrobin");
        DubboInterface service = reference.get();

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