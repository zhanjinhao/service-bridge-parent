package sb.core.operation.write;

import sb.core.buffer.*;
import sb.core.exception.WriteOperationException;
import sb.expose.ExposeServiceDriver;
import sb.pull.PullServiceDriver;
import sun.misc.ClassLoaderUtil;

import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;

/**
 * 对基本写操作进行实现
 */
public abstract class AbstractBasicWriteOperationImpl implements BasicWriteOperationInterface {
    @Override
    public void loadJar(String spsid, String jarName) throws Exception {

        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
        if (providerSystem == null)
            throw new WriteOperationException(WriteOperationException.PS_NOT_EXIST, "provider system does not exist, @<" + spsid + ">");
        int code = providerSystem.checkJarNameValid(jarName);
        if (code == -1) {
            throw new WriteOperationException(WriteOperationException.JAR_FILE_NOT_EXIST, "jar file does not exist, @<" + jarName + ">");
        } else if (code == -2) {
            throw new WriteOperationException(WriteOperationException.URLCLASSLOADER_HAS_EXISTED, "urlClassloader has existed, @<" + jarName + ">");
        }
        // 执行到这，所有的检查工作都已经完成
        try {
            providerSystem.buildURLClassLoader(jarName);
        } catch (Exception e) {
            throw new WriteOperationException(WriteOperationException.INVOKE_LOAD_JAR, "invoke LOAD_JAR error. @<" + spsid + ", " + jarName + ">");
        }
    }

    @Override
    public void buildStub(String spsid, String jarName, String serviceName, String version) throws Exception {
        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
        if (providerSystem == null)
            throw new WriteOperationException(WriteOperationException.PS_NOT_EXIST, "provider system does not exist, @<" + spsid + ">");

        PullServiceDriver pullServiceDriver = providerSystem.getPullServiceDriver();
        if (pullServiceDriver == null)
            throw new WriteOperationException(WriteOperationException.PSD_NOT_EXIST, "PSD does not exist, @<" + providerSystem.getProviderSystemConfig().getPullServiceDriverId() + ">");

        URLClassLoader classLoader = providerSystem.getUrlClassLoader(jarName);
        if (classLoader == null)
            throw new WriteOperationException(WriteOperationException.URLCLASSLOADER_NOT_EXIST, "urlClassloader does not exist, @<" + jarName + ">");

        Thread.currentThread().setContextClassLoader(classLoader);

        Class<?> interfaceClazz = classLoader.loadClass(serviceName);
        if (interfaceClazz == null)
            throw new WriteOperationException(WriteOperationException.INTERFACE_NOT_EXIST, "interface does not exist, @<" + serviceName + ">");

        int code = providerSystem.checkStoreStubValid(serviceName, version);
        if (code == -1) {
            throw new WriteOperationException(WriteOperationException.STUB_HAS_EXISTED, "interface does not exist, @<" + serviceName + ">");
        } else if (code == -2) {
            throw new WriteOperationException(WriteOperationException.URLCLASSLOADER_HAS_EXISTED, "urlClassloader does not exist, @<" + jarName + ">");
        }

        // 执行到这，所有的检查工作都已经完成
        Object stubObj;
        try {
            stubObj = pullServiceDriver.buildStub(interfaceClazz, serviceName, version, providerSystem.getProviderSystemConfig());
        } catch (Exception e) {
            throw new WriteOperationException(WriteOperationException.INVOKE_BUILD_STUB, "invoke BUILD_STUB error, exception is thrown, @<" + spsid + ", " + jarName + ", " + serviceName + ", " + version + ">");
        }
        if (stubObj == null)
            throw new WriteOperationException(WriteOperationException.INVOKE_BUILD_STUB, "invoke BUILD_STUB error, stubObj is null @<" + spsid + ", " + jarName + ", " + serviceName + ", " + version + ">");

        providerSystem.storeStub(serviceName, version, stubObj, interfaceClazz);
    }

    @Override
    public void buildSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception {
        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
        if (providerSystem == null)
            throw new WriteOperationException(WriteOperationException.PS_NOT_EXIST, "provider system does not exist, @<" + spsid + ">");

        ConsumerSystem consumerSystem = ConsumerSystemFactory.getConsumerSystem(scsid);
        if (consumerSystem == null)
            throw new WriteOperationException(WriteOperationException.CS_NOT_EXIST, "consumer system does not exist, @<" + scsid + ">");

        URLClassLoader classLoader = providerSystem.getUrlClassLoader(serviceName, version);
        if (classLoader == null)
            throw new WriteOperationException(WriteOperationException.URLCLASSLOADER_NOT_EXIST, "urlClassloader does not exist, @<" + serviceName + ", " + version + ">");

        Object stubObj = providerSystem.getStubObj(serviceName, version);
        if (stubObj == null)
            throw new WriteOperationException(WriteOperationException.STUB_NOT_EXIST, "stub does not exist, @<" + spsid + ", " + serviceName + ", " + version + ">");

        Thread.currentThread().setContextClassLoader(classLoader);

        Class interfaceClazz = providerSystem.getInterfaceClazz(serviceName, version);
        if (interfaceClazz == null)
            throw new WriteOperationException(WriteOperationException.INTERFACE_NOT_EXIST, "interface does not exist, @<" + serviceName + ">");

        ExposeServiceDriver exposeServiceDriver = consumerSystem.getExposeServiceDriver();
        if (exposeServiceDriver == null)
            throw new WriteOperationException(WriteOperationException.ESD_NOT_EXIST, "PSD does not exist, @<" + consumerSystem.getConsumerSystemConfig().getExposeServiceDriverId() + ">");

        if (!consumerSystem.checkStoreSkeletonValid(spsid, serviceName, version)) {
            throw new WriteOperationException(WriteOperationException.SKELETON_HAS_EXISTED, "skeleton does not exist, @<" + consumerSystem.getConsumerSystemConfig().getExposeServiceDriverId() + ">");
        }

        // 执行到这，所有的检查工作都已经完成
        Object skeletonObj;
        try {
            skeletonObj = exposeServiceDriver.buildSkeleton(interfaceClazz, stubObj, serviceName, version, consumerSystem.getConsumerSystemConfig(), providerSystem.getProviderSystemConfig());
        } catch (Exception e) {
            throw new WriteOperationException(WriteOperationException.INVOKE_BUILD_SKELETON, "invoke BUILD_SKELETON error, @<" + scsid + ", " + spsid + ", " + serviceName + ", " + version + ">");
        }
        if (skeletonObj == null)
            throw new WriteOperationException(WriteOperationException.INVOKE_BUILD_SKELETON, "invoke BUILD_SKELETON error, @<" + scsid + ", " + spsid + ", " + serviceName + ", " + version + ">");

        // 将骨架记录在ConsumerSystem对象中
        consumerSystem.recordSkeletonToConsumerSystem(spsid, serviceName, version, skeletonObj);
        // 将骨架链接到对应的桩
        providerSystem.linkConsumerSystemWithStub(scsid, serviceName, version);

    }

    @Override
    public void removeSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception {
        ConsumerSystem consumerSystem = ConsumerSystemFactory.getConsumerSystem(scsid);
        if (consumerSystem == null)
            throw new WriteOperationException(WriteOperationException.CS_NOT_EXIST, "consumer system does not exist, @<" + scsid + ">");

        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
        if (providerSystem == null)
            throw new WriteOperationException(WriteOperationException.PS_NOT_EXIST, "provider system does not exist, @<" + spsid + ">");

        URLClassLoader classLoader = providerSystem.getUrlClassLoader(serviceName, version);
        if (classLoader == null)
            throw new WriteOperationException(WriteOperationException.URLCLASSLOADER_NOT_EXIST, "urlClassloader does not exist, @<" + serviceName + ", " + version + ">");

        Thread.currentThread().setContextClassLoader(classLoader);

        Class interfaceClazz = providerSystem.getInterfaceClazz(serviceName, version);
        if (interfaceClazz == null)
            throw new WriteOperationException(WriteOperationException.INTERFACE_NOT_EXIST, "interface does not exist, @<" + serviceName + ">");

        ExposeServiceDriver exposeServiceDriver = consumerSystem.getExposeServiceDriver();
        if (exposeServiceDriver == null)
            throw new WriteOperationException(WriteOperationException.ESD_NOT_EXIST, "PSD does not exist, @<" + consumerSystem.getConsumerSystemConfig().getExposeServiceDriverId() + ">");

        Object skeletonObj = consumerSystem.getSkeletonObj(spsid, serviceName, version);
        if (skeletonObj == null)
            throw new WriteOperationException(WriteOperationException.SKELETON_NOT_EXIST, "skeleton does not exist, @<" + scsid + ", " + spsid + ", " + serviceName + ", " + version + ">");

        // 执行到这，所有的检查工作都已经完成
        try {
            exposeServiceDriver.removeSkeleton(interfaceClazz, skeletonObj, serviceName, version, consumerSystem.getConsumerSystemConfig());
        } catch (Exception e) {
            throw new WriteOperationException(WriteOperationException.INVOKE_REMOVE_SKELETON, "invoke REMOVE_SKELETON error, @<" + scsid + ", " + spsid + ", " + serviceName + ", " + version + ">");
        }

        // 解除骨架和桩的关联
        providerSystem.unlinkConsumerSystemWithStub(scsid, serviceName, version);
        // 将骨架连接到对应的桩
        consumerSystem.removeSkeletonFromConsumerSystem(spsid, serviceName, version);

    }

    @Override
    public void removeStub(String spsid, String jarName, String serviceName, String version, boolean removeLinked) throws Exception {
        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
        if (providerSystem == null)
            throw new WriteOperationException(WriteOperationException.PS_NOT_EXIST, "provider system does not exist, @<" + spsid + ">");

        Stub stub = providerSystem.getStub(serviceName, version);
        if (stub == null)
            throw new WriteOperationException(WriteOperationException.STUB_NOT_EXIST, "stub does not exist, @<" + spsid + ", " + serviceName + ", " + version + ">");

        if (removeLinked)
            removeSkeletonsInStub(stub, spsid, serviceName, version);

        Class interfaceClazz = providerSystem.getInterfaceClazz(serviceName, version);
        if (interfaceClazz == null)
            throw new WriteOperationException(WriteOperationException.INTERFACE_NOT_EXIST, "interface does not exist, @<" + serviceName + ">");

        PullServiceDriver pullServiceDriver = providerSystem.getPullServiceDriver();
        if (pullServiceDriver == null)
            throw new WriteOperationException(WriteOperationException.PSD_NOT_EXIST, "PSD does not exist, @<" + providerSystem.getProviderSystemConfig().getPullServiceDriverId() + ">");

        // 执行到这，所有的检查工作都已经完成
        try {
            pullServiceDriver.removeStub(interfaceClazz, serviceName, version, providerSystem.getProviderSystemConfig());
        } catch (Exception e) {
            throw new WriteOperationException(WriteOperationException.INVOKE_REMOVE_STUB, "invoke REMOVE_STUB error, @<" + spsid + ", " + jarName + ", " + serviceName + ", " + version + ">");
        }

        providerSystem.getStubBuffer().remove(new ServiceKey(serviceName, version));

        // 从URLClassLoaderWrap对象中移除桩
        providerSystem.removeServiceKeyFromURLClassLoaderWrap(serviceName, version);
        // 从ProviderSystem对象中移除桩
        providerSystem.removeStub(serviceName, version);

    }

    @Override
    public void releaseJar(String spsid, String jarName, boolean removeLinked) throws Exception {
        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);
        if (providerSystem == null)
            throw new WriteOperationException(WriteOperationException.PS_NOT_EXIST, "provider system does not exist, @<" + spsid + ">");

        URLClassLoaderWrap urlClassLoaderWrap = providerSystem.getUrlClassLoaderWrap(jarName);
        if (urlClassLoaderWrap == null)
            throw new WriteOperationException(WriteOperationException.URLCLASSLOADER_HAS_EXISTED, "URLClassLoaderWrap does not exist, @<" + jarName + ">");

        if (removeLinked)
            removeStubsInUrlClassLoaderWrap(urlClassLoaderWrap, spsid, jarName);

        URLClassLoader urlClassLoader = urlClassLoaderWrap.getUrlClassLoader();

        if (urlClassLoader == null)
            throw new WriteOperationException(WriteOperationException.URLCLASSLOADER_HAS_EXISTED, "urlClassloader does not exist, @<" + jarName + ">");

        // 执行到这，所有的检查工作都已经完成
        // 从系统中卸载URLClassLoader
        try {
            ClassLoaderUtil.releaseLoader(urlClassLoaderWrap.getUrlClassLoader());
        } catch (Exception e) {
            throw new WriteOperationException(WriteOperationException.INVOKE_RELEASE_JAR, "invoke RELEASE_JAR error, @<" + spsid + ", " + jarName + ">");
        }

        // 移除URLClassLoaderWrap
        providerSystem.getUrlClassLoaderWrapBuffer().remove(jarName);
    }

    private void removeStubsInUrlClassLoaderWrap(URLClassLoaderWrap urlClassLoaderWrap, String spsid, String jarName) throws Exception {
        List<ServiceKey> serviceKeys = urlClassLoaderWrap.getServiceKeys();
        Iterator<ServiceKey> iterator = serviceKeys.iterator();
        while (iterator.hasNext()) {
            ServiceKey next = iterator.next();
            removeStub(spsid, jarName, next.getServiceName(), next.getVersion(), true);
        }
    }

    private void removeSkeletonsInStub(Stub stub, String spsid, String serviceName, String version) throws Exception {
        List<String> scsids = stub.getScsids();
        Iterator<String> iterator = scsids.iterator();
        while (iterator.hasNext()) {
            String scsid = iterator.next();
            removeSkeleton(scsid, spsid, serviceName, version);
        }
    }
}
