package sb.core.buffer.cluster;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClusterManagementService {

    private String leaderIp = null;

    // 记录当前结点的
    public static final BlockingQueue<NodeWrap> nodeRecordList = new LinkedBlockingQueue<>();

    // 每次对nodeList的修改都是一个事件
    public static final BlockingQueue<NodeRecordEvent> nodeRecordEvents = new LinkedBlockingQueue<>();

    // 开启一个线程负责在 Leader 模式发送心跳包
    private static void sendHeartBeat() {

    }

    // 开启一个线程负责在 Follower 模式接收心跳包
    private static void receiveHeartBeat() {

    }

    // 开启一个线程，负责同步 nodeRecordList 和 node-record 文件 即启动时初始化 nodeRecordList
    private static void syncMemoryAndFile() {

    }

}