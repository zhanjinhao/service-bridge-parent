package sb.core.buffer;

import java.io.Serializable;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个类加载器只能绑定一个注册中心，在这实际上是冗余了 pullRegistryId，同时一个累加器对应一个jar包，顺便冗余一下 jarName
 */
public class URLClassLoaderWrap implements Serializable {

    private String spsid;
    private String jarName;
    private URLClassLoader urlClassLoader;

    public URLClassLoaderWrap(String spsid, String jarName, URLClassLoader urlClassLoader) {
        this.spsid = spsid;
        this.jarName = jarName;
        this.urlClassLoader = urlClassLoader;
    }

    // 记录此Jar包构建出来的桩
    private List<ServiceKey> serviceKeys = new ArrayList<>();

    public List<ServiceKey> getServiceKeys() {
        return serviceKeys;
    }

    private int state;

    public static final int EMPTY = 1;
    public static final int NO_USE = 1;
    public static final int PULL = 1;
    public static final int EXPOSE = 1;


    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public String getSpsid() {
        return spsid;
    }

    public void setSpsid(String spsid) {
        this.spsid = spsid;
    }

    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }

    public void setUrlClassLoader(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    @Override
    public String toString() {
        return "URLClassLoaderWrap{" +
                "spsid='" + spsid + '\'' +
                ", jarName='" + jarName + '\'' +
                ", urlClassLoader=" + urlClassLoader +
                ", serviceKeys=" + serviceKeys +
                ", state=" + state +
                '}';
    }
}
