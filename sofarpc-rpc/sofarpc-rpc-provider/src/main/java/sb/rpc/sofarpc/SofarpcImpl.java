package sb.rpc.sofarpc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SofarpcImpl implements SofarpcInterface {

    private static Map<String, ResellOrderDetail> resellOrderDetailMap;

    static {
        resellOrderDetailMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            resellOrderDetailMap.put(i + "order", new ResellOrderDetail(i + 500.0, new Date(System.currentTimeMillis()), "李四" + i));
        }
    }


    public String getHello() {
        return "hello";
    }

    public String getHelloByName(String name) {
        return "hello " + name + " !";
    }

    public String getHelloByNames(String... names) {
        int length = names.length;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(names[i] + " ");
        }
        return "hello " + stringBuilder.toString() + "!";
    }

    @Override
    public ResellOrderDetail getResellOrderDetail(String id) {
        return resellOrderDetailMap.get(id);
    }


}