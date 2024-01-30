package creatures;
import abstracts.Creature;

public class Bendum extends Creature {
    private static Bendum bendum;
    private Bendum() {
        setName("Винтик");
    }

    public static Bendum getInstance() {
        if (bendum == null) {
            bendum = new Bendum();
        }
        return bendum;
    }

    public String notRespond() {
        return toString() + " не отзывался";
    }
    public String notBe() {
        return toString() + " не был";
    }
}
