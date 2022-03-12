package sb.rest.springcloud;

import sb.common.annotation.EurekaServiceConfig;
import sb.common.annotation.ServiceMethodConfig;

@EurekaServiceConfig(serviceName = "order-service", requestPrefix = "")
public interface OrderDetailControllerApi {
    @ServiceMethodConfig(methodMapping = "getOrderDetail")
    OrderDetail getOrderDetail(String id);
}
