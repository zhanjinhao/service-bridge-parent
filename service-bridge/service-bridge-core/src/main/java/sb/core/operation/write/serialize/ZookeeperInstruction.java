package sb.core.operation.write.serialize;

public class ZookeeperInstruction extends Instruction {

    public static final int SAVE_JAR = -4;
    public static final int DELETE_JAR = 4;

    protected ZookeeperInstruction(int type, String phase1, String phase2, String phase3, String phase4) {
        super(type, phase1, phase2, phase3, phase4);
    }



}
