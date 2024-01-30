package creatures;
import abstracts.Creature;

public class Twistum extends Creature {
    private static Twistum twistum;
    private Twistum() {
        setName("Шпунтик");
    }

    public static Twistum getInstance() {
        if (twistum == null) {
            twistum = new Twistum();
        }
        return twistum;
    }
}
