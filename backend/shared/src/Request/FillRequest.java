package Request;

public class FillRequest {
    private String username;
    private int generations;
    private static final int DEFAULT_GENERATIONS = 4;

    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    public FillRequest(String username){
        this.username = username;
        this.generations = DEFAULT_GENERATIONS;
    }

    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }
}
