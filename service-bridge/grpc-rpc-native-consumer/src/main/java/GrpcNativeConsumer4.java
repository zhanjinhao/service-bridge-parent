import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sb.rpc.grpc4.OrderDetail4;
import sb.rpc.grpc4.OrderRequest4;
import sb.rpc.grpc4.OrderService4Grpc;

import java.util.Date;

public class GrpcNativeConsumer4 {
    public static void main(String[] args) throws Exception {

//        ZookeeperUtil zookeeperUtil = new ZookeeperUtil("grpcpython", "59.110.143.226:2181");
//        String str = zookeeperUtil.get(OrderService1Grpc.class.getName(), "1.0");
//
//        String[] split = str.split("&");
//        String ip = split[0].split("=")[1];
//        int port = Integer.parseInt(split[1].split("=")[1]);

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 14004)
                .usePlaintext()
                .build();

        OrderService4Grpc.OrderService4BlockingStub orderService4BlockingStub = OrderService4Grpc.newBlockingStub(managedChannel);

        for (int i = 0; i < 10000; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OrderRequest4 orderRequest4 = OrderRequest4.newBuilder().setOrderId(i + "order").build();
            OrderDetail4 orderDetail4 = orderService4BlockingStub.getOrderDetail(orderRequest4);

            String name = orderDetail4.getName();
            Date date = new Date(orderDetail4.getDate());
            double price = orderDetail4.getPrice();
            String buyer = orderDetail4.getBuyer();
            String seller = orderDetail4.getSeller();
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
