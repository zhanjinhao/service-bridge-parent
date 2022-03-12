package sb.core.operation.write;

import sb.core.exception.WriteOperationException;
import sb.core.init.snapshot.SnapShotInit;
import sb.core.operation.write.serialize.SerializeInterface;

public class BasicWriteOperation extends AbstractTransactionBasicWriteOperation {

    protected BasicWriteOperation(SerializeInterface serializeInterface) {
        super(serializeInterface);
    }

    public BasicWriteOperation() {
        super(SnapShotInit.serializeInterface);
    }

    @Override
    public void loadJar(String spsid, String jarName) throws Exception {
        try {
            super.begin();
            super.loadJar(spsid, jarName);
            super.commit();
        } catch (WriteOperationException e) {
            super.rollBack();
        }
    }

    @Override
    public void buildStub(String spsid, String jarName, String serviceName, String version) throws Exception {
        try {
            super.begin();
            super.buildStub(spsid, jarName, serviceName, version);
            super.commit();
        } catch (WriteOperationException e) {
            super.rollBack();
        }
    }

    @Override
    public void buildSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception {
        try {
            super.begin();
            super.buildSkeleton(scsid, spsid, serviceName, version);
            super.commit();
        } catch (WriteOperationException e) {
            super.rollBack();
        }
    }

    @Override
    public void removeSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception {
        try {
            super.begin();
            super.removeSkeleton(scsid, spsid, serviceName, version);
            super.commit();
        } catch (WriteOperationException e) {
            super.rollBack();
        }
    }


    @Override
    public void removeStub(String spsid, String jarName, String serviceName, String version, boolean removeLinked) throws Exception {
        try {
            super.begin();
            super.removeStub(spsid, jarName, serviceName, version, removeLinked);
            super.commit();
        } catch (WriteOperationException e) {
            super.rollBack();
        }
    }


    @Override
    public void releaseJar(String spsid, String jarName, boolean removeLinked) throws Exception {
        try {
            super.begin();
            super.releaseJar(spsid, jarName, removeLinked);
            super.commit();
        } catch (WriteOperationException e) {
            super.rollBack();
        }
    }

}
