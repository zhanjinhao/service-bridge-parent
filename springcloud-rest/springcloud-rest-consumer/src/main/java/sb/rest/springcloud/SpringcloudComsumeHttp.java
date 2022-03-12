package sb.rest.springcloud;

import sb.client.pojo.URLPojo;
import sb.client.proxy.SBHttpClientProxyFactory;

public class SpringcloudComsumeHttp {

    public static void main(String[] args) {

        OrderDetailControllerApi orderDetailControllerApi = null;

        try {
            orderDetailControllerApi = SBHttpClientProxyFactory.createHttpClient(OrderDetailControllerApi.class,
                    new URLPojo("http", "localhost", "8761", "ps-localhost-springcloud", "1.0"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10000; i++) {

            System.out.println(orderDetailControllerApi.getOrderDetail(i + "order"));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
