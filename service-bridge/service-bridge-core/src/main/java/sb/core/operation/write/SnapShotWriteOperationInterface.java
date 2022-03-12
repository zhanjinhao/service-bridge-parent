package sb.core.operation.write;

import sb.core.operation.write.serialize.InstructionsOneTransaction;

public interface SnapShotWriteOperationInterface {

    void executeSnapshot(InstructionsOneTransaction instructionsOneTransaction);

}
