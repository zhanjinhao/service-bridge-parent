package sb.rpc.grpc;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.Iterator;
import java.util.List;

public class ZookeeperUtil {

    private CuratorFramework client;

    public ZookeeperUtil(String namespace, String connectUrl) {
        RetryPolicy retryPolicy = new RetryOneTime(3000);
        client = CuratorFrameworkFactory.builder()
                .connectString(connectUrl)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace(namespace)
                .build();
        client.start();
        System.out.println("连接 zookeeper 成功, url=" + connectUrl);
    }


    public void create(String serviceName, String version, String data) throws Exception {


        // 新增节点
        client.create()
                .creatingParentsIfNeeded()
                // 节点的类型
                .withMode(CreateMode.EPHEMERAL)
                // 节点的权限列表 world:anyone:cdrwa
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                // arg1:节点的路径
                // arg2:节点的数据
                .forPath("/" + serviceName + "/" + version, data.getBytes());

    }

    public String get(String serviceName, String version) throws Exception {
        // 读取节点数据
        byte[] bys = client.getData()
                // 节点的路径
                .forPath("/" + serviceName + "/" + version);
        return new String(bys);
    }

    public String getServiceKey() throws Exception {

        List<String> list = client.getChildren()
                // 节点路径
                .forPath("/");

        Iterator<String> iterator = list.iterator();

        StringBuilder stringBuilder = new StringBuilder();

        while (iterator.hasNext()) {
            String next = iterator.next();
            List<String> list2 = client.getChildren().forPath("/" + next);
            Iterator<String> iterator1 = list2.iterator();
            while (iterator1.hasNext()) {
                String next1 = iterator1.next();
                stringBuilder.append(next + "`" + next1 + "&");
            }

        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();

    }

    public static void main(String[] args) throws Exception {

        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpcpython", "59.110.143.226:2181");
        String serviceKey = zookeeperUtil.getServiceKey();

        System.out.println(serviceKey);
    }

}