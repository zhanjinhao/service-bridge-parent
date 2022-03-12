package sb.expose;

import java.lang.reflect.Method;

public interface ExposeServiceDriver {


    /**
     *
     * @param interfaceClazz
     * @param stub
     * @param serviceName
     * @param version
     * @param consumerSystemConfig
     * @param others
     * @return 构建失败必须返回null
     */
    Object buildSkeleton(Class interfaceClazz, Object stub, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others);

    /**
     * 关闭已经暴露的服务
     *
     * @param interfaceClazz    暴露服务的接口字节码
     * @param service           服务器存根
     * @param serviceName       服务名
     * @param version           服务版本号
     * @param others            其他信息
     */
    Boolean removeSkeleton(Class interfaceClazz, Object service, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others);


    /**
     * 每一个driver对象都有一个唯一ID
     */
    String getExposeServiceDriverId();

    Boolean checkRegistryAlive(ConsumerSystemConfig consumerSystemConfig);

    default Boolean validateServiceClazz(Class interfaceClazz, Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class c : interfaces) {
            if (c.equals(interfaceClazz)) {
                return true;
            }
        }
        return false;
    }

    default void storeMethods(Class interfaceClazz, Object service, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {

        try {

            Object providerSystemConfig = others[0];

            Class<?> providerSystemConfigClazz = Class.forName("sb.pull.ProviderSystemConfig");

            Method getSpsid = providerSystemConfigClazz.getMethod("getSpsid");

            String spsid = (String)getSpsid.invoke(providerSystemConfig);

            Class<?> providerSystemFactoryClazz = Class.forName("sb.core.pojo.buffer.ProviderSystemFactory");

            Method storeMethod = providerSystemFactoryClazz.getMethod("storeMethods", String.class, String.class, String.class, Class.class);

            storeMethod.invoke(null, spsid, serviceName, version, interfaceClazz);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    default void removeMethods(Class interfaceClazz, Object service, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {

        try {

            Class<?> providerSystemFactoryClazz = Class.forName("sb.core.pojo.buffer.ProviderSystemFactory");

            Method storeMethod = providerSystemFactoryClazz.getMethod("removeMethods", String.class, String.class, String.class, Class.class);

            String spsid = consumerSystemConfig.getOthers();

            storeMethod.invoke(null, spsid, serviceName, version, interfaceClazz);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}