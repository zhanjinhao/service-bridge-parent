package sb.core.operation.write;

import sb.core.operation.write.serialize.Instruction;
import sb.core.operation.write.serialize.SerializeInterface;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTransactionBasicWriteOperation extends AbstractBufferedBasicWriteOperation implements TransactionInterface {

    private SerializeInterface serializeInterface;

    public AbstractTransactionBasicWriteOperation(SerializeInterface serializeInterface) {
        this.serializeInterface = serializeInterface;
    }

    public AbstractTransactionBasicWriteOperation() {
    }

    @Override
    public void begin() {
        List<Instruction> instructionsExecute = getInstructionsExecute();
        instructionsExecute.add(Instruction.getAtomicWriteSeparator());
    }

    @Override
    public void commit() {
        List<Instruction> instructionsDone = getInstructionsExecute();
        List<Instruction> instructionsCommit = getInstructionsCommit();
        Iterator<Instruction> iterator = instructionsDone.iterator();
        while (iterator.hasNext()) {
            Instruction next = iterator.next();
            serializeInterface.serializeInstruction(next);
            instructionsCommit.add(next);
            iterator.remove();
        }
    }

    @Override
    public void rollBack() {
        List<Instruction> instructionsCommit = getInstructionsCommit();
        Collections.reverse(instructionsCommit);
        Iterator<Instruction> iterator = instructionsCommit.iterator();
        while (iterator.hasNext()) {
            Instruction next = iterator.next();
            serializeInterface.deSerializeInstruction(next);
        }
    }

}
