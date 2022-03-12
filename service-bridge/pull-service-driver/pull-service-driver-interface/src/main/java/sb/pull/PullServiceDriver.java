package sb.pull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

/**
 * 此接口的实例都会被设置为单例。
 */
public interface PullServiceDriver {

    /**
     * 从RPC注册中心拉取服务
     *
     * @param interfaceClazz     暴露服务的接口字节码
     * @param serviceName        服务名
     * @param version            服务版本号
     * @param providerSystemConfig 注册中心的信息
     * @return 抛出异常和返回空都被认为是构建失败。构建失败时请释放所有系统的资源，比如端口号，同时需要将系统回复到此函数执行前的状态
     */
    Object buildStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig) throws Exception;


    Object removeStub(Class interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig);

    default boolean validateServiceClazz(Class interfaceClazz, Object service) {
        if(Objects.isNull(service))
            return false;
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class c : interfaces) {
            if (c.equals(interfaceClazz)) {
                return true;
            }
        }
        return false;
    }

    default String decode(String url) {
        try {
            String prevURL = "";
            String decodeURL = url;
            while (!prevURL.equals(decodeURL)) {
                prevURL = decodeURL;
                decodeURL = URLDecoder.decode(decodeURL, "UTF-8");
            }
            return decodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while decoding" + e.getMessage();
        }
    }
    /**
     * 每一个 driver 对象都有一个唯一ID。
     * 这个 ID 会被配置到配置文件中。
     */
    String getPullServiceDriverId();


    Boolean checkServiceAlive(Class interfaceClazz, String serviceName, String version);


    /**
     * 应用启动的时候会有线程停在锁上，此方法用于唤醒线程并在线程结束之后将其再停留在锁对象上
     */
    Void updateService();


    Boolean checkRegistryAlive(ProviderSystemConfig providerSystemConfig);


    List<ProvidedServiceKey> getAllServiceKey(ProviderSystemConfig providerSystemConfig);


}