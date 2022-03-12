import time
from grpcnative import OrderServiceProto4_pb2, OrderServiceProto4_pb2_grpc
from grpcnative import base


def run():

    channel = base.getChannel('sb.rpc.grpc4.OrderService4Grpc', '1.0')
    stub = OrderServiceProto4_pb2_grpc.OrderService4Stub(channel)

    counter = 1
    while counter <= 10000:
        orderid = str(counter) + 'order'
        response = stub.getOrderDetail(OrderServiceProto4_pb2.OrderRequest4(orderId=orderid))
        time2 = response.date / 1000

        timeArray2 = time.localtime(time2)
        format_time = time.strftime("%Y-%m-%d %H:%M:%S", timeArray2)

        print('name=', end='')
        print(response.name, end=', ')
        print('date=', end='')
        print(format_time, end=', ')
        print('price=', end='')
        print(response.price, end=', ')
        print('buyer=', end='')
        print(response.buyer, end=', ')
        print('seller=', end='')
        print(response.seller)


        counter += 1
        time.sleep(1)

if __name__ == '__main__':
    run()
