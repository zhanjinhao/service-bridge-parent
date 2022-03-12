package sb.core.buffer.cluster;

/**
 * 用于记录结点的新增和丢失等事件，HeartBeat和结点新上线的时候使用
 */
public class NodeRecordEvent {

    private int type;

    public static final int ALIVE = 1;
    public static final int LOST = -1;
    public static final int RECOVER = 2;
    public static final int OFFLINE = 3;

    private String ip;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
