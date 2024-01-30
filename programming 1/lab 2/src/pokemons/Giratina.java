package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;
public class Giratina  extends Pokemon {
    public Giratina(java.lang.String name, int level) {
        super(name, level);
        setStats(150, 100, 120, 100, 120,  90);
        setType(Type.GHOST, Type.DRAGON);
        setMove(new ThunderWave(), new EarthPower(), new AuraSphere(), new DarkPulse());
    }
}
