package sb.core.exception;

public class InstructionException extends RuntimeException {
    private int code;

    public InstructionException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
