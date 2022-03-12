package sb.rpc.grpc2;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sb.rpc.grpc.ZookeeperUtil;

public class GrpcProvider2 {

    private Server server;

    public void start() throws Exception {

        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpc-native-1", "59.110.143.226:2181");

        String url = "ip=localhost&port=14002";
        zookeeperUtil.create("sb.rpc.grpc2.OrderService2Grpc", "1.0", url);

        server = ServerBuilder.forPort(14002).addService(new OrderService2Impl()).build().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GrpcProvider2.this.stop();
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
        GrpcProvider2 grpcProvider2 = new GrpcProvider2();
        grpcProvider2.start();
        grpcProvider2.awaitTermination();
    }

}
