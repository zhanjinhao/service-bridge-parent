package sb.core.buffer;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 封装service的属性，一个 stub，一个 loader，一个 state
 */
public class Stub  implements Serializable {

    // 此桩被哪些 ConsumerSystem 使用了
    private List<String> scsids = new LinkedList<>();

    // 类加载器和服务一一对应
    private URLClassLoaderWrap urlClassLoaderWrap;

    // PSD 构建的桩
    private Object stub;

    private int state = Stub.PULL;

    // 用时间换空间，为了更方便的进行Http接口的暴露
    private Class interfaceClazz;
    private Map<String, Method> methodBuffer = new HashMap<>(1024, 0.1f);

    /**
     * 一个新拉取的服务是 PULL 的 <p>
     * 服务被 checkServiceAlive() 判定死亡后是 DEATH <p>
     * 此服务被 EXPOSE 后 是 PULL | EXPOSE <p>
     * 注册中心失去后，服务可能附加一个 LOST 状态
     */
    public static final int PULL = 1;
    public static final int DEATH = 2;
    public static final int EXPOSE = 4;
    public static final int LOST = 8;

    public Stub(URLClassLoaderWrap urlClassLoaderWrap) {
        this.urlClassLoaderWrap = urlClassLoaderWrap;
    }

    public Stub(Object stub) {
        this.stub = stub;
    }

    public Stub(URLClassLoaderWrap urlClassLoaderWrap, Object stub) {
        this.urlClassLoaderWrap = urlClassLoaderWrap;
        this.stub = stub;
    }

    public Stub() {

    }

    public URLClassLoaderWrap getUrlClassLoaderWrap() {
        return urlClassLoaderWrap;
    }

    public void setUrlClassLoaderWrap(URLClassLoaderWrap urlClassLoaderWrap) {
        this.urlClassLoaderWrap = urlClassLoaderWrap;
    }

    public Object getStub() {
        return stub;
    }

    public void setStub(Object stub) {
        this.stub = stub;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    // 在创建对象的时候就会初始化，所以不会返回null
    public List<String> getScsids() {
        return scsids;
    }

    public Map<String, Method> getMethodBuffer() {
        return methodBuffer;
    }

    public void setMethodBuffer(Map<String, Method> methodBuffer) {
        this.methodBuffer = methodBuffer;
    }

    public Class getInterfaceClazz() {
        return interfaceClazz;
    }

    public void setInterfaceClazz(Class interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }

    @Override
    public String toString() {
        return "Stub{" +
                "consumerSystemIds=" + scsids +
                ", urlClassLoaderWrap=" + urlClassLoaderWrap +
                ", stub=" + stub +
                ", state=" + state +
                ", interfaceClazz=" + interfaceClazz +
                ", methodBuffer=" + methodBuffer +
                '}';
    }
}