package sb.core.operation.write;

import sb.core.exception.WriteOperationException;
import sb.core.operation.write.serialize.InstructionsOneTransaction;

import java.util.Iterator;
import java.util.List;

public class SnapShotWriteOperation extends AbstractTransactionBasicWriteOperation implements SnapShotWriteOperationInterface {

    private static SnapShotWriteOperationInterface snapShotWriteOperationInterface;

    @Override
    public void executeSnapshot(InstructionsOneTransaction instructionsOneTransaction) {
        SingleInstructionOperation singleInstructionOperation = SingleInstructionOperation.getSingleInstructionOperation();
        List<String> instructionsList = instructionsOneTransaction.getInstructionsList();
        Iterator<String> iterator = instructionsList.iterator();
        begin();
        try {

            while (iterator.hasNext()) {
                String next = iterator.next();
                singleInstructionOperation.executeInstruction(next);
            }

        } catch (WriteOperationException e) {

        }


    }
}
