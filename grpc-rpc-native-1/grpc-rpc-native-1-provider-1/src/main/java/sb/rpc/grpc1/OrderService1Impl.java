package sb.rpc.grpc1;

import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class OrderService1Impl extends OrderService1Grpc.OrderService1ImplBase {

    static Map<String, OrderDetail1> orderDetailMap;

    static {
        orderDetailMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {

            OrderDetail1 orderDetail1 = OrderDetail1.newBuilder()
                    .setName("Huawe mobile phone")
                    .setDate(System.currentTimeMillis() - 24 * 60 * 60 * 1000 * 10)
                    .setPrice(i + 1000.0)
                    .setBuyer("lucy" + i)
                    .setSeller("grpc1").build();

            orderDetailMap.put(i + "order", orderDetail1);
        }
    }


    @Override
    public void getOrderDetail(OrderRequest1 request, StreamObserver<OrderDetail1> responseObserver) {
        String orderId = request.getOrderId();

        OrderDetail1 orderDetail1 = orderDetailMap.get(orderId);

        responseObserver.onNext(orderDetail1);

        responseObserver.onCompleted();

    }
}
