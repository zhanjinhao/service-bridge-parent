package sb.rpc.grpc2;

import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class OrderService2Impl extends OrderService2Grpc.OrderService2ImplBase {

    static Map<String, OrderDetail2> orderDetailMap;

    static {
        orderDetailMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {

            OrderDetail2 orderDetail2 = OrderDetail2.newBuilder()
                    .setName("Huawei mobile phone")
                    .setDate(System.currentTimeMillis() - 24 * 60 * 60 * 1000 * 10)
                    .setPrice(i + 1000.0)
                    .setBuyer("lucy" + i)
                    .setSeller("grpc2").build();

            orderDetailMap.put(i + "order", orderDetail2);
        }
    }

    @Override
    public void getOrderDetail(OrderRequest2 request, StreamObserver<OrderDetail2> responseObserver) {

        String orderId = request.getOrderId();

        OrderDetail2 orderDetail2 = orderDetailMap.get(orderId);

        responseObserver.onNext(orderDetail2);

        responseObserver.onCompleted();
    }
}
