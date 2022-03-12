package sb.common.pojo.ui;

import java.io.Serializable;

public class JarInfo implements Serializable, Comparable<JarInfo> {

    private String name;
    private String spsid;
    private int state;
    public static final int LOAD = 1;
    public static final int UN_LOAD = 2;

    public JarInfo(String name, String spsid, int state) {
        this.name = name;
        this.spsid = spsid;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpsid() {
        return spsid;
    }

    public void setSpsid(String spsid) {
        this.spsid = spsid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int compareTo(JarInfo o) {
        if(o.getName().compareTo(this.name) > 0)
            return -1;
        else if(o.getName().compareTo(this.name) < 0)
            return 1;
        return 0;
    }
}
