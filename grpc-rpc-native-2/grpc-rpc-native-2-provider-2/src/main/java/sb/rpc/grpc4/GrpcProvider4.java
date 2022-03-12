package sb.rpc.grpc4;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sb.rpc.grpc.ZookeeperUtil;

public class GrpcProvider4 {

    private Server server;

    public void start() throws Exception {

        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpc-native-2", "59.110.143.226:2181");

        String url = "ip=localhost&port=14004";
        zookeeperUtil.create("sb.rpc.grpc4.OrderService4Grpc", "1.0", url);

        server = ServerBuilder.forPort(14004).addService(new OrderService4Impl()).build().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GrpcProvider4.this.stop();
        }));

    }

    public void stop() {
        if (null != this.server) {
            this.server.shutdown();
        }
    }

    private void awaitTermination() throws InterruptedException {
        if (null != this.server) {
            this.server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        GrpcProvider4 grpcProvider4 = new GrpcProvider4();
        grpcProvider4.start();
        grpcProvider4.awaitTermination();
    }

}