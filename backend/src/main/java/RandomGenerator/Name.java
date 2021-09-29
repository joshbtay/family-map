package RandomGenerator;

public class Name {
    String[] data;

    public Name(String[] data) {
        this.data = data;
    }

    public String[] getNames() {
        return data;
    }

    public void setName(String[] data) {
        this.data = data;
    }

    public int size(){
        return data.length;
    }

    @Override
    public String toString(){
        if (data == null){
            return "";
        }
        StringBuilder edit = new StringBuilder();
        for (String s : data){
            edit.append(s + "\n");
        }
        return edit.toString();
    }
}
