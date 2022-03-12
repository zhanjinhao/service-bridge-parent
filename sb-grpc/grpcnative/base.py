from kazoo.client import KazooClient
import grpc
def getChannel(serviceName, version):
    zk = KazooClient(hosts="59.110.143.226:2181")
    zk.start()  # 与zookeeper连接
    data = zk.get('/grpcpython/' + serviceName + '/' + version)
    channel = grpc.insecure_channel(data[0])
    return channel