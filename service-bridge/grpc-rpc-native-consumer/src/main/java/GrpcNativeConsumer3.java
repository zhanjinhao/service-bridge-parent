import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sb.rpc.grpc3.OrderDetail3;
import sb.rpc.grpc3.OrderRequest3;
import sb.rpc.grpc3.OrderService3Grpc;

import java.util.Date;

public class GrpcNativeConsumer3 {
    public static void main(String[] args) throws Exception {
//
//        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpcpython", "59.110.143.226:2181");
//        String str = zookeeperUtil.get(OrderService1Grpc.class.getName(), "1.0");
//
//        String[] split = str.split("&");
//        String ip = split[0].split("=")[1];
//        int port = Integer.parseInt(split[1].split("=")[1]);

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 14003)
                .usePlaintext()
                .build();

        OrderService3Grpc.OrderService3BlockingStub orderService3BlockingStub = OrderService3Grpc.newBlockingStub(managedChannel);

        for (int i = 0; i < 10000; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OrderRequest3 orderRequest3 = OrderRequest3.newBuilder().setOrderId(i + "order").build();
            OrderDetail3 orderDetail3 = orderService3BlockingStub.getOrderDetail(orderRequest3);

            String name = orderDetail3.getName();
            Date date = new Date(orderDetail3.getDate());
            double price = orderDetail3.getPrice();
            String buyer = orderDetail3.getBuyer();
            String seller = orderDetail3.getSeller();
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
