package attacks;
import ru.ifmo.se.pokemon.*;

public class ThunderWave extends StatusMove{
    public ThunderWave() {
        super(Type.ELECTRIC, 0, 90);
    }

    protected String describe() {
        return "применяет Thunder Wave";
    }
    protected void applyOppEffects(Pokemon pokemon) {
        Effect.paralyze(pokemon);
    }
}
