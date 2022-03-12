package sb.core.init.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sb.core.buffer.ConsumerSystemFactory;
import sb.core.buffer.ProviderSystemFactory;
import sb.expose.ConsumerSystemConfig;
import sb.pull.ProviderSystemConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MicroserviceSystemConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(MicroserviceSystemConfig.class);

    public static void loadConfig() {

        File[] consumerSystemConfigs = new File(ApplicationConfig.CONFIG_PATH).listFiles(pathname -> {
            String name = pathname.getName();
            if (name.endsWith(".properties") && name.startsWith("cs"))
                return true;
            return false;
        });

        File[] providerSystemConfigs = new File(ApplicationConfig.CONFIG_PATH).listFiles(pathname -> {
            String name = pathname.getName();
            if (name.endsWith(".properties") && name.startsWith("ps"))
                return true;
            return false;
        });

        /**
         * registry-ip=
         * registry-port=
         * registry-type=
         * msg-protocol=
         * load-balance=
         * server-port=
         * others=
         */

        try {
            for (File file : consumerSystemConfigs) {
                loadConfig(file.getName());
            }
            for (File file : providerSystemConfigs) {
                loadConfig(file.getName());
            }

            LOGGER.info(ProviderSystemFactory.getProviderSystemMap().toString());
            LOGGER.info(ConsumerSystemFactory.getConsumerSystemMap().toString());

        } catch (Exception e) {
            LOGGER.error("load config error: " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static Boolean loadConfig(String fileName) throws Exception {

        File file = new File(ApplicationConfig.CONFIG_PATH + File.separator + fileName);
        if (!file.exists() || !fileName.endsWith(".properties"))
            return false;

        String systemId = fileName.replace(".properties", "");

        InputStream inputStream = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(inputStream);

        System.out.println(fileName);

        if (fileName.startsWith("cs-")) {
            // 加载文件的内容到内存
            ConsumerSystemConfig consumerSystemConfig = new ConsumerSystemConfig();
            consumerSystemConfig.setRegistryIp(properties.getProperty("registry-ip"));
            consumerSystemConfig.setRegistryPort(properties.getProperty("registry-port"));
            consumerSystemConfig.setRegistryType(properties.getProperty("registry-type"));
            consumerSystemConfig.setMsgProtocol(properties.getProperty("msg-protocol"));
            consumerSystemConfig.setLoadBalance(properties.getProperty("load-balance"));
            consumerSystemConfig.setServerPort(properties.getProperty("server-port"));
            consumerSystemConfig.setExposeServiceDriverId(properties.getProperty("expose-service-driver-id"));
            consumerSystemConfig.setScsid(properties.getProperty("scsid"));
            consumerSystemConfig.setOthers(properties.getProperty("others"));

            ConsumerSystemFactory.buildConsumerSystem(systemId, consumerSystemConfig, consumerSystemConfig.getExposeServiceDriverId());

        } else if (fileName.startsWith("ps-")) {
            ProviderSystemConfig providerSystemConfig = new ProviderSystemConfig();
            providerSystemConfig.setRegistryIp(properties.getProperty("registry-ip"));
            providerSystemConfig.setRegistryPort(properties.getProperty("registry-port"));
            providerSystemConfig.setRegistryType(properties.getProperty("registry-type"));
            providerSystemConfig.setMsgProtocol(properties.getProperty("msg-protocol"));
            providerSystemConfig.setLoadBalance(properties.getProperty("load-balance"));
            providerSystemConfig.setSpsid(properties.getProperty("spsid"));
            providerSystemConfig.setOthers(properties.getProperty("others"));
            providerSystemConfig.setPullServiceDriverId(properties.getProperty("pull-service-driver-id"));

            ProviderSystemFactory.buildProviderSystem(systemId, providerSystemConfig, providerSystemConfig.getPullServiceDriverId());
        } else
            return false;

        return true;
    }
}