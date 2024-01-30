package enums;

public enum Place {
    WORKSHOP("Мастерская"),
    HOME("Дом"),
    HALL("Коридор"),
    EXIT("Выход"),
    PAVILION("Беседка"),
    ROOF("Крыша");

    private final String location;
    Place(String place) {
        location = place;
    }

    @Override
    public String toString() {
        return location;
    }

    public String getLocation() {
        return location;
    }

    public static String be() { return " находится ";}
}
