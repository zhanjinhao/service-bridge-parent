package sb.core.buffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sb.expose.ExposeServiceDriver;
import sb.pull.PullServiceDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 使用SPI机制加载服务驱动
 */
public class ServiceDriver {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceDriver.class);

    private static Map<String, PullServiceDriver> pullServiceDriverMap = new HashMap<>();
    private static Map<String, ExposeServiceDriver> exposeServiceDriverMap = new HashMap<>();

    public static void loadServiceDrivers() {
        loadPSDs();
        loadESDs();
    }

    private static void loadPSDs() {
        ServiceLoader<PullServiceDriver> pullServiceDrivers = ServiceLoader.load(PullServiceDriver.class);

        for (PullServiceDriver service : pullServiceDrivers) {
            pullServiceDriverMap.put(service.getPullServiceDriverId(), service);
        }

        if (pullServiceDriverMap == null || pullServiceDriverMap.size() == 0) {
            LOGGER.error("there must be ay least one pull-service tool");
            System.exit(-1);
        }
        LOGGER.info(pullServiceDriverMap.toString());
    }

    private static void loadESDs() {
        // 加载 expose-service 的实例
        ServiceLoader<ExposeServiceDriver> exposeServices = ServiceLoader.load(ExposeServiceDriver.class);

        for (ExposeServiceDriver service : exposeServices) {
            exposeServiceDriverMap.put(service.getExposeServiceDriverId(), service);
        }

        if (exposeServiceDriverMap == null || exposeServiceDriverMap.size() == 0) {
            LOGGER.error("there must be ay least one pull-service tool");
            System.exit(-1);
        }
        LOGGER.info(exposeServiceDriverMap.toString());
    }


    public static PullServiceDriver getPullServiceDriver(String pullServiceDriverId) {
        return pullServiceDriverMap.get(pullServiceDriverId);
    }

    public static ExposeServiceDriver getExposeServiceDriver(String exposeServiceDriverId) {
        return exposeServiceDriverMap.get(exposeServiceDriverId);
    }

}