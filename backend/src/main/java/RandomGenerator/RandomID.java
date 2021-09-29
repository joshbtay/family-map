package RandomGenerator;

import java.util.UUID;

public class RandomID {
    /**
     * Simply generates a random UUID which can serve as a personID, eventID, or any other ID.
     * @return random UUID
     */
    public String getRandomID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
