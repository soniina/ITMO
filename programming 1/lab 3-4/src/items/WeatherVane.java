package items;
import abstracts.Item;
public class WeatherVane extends Item{
    public WeatherVane() {
        setName("Флюгер");
    }
    @Override
    public String mean() {
        return " показывает направление ветра";
    }
}
