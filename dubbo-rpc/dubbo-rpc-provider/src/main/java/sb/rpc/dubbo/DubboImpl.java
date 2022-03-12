package sb.rpc.dubbo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DubboImpl implements DubboInterface {

    static Map<String, OrderDetail> orderDetailMap;

    static Map<String, Boolean> orderResellMap;

    static {
        orderDetailMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            orderDetailMap.put(i + "order",
                    new OrderDetail("HuaWei mobile phone", new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000 * 10),
                            i + 1000.0, "lucy-" + i, "Dubbo"));
        }
    }

    @Override
    public OrderDetail getOrderDetail(String id) {
        return orderDetailMap.get(id);
    }

    public void setOrderIsResell(String id, Boolean isResell) {
        orderResellMap.put(id, isResell);
    }

}