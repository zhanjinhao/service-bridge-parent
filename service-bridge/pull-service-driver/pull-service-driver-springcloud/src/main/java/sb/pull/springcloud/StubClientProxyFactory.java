package sb.pull.springcloud;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StubClientProxyFactory {

    private static HttpClient httpClient = new HttpClient();

    // serviceName, schema://ip:port
    public static Map<String, String> serviceMapping(String appUrl) throws Exception {

        GetMethod getMethod = new GetMethod(appUrl);

        int code = httpClient.executeMethod(getMethod);
        Map<String, String> serviceMapping = new HashMap<>();
        if (code == 200) {
            InputStream responseBodyAsStream = getMethod.getResponseBodyAsStream();

            //创建SAXReader
            SAXReader sax = new SAXReader();
            //获得文档对象
            Document read = sax.read(responseBodyAsStream);
            //获得根元素
            Element root = read.getRootElement();

            List rootChildren = root.elements();

            Iterator rootChildrenItr = rootChildren.iterator();
            while (rootChildrenItr.hasNext()) {
                Element rootChild = (Element) rootChildrenItr.next();
                String name = rootChild.getName();

                if ("application".endsWith(name)) {
                    List applicationChildren = rootChild.elements();
                    Iterator applicationChildrenItr = applicationChildren.iterator();
                    String serviceName = null;
                    String urlPrefix = "http://";
                    while (applicationChildrenItr.hasNext()) {
                        Element applicationChild = (Element) applicationChildrenItr.next();
                        if ("name".equals(applicationChild.getName())) {
                            serviceName = applicationChild.getText().toLowerCase();
                        }
                        if ("instance".equals(applicationChild.getName())) {
                            List instanceChildren = applicationChild.elements();
                            Iterator instanceChildrenItr = instanceChildren.iterator();
                            while (instanceChildrenItr.hasNext()) {
                                Element instanceChild = (Element) instanceChildrenItr.next();
                                if ("ipAddr".equals(instanceChild.getName())) {
                                    urlPrefix = urlPrefix + instanceChild.getText();
                                }
                                if("port".equals(instanceChild.getName())){
                                    urlPrefix = urlPrefix + ":" + instanceChild.getText();
;                                }
                            }
                        }
                    }
                    serviceMapping.put(serviceName, urlPrefix);
                }
            }
        }
        return serviceMapping;
    }

}
