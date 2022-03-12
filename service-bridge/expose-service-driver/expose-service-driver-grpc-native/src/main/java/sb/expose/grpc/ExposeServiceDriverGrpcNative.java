package sb.expose.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import sb.expose.ConsumerSystemConfig;
import sb.expose.ExposeServiceDriver;
import sb.rpc.grpc.ZookeeperUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExposeServiceDriverGrpcNative implements ExposeServiceDriver {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private Map<ConsumerSystemConfig, Server> serverMap = new HashMap<>();
    private Map<ConsumerSystemConfig, List<BindableService>> bindableServiceMap = new HashMap<>();
    private Map<ConsumerSystemConfig, Thread> threadMap = new HashMap<>();
    private Map<ConsumerSystemConfig, ZookeeperUtil> zookeeperUtilMap = new HashMap<>();

    // interfaceClazz 是proto文件生成的父类，service是桩
    @Override
    public Object buildSkeleton(Class interfaceClazz, Object stub, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {
        executorService.submit(() -> {

            String namespace = consumerSystemConfig.getOthers();
            String registryIp = consumerSystemConfig.getRegistryIp();
            String registryPort = consumerSystemConfig.getRegistryPort();
            int serverPort = Integer.parseInt(consumerSystemConfig.getServerPort());

            String connectUrl = registryIp + ":" + registryPort;

            ZookeeperUtil zookeeperUtil = zookeeperUtilMap.get(consumerSystemConfig);
            if (zookeeperUtil == null) {
                zookeeperUtil = new ZookeeperUtil(namespace, connectUrl);
                zookeeperUtilMap.put(consumerSystemConfig, zookeeperUtil);
            }

            String url = "localhost:" + serverPort;

            try {
                zookeeperUtil.create(serviceName, version, url);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            System.out.println(contextClassLoader);

            String name = interfaceClazz.getName();
            String substring = name.substring(0, name.lastIndexOf(".") + 1);
            String s = name.replaceAll(substring, "").replace("Grpc", "ImplBase");

            Class clazz = null;
            try {
                clazz = contextClassLoader.loadClass(name + "$" + s);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            BindableService bindableService = (BindableService) DynamicProxyFactory.dynamicProxy(clazz, stub);

            Server server = serverMap.get(consumerSystemConfig);

            if (server == null) {

                try {
                    ServerBuilder serverBuilder = ServerBuilder.forPort(serverPort);
                    server = serverBuilder.addService(bindableService).build().start();

                    List<BindableService> bindableServices = new ArrayList<>();
                    bindableServices.add(bindableService);
                    bindableServiceMap.put(consumerSystemConfig, bindableServices);
                    threadMap.put(consumerSystemConfig, Thread.currentThread());
                    serverMap.put(consumerSystemConfig, server);

                    server.awaitTermination();
                } catch (InterruptedException e) {
                    // do nothing
                    System.out.println("server restart ! ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                server.shutdown();
                Thread thread = threadMap.get(consumerSystemConfig);
                thread.interrupt();

                List<BindableService> bindableServices = bindableServiceMap.get(consumerSystemConfig);
                bindableServices.add(bindableService);

                try {

                    ServerBuilder serverBuilder = ServerBuilder.forPort(serverPort);
                    Iterator<BindableService> iterator = bindableServices.iterator();

                    while (iterator.hasNext()) {
                        BindableService next = iterator.next();
                        serverBuilder = serverBuilder.addService(next);
                    }

                    server = serverBuilder.build().start();

                    serverMap.put(consumerSystemConfig, server);
                    threadMap.put(consumerSystemConfig, Thread.currentThread());

                    server.awaitTermination();
                }catch (InterruptedException e){
                    // do nothing
                    System.out.println("server restart ! ");
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        return null;
    }

    @Override
    public Boolean removeSkeleton(Class interfaceClazz, Object service, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {
        return null;
    }

    @Override
    public String getExposeServiceDriverId() {
        return "expose-service-driver-grpc-native";
    }

    @Override
    public Boolean checkRegistryAlive(ConsumerSystemConfig consumerSystemConfig) {
        return null;
    }

}