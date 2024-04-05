package models;

public enum Color {
    BLUE ("СИНИЙ"),
    YELLOW ("ЖЕЛТЫЙ"),
    ORANGE ("ОРАНЖЕВЫЙ"),
    WHITE ("БЕЛЫЙ");

    private final String color;

    Color (String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
