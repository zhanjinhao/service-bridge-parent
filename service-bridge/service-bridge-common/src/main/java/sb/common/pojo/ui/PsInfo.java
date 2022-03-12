package sb.common.pojo.ui;

import java.util.Objects;

public class PsInfo implements Comparable<PsInfo> {

    private String spsid;
    private String jarName;
    private String serviceName;
    private String version;
    private int state;
    public static final int BUILD = 1;
    public static final int UN_BUILD = 2;
    public static final int USED = 3;

    public PsInfo(String spsid, String jarName, String serviceName, String version, int state) {
        this.spsid = spsid;
        this.jarName = jarName;
        this.serviceName = serviceName;
        this.version = version;
        this.state = state;
    }

    public PsInfo() {
    }

    public String getSpsid() {
        return spsid;
    }

    public void setSpsid(String spsid) {
        this.spsid = spsid;
    }

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
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
    public int compareTo(PsInfo o) {

        if(o.getSpsid().compareTo(this.spsid) > 0)
            return -1;
        else if(o.getSpsid().compareTo(this.spsid) < 0)
            return 1;

        if (o.getServiceName().compareTo(this.serviceName) > 0) {
            return -1;
        } else if (o.getServiceName().compareTo(this.serviceName) < 0)
            return 1;

        if (o.getVersion().compareTo(this.version) > 0)
            return -1;
        else if (o.getVersion().compareTo(this.version) < 0)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsInfo psInfo = (PsInfo) o;
        return Objects.equals(serviceName, psInfo.serviceName) &&
                Objects.equals(version, psInfo.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version);
    }

    @Override
    public String toString() {
        return "PsInfo{" +
                "spsid='" + spsid + '\'' +
                ", jarName='" + jarName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", version='" + version + '\'' +
                ", state=" + state +
                '}';
    }
}
