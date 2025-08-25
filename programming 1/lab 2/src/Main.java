import pokemons.*;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        b.addAlly(new Giratina("Чужой", 1));
        b.addAlly(new Golisopod("Хищник", 3));
        b.addAlly(new Poliwhirl("Хулиган", 8));
        b.addFoe(new Politoed("Ворчун", 2));
        b.addFoe(new Poliwag("Чукча", 6));
        b.addFoe(new Wimpod("Злодей", 3));
        b.go();
    }
}