package items;
import abstracts.Creature;
import abstracts.Item;
public class Door extends Item {
    public Door() {
        setName("Дверь");
    }

    @Override
    public String mean() {
        return "";
    }

    public String open(Creature creature) {
        return creature + " отворил дверь";
    }

    public String close(Creature creature) {
        return creature + " закрыл дверь";
    }

}
