package sb.rpc.grpc3;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sb.rpc.grpc.ZookeeperUtil;

public class GrpcProvider3 {

    private Server server;

    public void start() throws Exception {

        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpc-native-2", "59.110.143.226:2181");

        String url = "ip=localhost&port=14003";
        zookeeperUtil.create("sb.rpc.grpc3.OrderService3Grpc", "1.0", url);

        server = ServerBuilder.forPort(14003).addService(new OrderService3Impl()).build().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GrpcProvider3.this.stop();
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
        GrpcProvider3 grpcProvider3 = new GrpcProvider3();
        grpcProvider3.start();
        grpcProvider3.awaitTermination();
    }

}