package sb.pull.springcloud;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import sb.common.annotation.EurekaServiceConfig;
import sb.common.annotation.ServiceMethodConfig;

import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.Map;

public class StubClientProxy {

    private static HttpClient client = new HttpClient();

    private Map<String, String> serviceMapping;

    public StubClientProxy(String appsUrl) {
        try {
            serviceMapping = StubClientProxyFactory.serviceMapping(appsUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object createHttpClient(Class interfaceClass) throws Exception {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Object httpClient = Proxy.newProxyInstance(
                loader,
                new Class[]{interfaceClass},
                (proxy, method, args) -> {

                    EurekaServiceConfig eurekaServiceConfigAnnotation = (EurekaServiceConfig) interfaceClass.getAnnotation(EurekaServiceConfig.class);

                    String serviceName = eurekaServiceConfigAnnotation.serviceName();
                    String requestPrefix = eurekaServiceConfigAnnotation.requestPrefix();

                    String urlPrefix = serviceMapping.get(serviceName);

                    ServiceMethodConfig serviceMethodConfigAnnotation = method.getAnnotation(ServiceMethodConfig.class);

                    String methodMapping = serviceMethodConfigAnnotation.methodMapping();

                    if (urlPrefix.endsWith("/")) {
                        urlPrefix = urlPrefix.substring(0, urlPrefix.length() - 1);
                    }

                    if (requestPrefix.endsWith("/")) {
                        requestPrefix = requestPrefix.substring(0, requestPrefix.length() - 1);
                    }

                    if (requestPrefix.startsWith("/")) {
                        requestPrefix = requestPrefix.substring(1);
                    }

                    if (methodMapping.startsWith("/")) {
                        methodMapping = methodMapping.substring(1);
                    }

                    if (methodMapping.endsWith("/")) {
                        methodMapping = methodMapping.substring(0, methodMapping.length() - 1);
                    }

                    String url = urlPrefix + "/" + requestPrefix + "/" + methodMapping;

                    int length = args.length;
                    for (int i = 0; i < length; i++) {
                        url = url + "/" + args[i];
                    }

                    GetMethod getMethod = new GetMethod(url);

                    int code = client.executeMethod(getMethod);

                    if (code == 200) {

                        InputStream responseBodyAsStream = getMethod.getResponseBodyAsStream();

                        StringBuilder stringBuilder = new StringBuilder();

                        byte[] responseBody = new byte[1024 * 1024];
                        int b;
                        while ((b = responseBodyAsStream.read(responseBody)) != -1) {
                            String s = new String(responseBody, 0, b, "utf-8");
                            stringBuilder.append(s);
                        }
                        Class<?> returnType = method.getReturnType();
                        String result = stringBuilder.toString();
                        if(result == null)
                            return null;
                        return JSON.parseObject(result, returnType);
                    }

                    return null;
                });

        return httpClient;
    }

}