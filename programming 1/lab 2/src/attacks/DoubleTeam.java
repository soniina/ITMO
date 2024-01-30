package attacks;

import ru.ifmo.se.pokemon.*;

public class DoubleTeam extends StatusMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 0);
    }

    protected String describe() {
        return "применяет Double Team";
    }

    protected void applySelfEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.EVASION, 1);
    }
}
