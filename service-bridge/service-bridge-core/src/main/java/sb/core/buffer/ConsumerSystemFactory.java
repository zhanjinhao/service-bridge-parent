package sb.core.buffer;

import sb.expose.ConsumerSystemConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerSystemFactory {

    private ConsumerSystemFactory() {

    }

    // exposeRegistryId, ExposeRegistry
    private static Map<String, ConsumerSystem> consumerSystemMap = new ConcurrentHashMap<>(256, 0.1f);

    public static Map<String, ConsumerSystem> getConsumerSystemMap() {
        return consumerSystemMap;
    }

    /**
     * 创建一个 ConsumerSystem 并缓存至 registriesMap
     *
     * @param scsid
     * @param consumerSystemConfig
     * @return
     */
    public static ConsumerSystem buildConsumerSystem(String scsid, ConsumerSystemConfig consumerSystemConfig, String exposeServiceDriverId) {

        ConsumerSystem consumerSystem = new ConsumerSystem(scsid, consumerSystemConfig, ServiceDriver.getExposeServiceDriver(exposeServiceDriverId));

        consumerSystemMap.put(scsid, consumerSystem);

        return consumerSystem;

    }

    public static ConsumerSystem getConsumerSystem(String scsid) {
        return consumerSystemMap.get(scsid);
    }


}