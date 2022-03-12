package sb.client.proxy;

import sb.client.pojo.URLPojo;

import java.util.HashMap;
import java.util.Map;

public class SBHttpClientProxyFactory {

    // fullClassName, httpClientObject
    private static Map<String, Object> sbHttpClientProxyMap = new HashMap<>(1024, 0.1f);

    public static <T> T createHttpClient(Class<T> clazz, URLPojo urlPojo) {

        urlPojo.setServiceName(clazz.getName());

        SBHttpClientProxy sbHttpClientProxy = new SBHttpClientProxy(urlPojo);

        Object httpClient = null;
        try {
            httpClient = sbHttpClientProxy.createHttpClient(clazz);
        } catch (Exception e) {
            httpClient = null;
            e.printStackTrace();
        }
        sbHttpClientProxyMap.put(clazz.getName(), httpClient);

        return (T) httpClient;

    }

    public static Object getHttpClient(String fullClassName) {
        return sbHttpClientProxyMap.get(fullClassName);
    }

}