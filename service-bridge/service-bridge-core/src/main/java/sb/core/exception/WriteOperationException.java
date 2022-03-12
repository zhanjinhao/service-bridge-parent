package sb.core.exception;

public class WriteOperationException extends RuntimeException {

    private int code;

    public WriteOperationException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static final int PS_NOT_EXIST = 1;
    public static final int CS_NOT_EXIST = 2;
    public static final int JAR_FILE_NOT_EXIST = 3;
    public static final int URLCLASSLOADER_HAS_EXISTED = 4;
    public static final int PSD_NOT_EXIST = 5;
    public static final int ESD_NOT_EXIST = 6;
    public static final int URLCLASSLOADER_NOT_EXIST = 7;
    public static final int INTERFACE_NOT_EXIST = 8;
    public static final int STUB_NOT_EXIST = 9;
    public static final int SKELETON_NOT_EXIST = 10;
    public static final int STUB_HAS_EXISTED = 10;
    public static final int SKELETON_HAS_EXISTED = 10;

    public static final int INVOKE_LOAD_JAR = 10;
    public static final int INVOKE_BUILD_STUB = 10;
    public static final int INVOKE_BUILD_SKELETON = 10;
    public static final int INVOKE_REMOVE_STUB = 10;
    public static final int INVOKE_REMOVE_SKELETON = 10;
    public static final int INVOKE_RELEASE_JAR = 10;
    public static final int INSTRUCTION_ERROR = 10;

}
