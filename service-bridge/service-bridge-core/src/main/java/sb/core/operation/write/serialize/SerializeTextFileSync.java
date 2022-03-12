package sb.core.operation.write.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 同步的序列化到文件中
 */
public class SerializeTextFileSync extends AbstractSerializeTextFileOperation {

    private final static Logger LOGGER = LoggerFactory.getLogger(SerializeTextFileSync.class);

    @Override
    public void serializeInstruction(Instruction instruction) {
        BufferedWriter bufferedWriter = getBufferedWriter(true);
        try {
            bufferedWriter.write(instruction.toString() + "\n");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            flushStream(bufferedWriter);
        }
    }

    @Override
    public void serializeInstructionsOneTransaction(InstructionsOneTransaction instructionsOneTransaction) {
        BufferedWriter bufferedWriter = getBufferedWriter(false);
        List<String> instructionsList = instructionsOneTransaction.getInstructionsList();
        Iterator<String> iterator = instructionsList.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            try {
                bufferedWriter.write(next);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                flushStream(bufferedWriter);
            }
        }
    }

    @Override
    public void deSerializeInstruction(Instruction instruction) {
        List<String> list = clearInstFile();
        String instStr = instruction.toString();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.equals(instStr)) {
                iterator.remove();
                executeInstructionReverse(instruction);
                break;
            }
        }
        list = compressInstructions(list);
        InstructionsOneTransaction instructionsOneTransaction = new InstructionsOneTransaction(list);
        serializeInstructionsOneTransaction(instructionsOneTransaction);
    }

    @Override
    public void deSerializeInstructionsOneTransaction(InstructionsOneTransaction instructionsOneTransaction) {

    }
}
