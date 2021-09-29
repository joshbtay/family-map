package Result;

public class Result {
    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    private String message;
    private boolean success;

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String message) {
        this.message = message;
        this.success = success;
    }
}
