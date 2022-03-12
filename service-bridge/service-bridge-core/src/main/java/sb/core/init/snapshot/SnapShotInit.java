package sb.core.init.snapshot;

import sb.core.init.config.ApplicationConfig;
import sb.core.operation.write.serialize.SerializeInterface;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class SnapShotInit {

    public static SerializeInterface serializeInterface;

    static {
        String appConfig = ApplicationConfig.APP_CONFIG;
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(appConfig);
            Properties properties = new Properties();
            properties.load(inputStream);
            String className = properties.getProperty("serialize-class");
            try {
                Class<?> aClass = Class.forName(className);
                Object serializeInterfaceImpl = aClass.newInstance();
                serializeInterface = (SerializeInterface) serializeInterfaceImpl;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
