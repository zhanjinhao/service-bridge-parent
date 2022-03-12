package sb.core.operation.write.serialize;

import sb.core.operation.write.SingleInstructionOperation;

public abstract class AbstractReverseInstructionSerialize implements SerializeInterface {

    protected void executeInstructionReverse(Instruction instruction) {

        SingleInstructionOperation singleInstructionOperation = SingleInstructionOperation.getSingleInstructionOperation();

        instruction.setType(-instruction.getType());

        singleInstructionOperation.executeInstruction(instruction);

    }

}