package enums;
public enum Direction {
    AROUND(" вокруг "),
    TO(" к "),
    FROM(" от "),
    INTHEDIRECTION(" в направлении "),
    OVER(" над "),
    WITH(" за "),
    DUP(" по "),
    ON(" на "),
    OF(" из "),
    IN(" в "),
    NEAR("неподалёку от "),
    INSIDE(" внутрь"),
    BACK(" обратно"),
    DEFAULT(" ");

    private final String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return direction;
    }
}
