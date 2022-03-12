package sb.rpc.grpc1;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sb.rpc.grpc.ZookeeperUtil;

public class GrpcProvider1 {

    private Server server;

    public void start() throws Exception {

        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpc-native-1", "59.110.143.226:2181");

        String url = "ip=localhost&port=14001";
        zookeeperUtil.create("sb.rpc.grpc1.OrderService1Grpc", "1.0", url);

        server = ServerBuilder.forPort(14001).addService(new OrderService1Impl()).build().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GrpcProvider1.this.stop();
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
        GrpcProvider1 grpcProvider1 = new GrpcProvider1();
        grpcProvider1.start();
        grpcProvider1.awaitTermination();
    }

}