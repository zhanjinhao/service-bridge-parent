package sb.core.buffer.cluster;

import sb.core.buffer.ServiceKey;

public class NodeServiceEvent {

    private int type;

    private String jarName;

    private ServiceKey serviceKey;


    public static final int LOAD_JAR = 1;
    public static final int LOAD_SERVICE = 2;
    public static final int EXPOSE_SERVICE = 4;
    public static final int SHUTDOWN_SERVICE = 8;
    public static final int LOST_SERVICE = 16;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }
}
