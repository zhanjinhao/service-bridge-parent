package sb.rpc.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sb.rpc.grpc1.OrderDetail1;
import sb.rpc.grpc1.OrderRequest1;
import sb.rpc.grpc1.OrderService1Grpc;

import java.util.Date;

public class GrpcNativeConsumer1 {
    public static void main(String[] args) {

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 15000)
                .usePlaintext()
                .build();

        OrderService1Grpc.OrderService1BlockingStub orderService1BlockingStub = OrderService1Grpc.newBlockingStub(managedChannel);

        for (int i = 0; i < 10000; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OrderRequest1 orderRequest1 = OrderRequest1.newBuilder().setOrderId(i + "order").build();
            OrderDetail1 orderDetail1 = orderService1BlockingStub.getOrderDetail(orderRequest1);

            String name = orderDetail1.getName();
            Date date = new Date(orderDetail1.getDate());
            double price = orderDetail1.getPrice();
            String buyer = orderDetail1.getBuyer();
            String seller = orderDetail1.getSeller();
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
