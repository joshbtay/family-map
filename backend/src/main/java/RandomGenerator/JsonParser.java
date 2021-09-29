package RandomGenerator;
import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class JsonParser {
    private Name maleNames;
    private Name femaleNames;
    private Name surnames;
    private LocationArray locations;
    private static final String filePath = "json/";

    /**
     * Constructor that fills arrays with names and location data
     */
    public JsonParser(){
        createNames();
        createLocations();
    }

    /**
     * Fills maleNames, femaleNames, and surnames arrays with data
     */
    public void createNames(){
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader( filePath + "mnames.json");
            maleNames = gson.fromJson(reader, Name.class);
            reader = new FileReader(filePath + "fnames.json");
            femaleNames = gson.fromJson(reader, Name.class);
            reader = new FileReader(filePath + "snames.json");
            surnames = gson.fromJson(reader, Name.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills Locations array with data
     */
    public void createLocations(){
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader( filePath + "locations.json");
            locations = gson.fromJson(reader, LocationArray.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets a male name from the male names array
     * @return a random male name
     */

    public String getMaleName(){
        Random random = new Random();
        int index = random.nextInt(maleNames.size());
        return maleNames.getNames()[index];
    }

    /**
     * Gets a female name from the female names array
     * @return a random female name
     */
    public String getFemaleName(){
        Random random = new Random();
        int index = random.nextInt(femaleNames.size());
        return femaleNames.getNames()[index];
    }

    /**
     * Gets a surname from the surnames array
     * @return a random surname
     */
    public String getSurname(){
        Random random = new Random();
        int index = random.nextInt(surnames.size());
        return surnames.getNames()[index];
    }

    /**
     * Gets a location from the Locations array
     * @return random Location object
     */
    public Location getLocation() {
        Random random = new Random();
        int index = random.nextInt(locations.size());
        return locations.getLocation(index);
    }
}
