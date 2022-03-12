package sb.core.operation.write.serialize;

import sb.core.init.config.ApplicationConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSerializeTextFileOperation extends AbstractReverseInstructionSerialize {

    private static String instPath = ApplicationConfig.INSTRUCTION_PATH;

    private volatile static BufferedReader bufferedReader;
    private volatile static BufferedWriter bufferedWriter;

    protected BufferedWriter getBufferedWriter(boolean append) {
        if (bufferedWriter == null) {
            synchronized (AbstractSerializeTextFileOperation.class) {
                if (bufferedWriter == null) {
                    try {
                        bufferedWriter = new BufferedWriter(new FileWriter(instPath, append));
                    } catch (IOException e) {
                        bufferedWriter = null;
                        e.printStackTrace();
                    }
                }
            }
        }
        return bufferedWriter;
    }


    protected List<String> clearInstFile() {
        List<String> instructions = new ArrayList<>();
        BufferedReader bufferedReader = getBufferedReader();
        String ins;
        try {
            while ((ins = bufferedReader.readLine()) != null) {
                instructions.add(ins);
            }
        } catch (Exception e) {

        } finally {
            closeStream(bufferedReader);
        }

        BufferedWriter bufferedWriter = getBufferedWriter(false);
        try {
            bufferedWriter.write("");
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufferedWriter);
        }
        return instructions;
    }

    protected BufferedReader getBufferedReader() {
        if (bufferedReader == null) {
            synchronized (AbstractSerializeTextFileOperation.class) {
                if (bufferedReader == null) {
                    try {
                        bufferedReader = new BufferedReader(new FileReader(instPath));
                    } catch (IOException e) {
                        bufferedReader = null;
                        e.printStackTrace();
                    }
                }
            }
        }
        return bufferedReader;
    }

    protected void closeStream(BufferedReader bufferedReader) {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeStream(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void flushStream(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected List<String> compressInstructions(List<String> insts) {
        int size = insts.size();

        if (size < 2)
            return insts;

        List<String[]> splits = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String inst = insts.get(i);
            if (inst == null || inst.equals(""))
                continue;
            splits.add(insts.get(i).split(Instruction.getSeparator()));
        }

        List<Integer> recordRemove = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String[] split = splits.get(i);
            String key = split[0];
            int type = Integer.parseInt(split[1]);

            for (int j = i + 1; j < size; j++) {
                if (recordRemove.indexOf(j) != -1) {
                    continue;
                }
                String[] splitCompare = splits.get(j);
                String keyCompare = splitCompare[0];
                int typeCompare = Integer.parseInt(splitCompare[1]);
                if (keyCompare.equals(key) && (type + typeCompare == 0)) {
                    recordRemove.add(j);
                }
            }
        }

        Iterator<Integer> iterator = recordRemove.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            insts.remove(next);
        }
        return insts;
    }


}
