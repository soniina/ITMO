package attacks;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class SandAttack extends StatusMove {
    public SandAttack() {
        super(Type.GROUND, 0, 100);
    }

    protected String describe() {
        return "применяет Sand Attack";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.ACCURACY, -1);
    }
}
