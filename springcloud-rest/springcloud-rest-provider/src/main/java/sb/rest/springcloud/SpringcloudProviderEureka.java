package sb.rest.springcloud;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class SpringcloudProviderEureka {
    static Map<String, OrderDetail> orderDetailMap;

    static {
        orderDetailMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            orderDetailMap.put(i + "order",
                    new OrderDetail("HuaWei mobile phone", new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000 * 10),
                            i + 1000.0, "lucy" + i, "Springcloud"));
        }
    }

    @RequestMapping("/getOrderDetail/{id}")
    public OrderDetail getOrderDetail(@PathVariable("id") String id) {
        return orderDetailMap.get(id);
    }
}
