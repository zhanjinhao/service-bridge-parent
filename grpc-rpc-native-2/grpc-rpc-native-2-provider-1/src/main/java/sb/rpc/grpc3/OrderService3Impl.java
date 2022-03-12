package sb.rpc.grpc3;

import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class OrderService3Impl extends OrderService3Grpc.OrderService3ImplBase {

    static Map<String, OrderDetail3> orderDetailMap;

    static {
        orderDetailMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {

            OrderDetail3 orderDetail3 = OrderDetail3.newBuilder()
                    .setName("Huawe mobile phone")
                    .setDate(System.currentTimeMillis() - 24 * 60 * 60 * 1000 * 10)
                    .setPrice(i + 1000.0)
                    .setBuyer("lucy" + i)
                    .setSeller("grpc3").build();

            orderDetailMap.put(i + "order", orderDetail3);
        }
    }


    @Override
    public void getOrderDetail(OrderRequest3 request, StreamObserver<OrderDetail3> responseObserver) {
        String orderId = request.getOrderId();

        OrderDetail3 orderDetail3 = orderDetailMap.get(orderId);

        responseObserver.onNext(orderDetail3);

        responseObserver.onCompleted();

    }
}
