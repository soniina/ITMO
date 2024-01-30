package attacks;

import ru.ifmo.se.pokemon.*;

public class Blizzard extends SpecialMove {
    public Blizzard() {
        super(Type.ICE, 110, 70);
    }

    protected String describe() {
        return "применяет Blizzard";
    }

    protected void applyOppEffects(Pokemon pokemon) {
        if (Math.random() <= 0.1) {
            Effect.freeze(pokemon);
        }
    }
}
