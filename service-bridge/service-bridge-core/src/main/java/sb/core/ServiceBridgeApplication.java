package sb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sb.core.init.config.MicroserviceSystemConfig;
import sb.core.server.operation.OperationServer;
import sb.core.buffer.ServiceDriver;

public class ServiceBridgeApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceBridgeApplication.class);

    public static void main(String[] args) throws Exception {

        checkJavaVersion();

        ServiceDriver.loadServiceDrivers();

        MicroserviceSystemConfig.loadConfig();

//        Class.forName("sb.core.init.RecoverFromDisk");

        OperationServer.start();

    }

    private static void checkJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        if (!javaVersion.startsWith("1.8")) {
            LOGGER.error("please make sure you are using jdk8! ");
            System.exit(-1);
        }
    }

}