package sb.core.buffer;

import sb.expose.ConsumerSystemConfig;
import sb.expose.ExposeServiceDriver;

import java.io.Serializable;
import java.util.*;

public class ConsumerSystem implements Serializable {

    private String scsid;   // Service Consumer System ID
    private ConsumerSystemConfig consumerSystemConfig;
    private ExposeServiceDriver exposeServiceDriver;

    private int state;

    /**
     * 一个新构建的 ExposeRegistry 是 NEW <p>
     * expose服务后是 EXPOSE <p>
     * 暴露的服务全部关闭后是 EMPTY <p>
     * 注册中心丢失是 DEATH <p>
     */
    public static final int NEW = 1;
    public static final int EMPTY = 2;
    public static final int DEATH = 4;
    public static final int EXPOSE = 8;

    /**
     * 记录本系统存在哪些骨架
     */
    // <spsid, List<Skeleton>>
    private Map<String, List<Skeleton>> skeletonBuffer = new HashMap<>();

    public ConsumerSystem(String scsid, ConsumerSystemConfig consumerSystemConfig, ExposeServiceDriver exposeServiceDriver) {
        this.scsid = scsid;
        this.consumerSystemConfig = consumerSystemConfig;
        this.exposeServiceDriver = exposeServiceDriver;
    }

    public ExposeServiceDriver getExposeServiceDriver() {
        return exposeServiceDriver;
    }

    public void setExposeServiceDriver(ExposeServiceDriver exposeServiceDriver) {
        this.exposeServiceDriver = exposeServiceDriver;
    }

    public ConsumerSystemConfig getConsumerSystemConfig() {
        return consumerSystemConfig;
    }

    public void setConsumerSystemConfig(ConsumerSystemConfig consumerSystemConfig) {
        this.consumerSystemConfig = consumerSystemConfig;
    }

    public String getScsid() {
        return scsid;
    }

    public void setScsid(String scsid) {
        this.scsid = scsid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    // 初始化的时候会创建一个Map，不会返回null
    public Map<String, List<Skeleton>> getSkeletonBuffer() {
        return skeletonBuffer;
    }

    public Object getSkeletonObj(String spsid, String serviceName, String version) {
        Skeleton skeleton = getSkeleton(spsid, serviceName, version);
        if (skeleton == null) {
            return null;
        }
        return skeleton.getSkeleton();
    }

    public void recordSkeletonToConsumerSystem(String spsid, String serviceName, String version, Object skeletonObj) {

        Skeleton skeleton = new Skeleton(new ServiceKey(serviceName, version), skeletonObj);

        List<Skeleton> skeletons = skeletonBuffer.get(spsid);

        if (skeletons == null) {
            skeletons = new LinkedList<>();
        }

        skeletons.add(skeleton);

        skeletonBuffer.put(spsid, skeletons);
    }

    public void removeSkeletonFromConsumerSystem(String spsid, String serviceName, String version) {

        ServiceKey serviceKey = new ServiceKey(serviceName, version);

        List<Skeleton> skeletons = skeletonBuffer.get(spsid);
        if (skeletons == null) {
            return;
        }

        Iterator<Skeleton> iterator = skeletons.iterator();

        while (iterator.hasNext()) {
            Skeleton next = iterator.next();
            if (next.getServiceKey().equals(serviceKey)) {
                iterator.remove();
                break;
            }
        }

        skeletonBuffer.put(spsid, skeletons);
    }

    public boolean checkStoreSkeletonValid(String spsid, String serviceName, String version) {
        Object skeleton = getSkeleton(spsid, serviceName, version);
        return skeleton == null ? true : false;
    }

    public Skeleton getSkeleton(String spsid, String serviceName, String version) {
        List<Skeleton> skeletons = skeletonBuffer.get(spsid);

        if (skeletons == null) {
            return null;
        }

        Iterator<Skeleton> iterator = skeletons.iterator();
        while (iterator.hasNext()) {
            Skeleton next = iterator.next();
            ServiceKey serviceKey = next.getServiceKey();
            if (serviceKey.getServiceName().equals(serviceName) && serviceKey.getVersion().equals(version)) {
                return next;
            }
        }
        return null;
    }
}