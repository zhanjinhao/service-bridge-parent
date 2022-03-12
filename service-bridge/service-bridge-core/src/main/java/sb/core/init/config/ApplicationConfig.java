package sb.core.init.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ApplicationConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    private static String PROJECT_PATH;
    public static String JAR_PATH;
    public static String CONFIG_PATH;
    public static String INSTRUCTION_PATH;
    public static String APP_CONFIG;

    static {

        File file = new File(".");

        try {
            PROJECT_PATH = file.getCanonicalPath();
            JAR_PATH = PROJECT_PATH + File.separator + "jar_directory";
            CONFIG_PATH = PROJECT_PATH + File.separator + "config_directory";
            INSTRUCTION_PATH = CONFIG_PATH + File.separator + "instructions";
            APP_CONFIG = CONFIG_PATH + File.separator + "service-bridge.properties";
        } catch (IOException e) {
            LOGGER.error("config error", e);
        }

    }
}