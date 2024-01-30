package attacks;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 100);
    }

    protected String describe() {
        return "применяет Facade";
    }

    @Override
    protected double calcBaseDamage(Pokemon attack, Pokemon defend) {
        double damage = super.calcBaseDamage(attack, defend);
        Status pokemon_cond = attack.getCondition();
        if (pokemon_cond == Status.BURN || pokemon_cond == Status.POISON || pokemon_cond == Status.PARALYZE) {
            damage *= 2;
        }
        return damage;
    }
}