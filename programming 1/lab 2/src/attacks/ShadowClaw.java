package attacks;

import ru.ifmo.se.pokemon.*;

public class ShadowClaw extends PhysicalMove {
    public ShadowClaw() {
        super(Type.GHOST, 70, 100);
    }

    protected String describe() {
        return "применяет ShadowClaw";
    }

    protected double calcCriticalHit(Pokemon attack, Pokemon defend) {
        if (Math.random() <= 0.125) {
            return 2;
        }
        return 1;
    }
}
