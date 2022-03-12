package sb.core.server;

import java.util.Objects;

public class Result {

    private String message;
    private int statusCode;
    private Object invokeResult;

    private Result() {   }

    public Result(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Result(int statusCode) {
        this.statusCode = statusCode;
    }

    public Result(int statusCode, String message, Object invokeResult) {
        this.statusCode = statusCode;
        this.message = message;
        this.invokeResult = invokeResult;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private static Result success = new Result(200, "success");
    private static Result exception = new Result(400, "exception");
    private static Result error = new Result(500, "error");
    private static Result asyncSuccess = new Result(600, "asynchronous operation success, please check your log for more information");
    private static Result nullResult = new Result();

    public static Result getSuccess() {
        return success;
    }

    public static Result getException() {
        return exception;
    }

    public static Result getError() {
        return error;
    }

    public static Result getAsyncSuccess() {
        return asyncSuccess;
    }

    public static Result getSuccess(Object invokeResult) {
        return new Result(200, "success", invokeResult);
    }

    public static Result getNullResult() {
        return nullResult;
    }

    public Object getInvokeResult() {
        return invokeResult;
    }

    @Override
    public String toString() {
        return "Result{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                ", invokeResult=" + invokeResult +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return statusCode == result.statusCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode);
    }
}