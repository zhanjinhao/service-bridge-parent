package sb.core.buffer;

import java.io.Serializable;
import java.util.Objects;

/**
 * 封装一个servicekey，由name和version组成
 */
public class ServiceKey implements Serializable {

    private String serviceName;
    private String version;
    private String sourceSpsid;

    private int state;
    public final static int LOST = 1;
    public final static int NEW = 2;
    public final static int HEAD = 4;
    public final static int ALIVE = 8;

    /**
     * serviceName 和 version 不允许为null
     *
     * @param serviceName
     * @param version
     */
    public ServiceKey(String serviceName, String version) {
        this.serviceName = serviceName;
        this.version = version;
    }

//    public ServiceKey(String serviceName, String version, String sourceSpsid) {
//        this.serviceName = serviceName;
//        this.version = version;
//        this.sourceSpsid = sourceSpsid;
//    }


    public String getSourceSpsid() {
        return sourceSpsid;
    }

    public void setSourceSpsid(String sourceSpsid) {
        this.sourceSpsid = sourceSpsid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceKey that = (ServiceKey) o;
        return Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version);
    }

    @Override
    public String toString() {
        return "ServiceKey{" +
                "name='" + serviceName + '\'' +
                ", version='" + version + '\'' +
                ", state=" + state +
                '}';
    }
}