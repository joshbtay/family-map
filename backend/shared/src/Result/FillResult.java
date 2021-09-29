package Result;

public class FillResult extends Result{
    public FillResult(boolean success) {
        super(success);
    }

    public FillResult(boolean success, String error) {
        super(success, error);
    }
}
