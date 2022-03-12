package sb.core.buffer.cluster;

import java.net.Socket;

public class SocketWrap {

    private Socket socket;

    /**
     * 小于 0 表示 Socket无效
     * 大于 0 表示 Socket有效
     * 初始化或者 socket == null 的时候 type 为0
     */
    private int type;


}
