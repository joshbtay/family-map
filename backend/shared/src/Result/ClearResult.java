package Result;

public class ClearResult extends Result{
    public ClearResult(boolean success, String error) {
        super(success, error);
    }

    public ClearResult(boolean success) {
        super(success);
    }
}
