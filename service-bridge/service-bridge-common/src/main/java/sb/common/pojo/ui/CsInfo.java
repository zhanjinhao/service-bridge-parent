package sb.common.pojo.ui;

public class CsInfo implements Comparable<CsInfo>{

    private String scsid;
    private String spsid;

    private String serviceName;

    private String version;

    private int state;
    public static final int BUILD = 1;
    public static final int LOST = 2;

    public CsInfo(String scsid, String spsid, String serviceName, String version, int state) {
        this.scsid = scsid;
        this.spsid = spsid;
        this.serviceName = serviceName;
        this.version = version;
        this.state = state;
    }


    public CsInfo() {
    }

    public String getSpsid() {
        return spsid;
    }

    public void setSpsid(String spsid) {
        this.spsid = spsid;
    }

    public String getScsid() {
        return scsid;
    }

    public void setScsid(String scsid) {
        this.scsid = scsid;
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
    public int compareTo(CsInfo o) {
        if(o.getScsid().compareTo(this.scsid) > 0)
            return -1;
        else if(o.getScsid().compareTo(this.scsid) < 0)
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



}
