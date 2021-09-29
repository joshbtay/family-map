package RandomGenerator;

public class Location {
    private float latitude;
    private float longitude;
    private String country;
    private String city;

    public Location(float latitude, float longitude, String country, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString(){
        return "Latitude " + latitude + ", Longitude " + longitude + ", country: " + country +
                ", city: " + city;
    }
}
