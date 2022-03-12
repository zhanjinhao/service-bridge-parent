package sb.core.operation.write.serialize;

import java.util.List;

public class InstructionsOneTransaction {

    private List<String> instructionsList;

    public InstructionsOneTransaction(List<String> instructionsList) {
        this.instructionsList = instructionsList;
    }

    public List<String> getInstructionsList() {
        return instructionsList;
    }
}
