package sb.core.operation.write;

public interface BasicWriteOperationInterface {

    void loadJar(String spsid, String jarName) throws Exception;

    void buildStub(String spsid, String jarName, String serviceName, String version) throws Exception;

    void buildSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception;

    void removeSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception;

    void removeStub(String spsid, String jarName, String serviceName, String version, boolean removeLinked) throws Exception;

    void releaseJar(String spsid, String jarName, boolean removeLinked) throws Exception;

}
