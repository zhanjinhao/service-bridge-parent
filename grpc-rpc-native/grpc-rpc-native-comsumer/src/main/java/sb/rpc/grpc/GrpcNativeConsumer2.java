package sb.rpc.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sb.rpc.grpc1.OrderDetail1;
import sb.rpc.grpc1.OrderRequest1;
import sb.rpc.grpc1.OrderService1Grpc;
import sb.rpc.grpc2.OrderDetail2;
import sb.rpc.grpc2.OrderRequest2;
import sb.rpc.grpc2.OrderService2Grpc;

import java.util.Date;

public class GrpcNativeConsumer2 {
    public static void main(String[] args) throws Exception {

        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpcpython", "59.110.143.226:2181");
        String str = zookeeperUtil.get(OrderService1Grpc.class.getName(), "1.0");

        String[] split = str.split("&");
        String ip = split[0].split("=")[1];
        int port = Integer.parseInt(split[1].split("=")[1]);

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build();

        OrderService2Grpc.OrderService2BlockingStub orderService2BlockingStub = OrderService2Grpc.newBlockingStub(managedChannel);

        for (int i = 0; i < 10000; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OrderRequest2 orderRequest2 = OrderRequest2.newBuilder().setOrderId(i + "order").build();
            OrderDetail2 orderDetail2 = orderService2BlockingStub.getOrderDetail(orderRequest2);

            String name = orderDetail2.getName();
            Date date = new Date(orderDetail2.getDate());
            double price = orderDetail2.getPrice();
            String buyer = orderDetail2.getBuyer();
            String seller = orderDetail2.getSeller();
            String out = "OrderDetail{" +
                    "name='" + name + '\'' +
                    ", date=" + date +
                    ", price=" + price +
                    ", buyer='" + buyer + '\'' +
                    ", seller='" + seller + '\'' +
                    '}';
            System.out.println(out);

        }

    }
}
