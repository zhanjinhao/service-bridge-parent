package sb.core.operation.write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sb.core.exception.WriteOperationException;
import sb.core.operation.write.serialize.Instruction;

/**
 * 这个类是单例，因为不涉及到存储指令
 */
public class SingleInstructionOperation extends AbstractBasicWriteOperationImpl {

    private final static Logger LOGGER = LoggerFactory.getLogger(SingleInstructionOperation.class);
    private static SingleInstructionOperation singleInstructionOperation = new SingleInstructionOperation();

    private SingleInstructionOperation() {

    }

    public static SingleInstructionOperation getSingleInstructionOperation() {
        return singleInstructionOperation;
    }

    public void removeStub(String spsid, String jarName, String serviceName, String version) throws Exception {
        super.removeStub(spsid, jarName, serviceName, version, false);
    }

    public void releaseJar(String spsid, String jarName) throws Exception {
        super.releaseJar(spsid, jarName, false);
    }

    public void executeInstruction(Instruction instruction) {
        if (instruction == null) {
            throw new WriteOperationException(WriteOperationException.INSTRUCTION_ERROR, "instruction error");
        }
        int type = instruction.getType();
        try {
            switch (type) {
                case Instruction.LOAD_JAR:
                    loadJar(instruction.getPhase1(), instruction.getPhase2());
                case Instruction.BUILD_STUB:
                    buildStub(instruction.getPhase1(), instruction.getPhase2(), instruction.getPhase3(), instruction.getPhase4());
                case Instruction.BUILD_SKELETON:
                    buildSkeleton(instruction.getPhase1(), instruction.getPhase2(), instruction.getPhase3(), instruction.getPhase4());
                case Instruction.REMOVE_SKELETON:
                    removeSkeleton(instruction.getPhase1(), instruction.getPhase2(), instruction.getPhase3(), instruction.getPhase4());
                case Instruction.REMOVE_STUB:
                    removeStub(instruction.getPhase1(), instruction.getPhase2(), instruction.getPhase3(), instruction.getPhase4());
                case Instruction.RELEASE_JAR:
                    releaseJar(instruction.getPhase1(), instruction.getPhase2());
                case Instruction.ATOMIC_WRITE_SEPARATOR:
                    return;
                default:
                    throw new WriteOperationException(WriteOperationException.INSTRUCTION_ERROR, "instruction error");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    public void executeInstruction(String instStr) {
        Instruction instruction = Instruction.toInstruction(instStr);
        executeInstruction(instruction);
    }


}
