package sb.core.operation.write;

public class CompoundWriteOperation extends AbstractTransactionBasicWriteOperation implements CompoundWriteOperationInterface {

    private CompoundWriteOperation() { }

    public static CompoundWriteOperation newCompoundWriteOperation() {
        return new CompoundWriteOperation();
    }

    @Override
    public void reBuildSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception {
        removeSkeleton(scsid, spsid, serviceName, version);
        buildSkeleton(scsid, spsid, serviceName, version);
    }

    @Override
    public void reBuildStub(String spsid, String jarName, String serviceName, String version) throws Exception {

    }

    @Override
    public void reLoadJar(String spsid, String jarName) throws Exception {

    }

//    public Result reexposeService(String scsid, String spsid, String serviceName, String version) throws Exception {
//        atomicOperationService.removeSkeleton(scsid, spsid, serviceName, version);
//        atomicOperationService.buildSkeleton(scsid, spsid, serviceName, version);
//        return Result.getSuccess();
//    }
//
//    public Result updateService(String spsid, String jarName, String serviceName, String version, Boolean rebuildStub) throws Exception {
//
//        ProviderSystem registry = ProviderSystemFactory.getProviderSystem(spsid);
//
//        ServiceKey serviceKey = new ServiceKey(serviceName, version);
//
//        Stub stub = registry.getStubBuffer().get(serviceKey);
//
//        Class interfaceClazz = stub.getInterfaceClazz();
//
//        String className = interfaceClazz.getName();
//
//        List<String> scsids = stub.getScsids();
//
//        atomicOperationService.removeStub(spsid, jarName, serviceName, version);
//
//        atomicOperationService.buildStub(spsid, jarName, serviceName, version);
//
//        if (rebuildStub) {
//            Iterator<String> iterator = scsids.iterator();
//            while (iterator.hasNext()) {
//                atomicOperationService.buildSkeleton(iterator.next(), spsid, serviceName, version);
//            }
//        }
//        return Result.getSuccess();
//    }
//
//    public Result updateJar(String spsid, String jarName, Boolean rebuildStub, Boolean rebuildSkeleton) throws Exception {
//
//        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
//
//        URLClassLoaderWrap urlClassLoaderWrap = providerSystem.getUrlClassLoaderWrapBuffer().get(jarName);
//
//        // 此包构建的桩
//        List<ServiceKey> serviceKeys = urlClassLoaderWrap.getServiceKeys();
//
//        Map<ServiceKey, Stub> servicesBuffer = providerSystem.getStubBuffer();
//
//        Map<ServiceKey, Stub> tempServicesBuffer = new HashMap<>();
//
//        Iterator<ServiceKey> iteratorJar = serviceKeys.iterator();
//
//        // 暂存 servicesBuffer
//        while (iteratorJar.hasNext()) {
//            ServiceKey next = iteratorJar.next();
//            tempServicesBuffer.put(next, servicesBuffer.get(next));
//        }
//
//        // 复制一份对象，防止等下被释放
//        Map<ServiceKey, Stub> cloneServicesBuffer = CloneUtils.clone((HashMap) tempServicesBuffer);
//
//        atomicOperationService.releaseJar(spsid, jarName);
//        atomicOperationService.loadJar(spsid, jarName);
//
//        if (rebuildStub) {
//
//            Iterator<ServiceKey> tempIteratorFullJar = cloneServicesBuffer.keySet().iterator();
//
//            while (tempIteratorFullJar.hasNext()) {
//
//                ServiceKey next = tempIteratorFullJar.next();
//
//                Stub stub = cloneServicesBuffer.get(next);
//
//                List<String> scsids = stub.getScsids();
//
//                atomicOperationService.buildStub(spsid, jarName, next.getServiceName(), next.getVersion());
//
//                if (rebuildSkeleton) {
//
//                    Iterator<String> scsidsiterator = scsids.iterator();
//                    while (scsidsiterator.hasNext()) {
//                        atomicOperationService.buildSkeleton(scsidsiterator.next(), spsid, next.getServiceName(), next.getVersion());
//                    }
//
//                }
//            }
//        }
//
//        return Result.getSuccess();
//    }
//
//
//    public Result updateService(String pullRegistryId, String jarName, String serviceName, String version) throws Exception {
//        return updateService(pullRegistryId, jarName, serviceName, version, true);
//    }
//
//    public Result updateJar(String pullRegistryId, String jarName) throws Exception {
//        return updateJar(pullRegistryId, jarName, true, true);
//    }

}