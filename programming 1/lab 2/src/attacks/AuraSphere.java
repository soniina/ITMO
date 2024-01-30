package attacks;
import ru.ifmo.se.pokemon.*;

public class AuraSphere extends SpecialMove{
    public AuraSphere() {
        super(Type.FIGHTING, 80, 1e9);
    }
    protected String describe() {
        return "применяет AuraSphere";
    }
}
