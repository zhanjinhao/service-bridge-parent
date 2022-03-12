#! /usr/bin/env python
# -*- coding: utf-8 -*-
# from __future__ import print_function
import grpc
from example import helloworld_pb2, helloworld_pb2_grpc


def run():
    # NOTE(gRPC Python Team): .close() is possible on a channel and should be
    # used in circumstances in which the with statement does not fit the needs
    # of the code.
    with grpc.insecure_channel('localhost:15000') as channel:
        stub = helloworld_pb2_grpc.GreeterStub(channel)
        response = stub.SayHello(helloworld_pb2.HelloRequest(name='张三'))
    print("Greeter client received: " + response.message)


if __name__ == '__main__':
    run()
