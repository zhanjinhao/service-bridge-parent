package sb.core.buffer;

import sb.core.init.config.ApplicationConfig;
import sb.pull.ProviderSystemConfig;
import sb.pull.PullServiceDriver;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每个对象封装一个 提供者系统
 */
public class ProviderSystem  implements Serializable {

    // 唯一确定一个系统，从配置文件的文件名获取
    private String spsid;

    // 封装系统的状态
    private int state;

    /**
     * 一个新构建的 PullRegistry 是 NEW 的 <p>
     * pull 服务后是 PULL <p>
     * 拉取的服务全部释放后是 EMPTY <p>
     * 注册中心丢失是 DEATH <p>
     * 有 Service 被被 EXPOSE，就 PULL | EXPOSE，当然此时也是PULL <p>
     */
    public static final int NEW = 1;
    public static final int EMPTY = 2;
    public static final int DEATH = 4;
    public static final int PULL = 8;
    public static final int EXPOSE = 16;

    // 封装一个配置文件
    private ProviderSystemConfig providerSystemConfig;

    // 必须存在
    private PullServiceDriver pullServiceDriver;

    // jarName, URLClassLoaderWrap
    private Map<String, URLClassLoaderWrap> urlClassLoaderWrapBuffer = new HashMap<>();

    // 封装每个桩
    private Map<ServiceKey, Stub> stubBuffer = new HashMap<>(256, 0.1f);

    public ProviderSystem(String spsid, ProviderSystemConfig providerSystemConfig, PullServiceDriver pullServiceDriver) {
        this.spsid = spsid;
        this.providerSystemConfig = providerSystemConfig;
        this.pullServiceDriver = pullServiceDriver;
    }

    public String getSpsid() {
        return spsid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ProviderSystemConfig getProviderSystemConfig() {
        return providerSystemConfig;
    }

    public PullServiceDriver getPullServiceDriver() {
        return pullServiceDriver;
    }

    public Map<String, URLClassLoaderWrap> getUrlClassLoaderWrapBuffer() {
        return urlClassLoaderWrapBuffer;
    }

    public void setUrlClassLoaderWrapBuffer(Map<String, URLClassLoaderWrap> urlClassLoaderWrapBuffer) {
        this.urlClassLoaderWrapBuffer = urlClassLoaderWrapBuffer;
    }

    public Map<ServiceKey, Stub> getStubBuffer() {
        return stubBuffer;
    }

    public Boolean containService(String serviceName, String version) {
        ServiceKey serviceKey = new ServiceKey(serviceName, version);

        if (stubBuffer.get(serviceKey) != null)
            return true;
        return false;
    }

    public Stub getStub(String serviceName, String version) {
        ServiceKey serviceKey = new ServiceKey(serviceName, version);
        return this.getStubBuffer().get(serviceKey);
    }

    public URLClassLoaderWrap getUrlClassLoaderWrap(String jarName) {
        Map<String, URLClassLoaderWrap> urlClassLoaderWrapMap = this.getUrlClassLoaderWrapBuffer();
        return urlClassLoaderWrapMap.get(jarName);
    }

    /**
     * @param jarName
     * @return 返回null表示jarName非法，serviceKeys在构建对象时会被初始化
     */
    public List<ServiceKey> getUrlClassLoaderWrapServiceKeys(String jarName) {
        URLClassLoaderWrap urlClassLoaderWrap = getUrlClassLoaderWrap(jarName);

        if (urlClassLoaderWrap == null) {
            return null;
        }

        return urlClassLoaderWrap.getServiceKeys();
    }

    public URLClassLoader getUrlClassLoader(String jarName) {
        URLClassLoaderWrap urlClassLoaderWrap = getUrlClassLoaderWrap(jarName);
        if (urlClassLoaderWrap == null) {
            return null;
        }
        return urlClassLoaderWrap.getUrlClassLoader();
    }

    /**
     * 必须先调用 checkStoreStubValid
     * @param serviceName
     * @param version
     * @param stubObj
     * @param interfaceClazz
     */
    public void storeStub(String serviceName, String version, Object stubObj, Class<?> interfaceClazz) {

        URLClassLoaderWrap urlClassLoaderWrap = getUrlClassLoaderWrap(serviceName, version);

        ServiceKey serviceKey = new ServiceKey(serviceName, version);
        Stub stub = new Stub(urlClassLoaderWrap, stubObj);
        stub.setInterfaceClazz(interfaceClazz);

        stubBuffer.put(serviceKey, stub);
    }

    public int checkStoreStubValid(String serviceName, String version) {

        // 如果已经存在，就必须先删除
        if (containService(serviceName, version))
            return -1;

        URLClassLoaderWrap urlClassLoaderWrap = getUrlClassLoaderWrap(serviceName, version);

        if(urlClassLoaderWrap == null){
            return -2;
        }

        return 1;
    }

    public URLClassLoaderWrap getUrlClassLoaderWrap(String serviceName, String version){
        Stub stub = stubBuffer.get(new ServiceKey(serviceName, version));
        if (stub == null) {
            return null;
        }
        return stub.getUrlClassLoaderWrap();
    }

    public URLClassLoader getUrlClassLoader(String serviceName, String version) {
        Stub stub = stubBuffer.get(new ServiceKey(serviceName, version));
        if (stub == null) {
            return null;
        }
        URLClassLoaderWrap urlClassLoaderWrap = stub.getUrlClassLoaderWrap();
        if (urlClassLoaderWrap == null) {
            return null;
        }
        return urlClassLoaderWrap.getUrlClassLoader();
    }

    public Object getStubObj(String serviceName, String version) {
        Stub stub = getStub(serviceName, version);
        if (stub == null) {
            return null;
        }
        return stub.getStub();
    }

    public Class getInterfaceClazz(String serviceName, String version) {

        Stub stub = stubBuffer.get(new ServiceKey(serviceName, version));
        if (stub == null) {
            return null;
        }
        return stub.getInterfaceClazz();
    }

    public void removeStub(String serviceName, String version) {
        if (!containService(serviceName, version))
            return;

        stubBuffer.remove(new ServiceKey(serviceName, version));
    }

    public void linkConsumerSystemWithStub(String scsid, String serviceName, String version) {

        Stub stub = stubBuffer.get(new ServiceKey(serviceName, version));

        List<String> consumerSystemIds = stub.getScsids();

        consumerSystemIds.add(scsid);
    }

    public void recordStubToURLClassLoaderWrap(String serviceName, String version) {

        ServiceKey serviceKey = new ServiceKey(serviceName, version);

        Stub stub = stubBuffer.get(serviceKey);

        URLClassLoaderWrap urlClassLoaderWrap = stub.getUrlClassLoaderWrap();

        List<ServiceKey> serviceKeys = urlClassLoaderWrap.getServiceKeys();

        serviceKeys.add(serviceKey);

//        stub.setUrlClassLoaderWrap(urlClassLoaderWrap);

    }

    public void unlinkConsumerSystemWithStub(String scsid, String serviceName, String version) {
        Stub stub = stubBuffer.get(new ServiceKey(serviceName, version));
        List<String> consumerSystemIds = stub.getScsids();
        consumerSystemIds.remove(scsid);
    }

    public void removeServiceKeyFromURLClassLoaderWrap(String serviceName, String version) {

        ServiceKey serviceKey = new ServiceKey(serviceName, version);

        Stub stub = stubBuffer.get(serviceKey);

        if (stub == null)
            return;

        URLClassLoaderWrap urlClassLoaderWrap = stub.getUrlClassLoaderWrap();

        List<ServiceKey> serviceKeys = urlClassLoaderWrap.getServiceKeys();

        serviceKeys.remove(serviceKey);

        stub.setUrlClassLoaderWrap(urlClassLoaderWrap);
    }

    /**
     * 调用此方法之前必须先调用 checkJarNameValid
     * @param jarName
     * @throws Exception
     */
    public void buildURLClassLoader(String jarName) throws Exception {
        File file = new File(ApplicationConfig.JAR_PATH + File.separator + jarName);

        Map<String, URLClassLoaderWrap> urlClassLoaderWrapBuffer = getUrlClassLoaderWrapBuffer();

        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{});
        URL url = file.toURI().toURL();
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        add.setAccessible(true);
        add.invoke(urlClassLoader, url);
        URLClassLoaderWrap urlClassLoaderWrap = new URLClassLoaderWrap(spsid, jarName, urlClassLoader);

        urlClassLoaderWrapBuffer.put(jarName, urlClassLoaderWrap);
    }


    public int checkJarNameValid(String jarName) {
        File file = new File(ApplicationConfig.JAR_PATH + File.separator + jarName);
        if (!file.exists())
            return -1;

        Map<String, URLClassLoaderWrap> urlClassLoaderWrapBuffer = getUrlClassLoaderWrapBuffer();

        if (urlClassLoaderWrapBuffer.get(jarName) != null) {
            return -2;
        }

        return 1;
    }

    public Method getMethod(String serviceName, String version, String fullMethodName) {

        Stub stub = stubBuffer.get(new ServiceKey(serviceName, version));
        if (stub == null)
            return null;
        Map<String, Method> methodBuffer = stub.getMethodBuffer();

        return methodBuffer.get(fullMethodName);
    }
}