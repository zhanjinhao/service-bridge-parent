package sb.core.operation.write;

public interface CompoundWriteOperationInterface {

    void reBuildSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception;

    void reBuildStub(String spsid, String jarName, String serviceName, String version) throws Exception;

    void reLoadJar(String spsid, String jarName) throws Exception;

}
