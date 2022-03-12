package sb.core.operation.write.serialize;

import sb.core.exception.InstructionException;

import java.util.Objects;

public class Instruction {

    private static final String separator = "`";

    public static String getSeparator() {
        return separator;
    }

    public static final int LOAD_JAR = 1;
    public static final int BUILD_STUB = 2;
    public static final int BUILD_SKELETON = 3;
    public static final int REMOVE_SKELETON = -3;
    public static final int REMOVE_STUB = -2;
    public static final int RELEASE_JAR = -1;
    public static final int ATOMIC_WRITE_SEPARATOR = 0;

    private String key;

    private int type;

    // LOAD_JAR、BUILD_STUB、REMOVE_STUB、RELEASE_JAR：表示 spsid
    // BUILD_SKELETON、REMOVE_SKELETON：表示 scsid
    private String phase1;

    // LOAD_JAR、BUILD_STUB、REMOVE_STUB、RELEASE_JAR：表示 jarName
    // BUILD_SKELETON、REMOVE_SKELETON：表示 spsid
    private String phase2;

    // phase3 任何时候都表示 serviceName。用于 BUILD_STUB、BUILD_SKELETON、REMOVE_SKELETON、REMOVE_STUB
    private String phase3;

    // phase4 任何时候都表示 version。用于 BUILD_STUB、BUILD_SKELETON、REMOVE_SKELETON、REMOVE_STUB
    private String phase4;


    private Instruction(int type, String phase1, String phase2, String phase3, String phase4) {
        this.type = type;
        this.phase1 = phase1;
        this.phase2 = phase2;
        this.phase3 = phase3;
        this.phase4 = phase4;
        key = key();
    }

    private Instruction(int type, String phase1, String phase2, String phase3, String phase4, String key) {
        this.type = type;
        this.phase1 = phase1;
        this.phase2 = phase2;
        this.phase3 = phase3;
        this.phase4 = phase4;
        this.key = key;
    }

    public static Instruction getLOAD_JAR(String spsid, String jarName) {
        return new Instruction(LOAD_JAR, spsid, jarName, null, null);
    }

    public static Instruction getBUILD_STUB(String spsid, String jarName, String serviceName, String version) {
        return new Instruction(BUILD_STUB, spsid, jarName, serviceName, version);
    }

    public static Instruction getBUILD_SKELETON(String scsid, String spsid, String serviceName, String version) {
        return new Instruction(BUILD_SKELETON, scsid, spsid, serviceName, version);
    }

    public static Instruction getREMOVE_SKELETON(String scsid, String spsid, String serviceName, String version) {
        return new Instruction(REMOVE_SKELETON, scsid, spsid, serviceName, version);
    }

    public static Instruction getREMOVE_STUB(String spsid, String jarName, String serviceName, String version) {
        return new Instruction(REMOVE_STUB, spsid, jarName, serviceName, version);
    }

    public static Instruction getRELEASE_JAR(String spsid, String jarName) {
        return new Instruction(RELEASE_JAR, spsid, jarName, null, null);
    }

    public static Instruction toInstruction(String instructionStr) {
        String[] split = instructionStr.split(separator);
        int length = split.length;
        if (length < 1 || length > 7)
            throw new InstructionException(-1, instructionStr);
        String typeStr = split[1];
        int type = Integer.parseInt(typeStr);
        switch (type) {
            case LOAD_JAR:
            case RELEASE_JAR:
                return new Instruction(type, split[2], split[3], null, null);
            case BUILD_STUB:
            case BUILD_SKELETON:
            case REMOVE_SKELETON:
            case REMOVE_STUB:
                return new Instruction(type, split[2], split[3], split[4], split[5]);
            case ATOMIC_WRITE_SEPARATOR:
                return new Instruction(type, split[2], null, null, null);
        }
        return null;
    }

    private String key() {
        switch (type) {
            case LOAD_JAR:
            case RELEASE_JAR:
                return String.valueOf(Objects.hash(phase1, phase2));
            case BUILD_STUB:
            case REMOVE_STUB:
                return String.valueOf(Objects.hash(phase1, phase3, phase4));
            case BUILD_SKELETON:
            case REMOVE_SKELETON:
                return String.valueOf(Objects.hash(phase1, phase2, phase3, phase4));
            case ATOMIC_WRITE_SEPARATOR:
                return String.valueOf(Objects.hash(phase1));
        }
        return null;
    }

    @Override
    public String toString() {
        if (type == LOAD_JAR || type == RELEASE_JAR)
            return key + separator + type + separator + phase1 + separator + phase2;
        return key + separator + type + separator + phase1 + separator + phase2 + separator + phase3 + separator + phase4;
    }

    public static Instruction getAtomicWriteSeparator() {
        long l = System.currentTimeMillis();
        return new Instruction(0, separator, null, null, null, String.valueOf(l));
    }

    public int getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getPhase1() {
        return phase1;
    }

    public String getPhase2() {
        return phase2;
    }

    public String getPhase3() {
        return phase3;
    }

    public String getPhase4() {
        return phase4;
    }

    public void setType(int type) {
        this.type = type;
    }
}