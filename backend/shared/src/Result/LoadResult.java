package Result;

public class LoadResult extends Result{


    public LoadResult(boolean success) {
        super(success);
    }

    public LoadResult(boolean success, String error) {
        super(success, error);
    }
}
