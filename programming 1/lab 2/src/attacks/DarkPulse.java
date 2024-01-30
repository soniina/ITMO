package attacks;
import ru.ifmo.se.pokemon.*;

public class DarkPulse extends SpecialMove{
    public DarkPulse() {
        super(Type.DARK, 80, 100);
    }
    protected String describe() {
        return "применяет DarkPulse";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (Math.random() <= 0.2) {
            Effect.flinch(pokemon);
        }
    }
}
