package RandomGenerator;

import model.Event;
import model.Person;
import model.User;

import java.util.ArrayList;
import java.util.Random;

public class RandomGenerations {
    private static final int CURRENT_YEAR = 2021;
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 45 - MIN_AGE;
    private RandomID randomID = new RandomID();
    private ArrayList<Person> persons = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private String username = null;
    private static final String BIRTH = "Birth";
    private static final String MARRIAGE = "Marriage";
    private static final String DEATH = "Death";
    private JsonParser parser = new JsonParser();

    /**
     * packages person and event arrays into an object to return
     * @return AncestorData object
     */
    public AncestorData getData() {
        return new AncestorData(persons.toArray(new Person[persons.size()]), events.toArray(new Event[events.size()]));
    }

    /**
     * Given a desired number of generations and a User object, generates as many generations as you want
     * @param generationCount
     * @param user
     */
    public void generateAncestors(int generationCount, User user){
        persons = new ArrayList<>();
        events = new ArrayList<>();
        username = user.getUsername();
        String personID = user.getPersonID();
        String fatherID = "";
        String motherID = "";
        int birthYear = getRandomBirthYear(CURRENT_YEAR);
        if(generationCount > 0){
            fatherID = randomID.getRandomID();
            motherID = randomID.getRandomID();
        }
        Person root = new Person(personID, username, user.getFirstName(),
                user.getLastName(), user.getGender(), fatherID, motherID, "");
        persons.add(root);
        Location rootLocation = getRandomLocation();
        Event rootEvent = new Event(randomID.getRandomID(), user.getUsername(), personID,
                rootLocation.getLatitude(), rootLocation.getLongitude(),
                rootLocation.getCountry(),rootLocation.getCity(),BIRTH, birthYear);
        events.add(rootEvent);
        if(generationCount > 0){
            generateAncestorsHelper(generationCount - 1, user.getLastName(), motherID, fatherID, birthYear);
        }
    }

    /**
     * Helper method for generateAncestors.
     * @param generationCount
     * @param lastName
     * @param motherID
     * @param fatherID
     * @param birth
     */
    // I realize that this method is a bit long. I tried a couple times to split it up into smaller methods,
    //  but they ended up being less readable and more confusing. The problem is that most of the data constantly
    // needs to be referenced by other objects. While this is a bit long, it is well organized and is very readable
    private void generateAncestorsHelper(int generationCount, String lastName, String motherID, String fatherID, int birth){

        //create grandparent IDs
        String fatherFatherID = "";
        String fatherMotherID = "";
        String motherFatherID = "";
        String motherMotherID = "";

        //fill out grandparent IDs if necessary
        if(generationCount > 0){
            fatherFatherID = randomID.getRandomID();
            fatherMotherID = randomID.getRandomID();
            motherFatherID = randomID.getRandomID();
            motherMotherID = randomID.getRandomID();
        }

        //fill out event locations
        Location fatherBirthLocation = getRandomLocation();
        Location motherBirthLocation = getRandomLocation();
        Location marriageLocation = getRandomLocation();
        Location fatherDeathLocation = getRandomLocation();
        Location motherDeathLocation = getRandomLocation();

        //fill out event dates
        int motherBirth = getRandomBirthYear(birth);
        int fatherBirth = getRandomBirthYear(birth);
        int marriage = getRandomMarriageYear(Math.max(motherBirth, fatherBirth), birth);
        int motherDeath = getRandomDeathYear(marriage);
        int fatherDeath = getRandomDeathYear(marriage);

        String motherLastName = getRandomLastName();

        //create people
        Person father = new Person(fatherID, username, getRandomBoy(),
                lastName, "m", fatherFatherID, fatherMotherID, motherID);
        Person mother = new Person(motherID, username, getRandomGirl(),
                motherLastName, "f", motherFatherID, motherMotherID, fatherID);

        //fill out life events
        Event fatherBirthEvent = createEvent(fatherID, fatherBirthLocation, BIRTH, fatherBirth);
        Event fatherMarriageEvent = createEvent(fatherID, marriageLocation, MARRIAGE, marriage);
        Event fatherDeathEvent = createEvent(fatherID, fatherDeathLocation, DEATH, fatherDeath);
        Event motherBirthEvent = createEvent(motherID, motherBirthLocation, BIRTH, motherBirth);
        Event motherMarriageEvent = createEvent(motherID, marriageLocation, MARRIAGE, marriage);
        Event motherDeathEvent = createEvent(motherID, motherDeathLocation, DEATH, motherDeath);


        //Add generated data to corresponding arrays
        persons.add(father);
        persons.add(mother);

        events.add(fatherBirthEvent);
        events.add(motherBirthEvent);
        events.add(fatherMarriageEvent);
        events.add(motherMarriageEvent);
        events.add(fatherDeathEvent);
        events.add(motherDeathEvent);

        //if not base case (generationCount == 0), recurse
        if(generationCount > 0){
            //generate mom's side
            generateAncestorsHelper(generationCount - 1, motherLastName, motherMotherID, motherFatherID, motherBirth);
            //generate dad's side
            generateAncestorsHelper(generationCount - 1, lastName, fatherMotherID, fatherFatherID, fatherBirth);
        }

    }

    private Event createEvent(String personID, Location location, String type, int year){
        return new Event(randomID.getRandomID(), username, personID,
                location.getLatitude(), location.getLongitude(), location.getCountry(),
                location.getCity(), type, year);
    }

    private int getRandomBirthYear(int constraintYear){
        Random random = new Random();
        int currentAge = random.nextInt(MAX_AGE) + MIN_AGE;
        return constraintYear - currentAge;
    }

    private int getRandomMarriageYear(int birthYear, int childBirthYear){
        Random random = new Random();
        int leftoverYears = childBirthYear - birthYear - MIN_AGE;
        if (leftoverYears <= 0)
            leftoverYears = 1;
        leftoverYears = random.nextInt(leftoverYears);
        return childBirthYear - leftoverYears;
    }

    private int getRandomDeathYear(int marriageYear){
        Random random = new Random();
        return random.nextInt(MAX_AGE + MIN_AGE) + marriageYear;
    }

    private Location getRandomLocation(){
        return parser.getLocation();
    }

    private String getRandomBoy(){
        return parser.getMaleName();
    }

    private String getRandomGirl(){
        return parser.getFemaleName();
    }

    private String getRandomLastName(){
        return parser.getSurname();
    }



}
