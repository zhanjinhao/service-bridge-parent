package sb.core.buffer;

import java.io.Serializable;

public class Skeleton implements Serializable {

    private ServiceKey serviceKey;

    private Object skeleton;

    public Skeleton() {
    }

    public Skeleton(ServiceKey serviceKey, Object skeleton) {
        this.serviceKey = serviceKey;
        this.skeleton = skeleton;
    }

    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    public Object getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(Object skeleton) {
        this.skeleton = skeleton;
    }

    @Override
    public String toString() {
        return "Skeleton{" +
                "serviceKey=" + serviceKey +
                ", skeleton=" + skeleton +
                '}';
    }
}