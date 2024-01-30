package items;
import abstracts.Item;
public class Rope extends Item {
    public Rope() {
        setName("Верёвка");
    }
    public String first_end() {
        return "один конец верёвки";
    }
    public String other_end() {
        return "другой конец верёвки";
    }

    @Override
    public String mean() {
        return "";
    }
}
