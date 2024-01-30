package creatures;
import abstracts.Creature;

public class Shorty extends Creature {
    public Shorty() {
        setName("Коротышка");
    }

    public String watch(Creature creature) {
        return toString() + " следил за " + creature;
    }

    public String pull() {
        return toString() + " принялся тянуть ";
    }

    public String canPull() {
        return toString() + " может притянуть";
    }
}
