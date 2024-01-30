package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;
public class Golisopod extends Wimpod {
    public Golisopod(java.lang.String name, int level) {
        super(name, level);
        setStats(75, 125, 140, 60, 90,  40);
        addMove(new ShadowClaw());
    }
}