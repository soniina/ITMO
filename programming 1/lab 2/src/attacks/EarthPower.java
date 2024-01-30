package attacks;
import ru.ifmo.se.pokemon.*;

public class EarthPower extends SpecialMove{
    public EarthPower() {
        super(Type.GROUND, 90, 100);
    }
    protected String describe() {
        return "применяет EarthPower";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.SPECIAL_DEFENSE, -1);
    }
}
