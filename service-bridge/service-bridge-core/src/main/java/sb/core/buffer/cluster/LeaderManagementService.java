package sb.core.buffer.cluster;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 集群失去结点的时候不能进行 service 相关的操作。此功能使用一个拦截器实现。
 */
public class LeaderManagementService {

    private static volatile int nodeState;

    public static final int LEADER = 1;
    public static final int FOLLOWER = 2;
    public static final int LOST = 4;

    private static BlockingQueue<SocketWrap> followers = new LinkedBlockingQueue<>();

    // Leader丢失后，新 Leader 上线后通知列表里的其他结点
    public static void echoLeader() {

    }


    /**
     * 结点启动的时候 Leader 传输 Config 给他。包括 exposeRegistry 和 pullRegistry
     */
    public static void sendRegistryConfigs() {

    }

    /**
     * 有结点启动的时候 Leader 传输 Jar
     */
    public static void sendJars() {

    }

    /**
     * 新增Jar包的时候，传输至其他结点
     */
    public static void sendJar(String jarName) {

    }

    public static void sendInstruction(String instruction){

    }

    public static void syncFollowersFromPartners(){

    }

}