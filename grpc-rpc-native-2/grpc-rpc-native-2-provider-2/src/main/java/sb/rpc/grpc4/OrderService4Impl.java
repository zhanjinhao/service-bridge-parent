package sb.rpc.grpc4;

import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class OrderService4Impl extends OrderService4Grpc.OrderService4ImplBase {

    static Map<String, OrderDetail4> orderDetailMap;

    static {
        orderDetailMap = new HashMap<>();
        for (int i = 0; i < 10000; i++) {

            OrderDetail4 orderDetail4 = OrderDetail4.newBuilder()
                    .setName("Huawei mobile phone")
                    .setDate(System.currentTimeMillis() - 24 * 60 * 60 * 1000 * 10)
                    .setPrice(i + 1000.0)
                    .setBuyer("lucy" + i)
                    .setSeller("grpc4").build();

            orderDetailMap.put(i + "order", orderDetail4);
        }
    }


    @Override
    public void getOrderDetail(OrderRequest4 request, StreamObserver<OrderDetail4> responseObserver) {
        String orderId = request.getOrderId();

        OrderDetail4 orderDetail4 = orderDetailMap.get(orderId);

        responseObserver.onNext(orderDetail4);

        responseObserver.onCompleted();

    }
}
