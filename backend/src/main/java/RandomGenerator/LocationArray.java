package RandomGenerator;

public class LocationArray {
    Location[] data;
    public Location[] getLocations(){
        return data;
    }
    public Location getLocation(int index){
        return data[index];
    }

    public void setLocations(Location[] locations) {
        this.data = locations;
    }

    public int size(){
        return data.length;
    }

    public LocationArray(Location[] locations) {
        this.data = locations;
    }

    @Override
    public String toString() {
        if (data == null){
            return "";
        }
        StringBuilder edit = new StringBuilder();
        for (Location l : data){
            edit.append(l.toString() + "\n");
        }
        return edit.toString();
    }

}
