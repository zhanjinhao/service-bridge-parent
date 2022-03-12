package sb.common.pojo.ui;

import java.io.Serializable;

public class DownloadInfo implements Serializable, Comparable<DownloadInfo> {

    private String name;

    private int type;
    private int state;

    public static final int JAR = 1;
    public static final int IDL = 2;

    public static final int USING = 1;
    public static final int NO_USE = 2;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public DownloadInfo(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(DownloadInfo o) {

        return o.getName().compareTo(this.name);
    }
}
