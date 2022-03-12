package sb.rest.springcloud;

import sb.client.pojo.URLPojo;
import sb.client.proxy.SBHttpClientProxyFactory;
import sb.rpc.dubbo.DubboInterface;

public class SpringcloudComsumeDubbo {

    public static void main(String[] args) {

        DubboInterface dubboInterface = null;

        try {
            dubboInterface = SBHttpClientProxyFactory.createHttpClient(DubboInterface.class,
                    new URLPojo("http", "localhost", "8761", "ps-aliyun-isjinhao-dubbo", "1.0"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10000; i++) {

            System.out.println(dubboInterface.getOrderDetail(i + "order"));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
