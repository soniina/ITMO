package attacks;

import ru.ifmo.se.pokemon.*;

public class Swagger extends StatusMove {
    public Swagger() {
        super(Type.NORMAL, 0, 85);
    }

    protected String describe() {
        return "применяет Swagger";
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect.confuse(pokemon);
        pokemon.setMod(Stat.ATTACK, 2);
    }
}
