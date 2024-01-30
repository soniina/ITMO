package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;
public class Politoed extends Poliwhirl {
    public Politoed(java.lang.String name, int level) {
        super(name, level);
        setStats(90, 75, 75, 90, 100,  70);
        addMove(new Blizzard());
    }
}