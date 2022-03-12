package sb.core.operation.write;

import sb.core.operation.write.serialize.Instruction;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractBufferedBasicWriteOperation extends AbstractBasicWriteOperationImpl {

    private List<Instruction> instructionsExecute = new LinkedList<>();
    private List<Instruction> instructionsCommit = new LinkedList<>();

    protected List<Instruction> getInstructionsExecute() {
        return instructionsExecute;
    }

    protected List<Instruction> getInstructionsCommit() {
        return instructionsCommit;
    }

    @Override
    public void loadJar(String spsid, String jarName) throws Exception {
        super.loadJar(spsid, jarName);
        instructionsExecute.add(Instruction.getLOAD_JAR(spsid, jarName));
    }

    @Override
    public void buildStub(String spsid, String jarName, String serviceName, String version) throws Exception {
        super.buildStub(spsid, jarName, serviceName, version);
        instructionsExecute.add(Instruction.getBUILD_STUB(spsid, jarName, serviceName, version));
    }

    @Override
    public void buildSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception {
        super.buildSkeleton(scsid, spsid, serviceName, version);
        instructionsExecute.add(Instruction.getBUILD_SKELETON(scsid, spsid, serviceName, version));
    }

    @Override
    public void removeSkeleton(String scsid, String spsid, String serviceName, String version) throws Exception {
        super.removeSkeleton(scsid, spsid, serviceName, version);
        instructionsExecute.add(Instruction.getREMOVE_SKELETON(scsid, spsid, serviceName, version));
    }

    @Override
    public void removeStub(String spsid, String jarName, String serviceName, String version, boolean removeLinked) throws Exception {
        super.removeStub(spsid, jarName, serviceName, version, removeLinked);
        instructionsExecute.add(Instruction.getREMOVE_STUB(spsid, jarName, serviceName, version));
    }

    @Override
    public void releaseJar(String spsid, String jarName, boolean removeLinked) throws Exception {
        super.releaseJar(spsid, jarName, removeLinked);
        instructionsExecute.add(Instruction.getRELEASE_JAR(spsid, jarName));
    }

}