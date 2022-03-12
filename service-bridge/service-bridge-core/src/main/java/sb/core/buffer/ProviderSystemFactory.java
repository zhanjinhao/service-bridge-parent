package sb.core.buffer;

import sb.pull.ProviderSystemConfig;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderSystemFactory {

    private ProviderSystemFactory() {

    }

    // registryId, pullRegistry
    private static Map<String, ProviderSystem> providerSystemMap = new ConcurrentHashMap<>(256, 0.1f);

    public static Map<String, ProviderSystem> getProviderSystemMap() {
        return providerSystemMap;
    }

    /**
     * 创建一个pullRegistry，并缓存至pullRegistriesMap
     *
     * @param spsid
     * @param providerSystemConfig
     * @return
     */
    public static ProviderSystem buildProviderSystem(String spsid, ProviderSystemConfig providerSystemConfig, String pullServiceDriverId) {

        ProviderSystem providerSystem = new ProviderSystem(spsid, providerSystemConfig, ServiceDriver.getPullServiceDriver(pullServiceDriverId));

        providerSystemMap.put(spsid, providerSystem);

        return providerSystem;

    }

    public static ProviderSystem getProviderSystem(String spsid) {
        return providerSystemMap.get(spsid);
    }


    public static Boolean storeMethods(String spsid, String serviceName, String version, Class interfaceClazz) {

        Map<ServiceKey, Stub> servicesBuffer = getServicesBuffer(spsid);

        Stub stub = servicesBuffer.get(new ServiceKey(serviceName, version));
        if (stub == null)
            return false;
        Method[] methods = interfaceClazz.getMethods();
        Map<String, Method> methodBuffer = stub.getMethodBuffer();
        for (Method m : methods) {
            methodBuffer.put(m.toString(), m);
            System.out.println("storeMethod   ->   " + m.toString());
        }
        return true;
    }


    public static Boolean removeMethods(String spsid, String serviceName, String version, Class interfaceClazz) {

        Map<ServiceKey, Stub> servicesBuffer = getServicesBuffer(spsid);

        Stub stub = servicesBuffer.get(new ServiceKey(serviceName, version));
        if (stub == null)
            return false;
        Method[] methods = interfaceClazz.getMethods();
        Map<String, Method> methodBuffer = stub.getMethodBuffer();
        for (Method m : methods) {
            methodBuffer.remove(m.toString());
            System.out.println("removeMethod   ->   " + m.toString());
        }
        return true;
    }


    private static Map<ServiceKey, Stub> getServicesBuffer(String spsid) {

        ProviderSystem providerSystem = providerSystemMap.get(spsid);

        return providerSystem.getStubBuffer();

    }

}