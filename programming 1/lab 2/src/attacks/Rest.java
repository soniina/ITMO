package attacks;

import ru.ifmo.se.pokemon.*;

public class Rest extends StatusMove {
    public Rest() {
        super(Type.PSYCHIC, 0, 0);
    }

    protected String describe() {
        return "применяет Rest";
    }
    protected void applySelfEffects(Pokemon pokemon) {
        Effect.sleep(pokemon);
        pokemon.restore();
    }
}
