package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;
public class Wimpod extends Pokemon {
    public Wimpod(java.lang.String name, int level) {
        super(name, level);
        setStats(25, 35, 40, 20, 30,  80);
        setType(Type.BUG, Type.WATER);
        setMove(new SandAttack(), new Facade(), new Swagger());
    }
}
