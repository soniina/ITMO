package items;
import abstracts.*;
public class Order extends Item {
    public Order() {
        setName("Приказ");
    }

    @Override
    public String mean() {
        return "";
    }

    public String execute() {
        return toString() + " исполнен";
    }

    public String execute(Creature creature) {
        return creature + " исполнил " + toString();
    }
}
