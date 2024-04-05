package models;

public class Coordinates implements Comparable<Coordinates> {
    private Double x; //Значение поля должно быть больше -190, Поле не может быть null
    private Float y; //Поле не может быть null

    public Coordinates() {};

    public Double getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public void setX(String line) {
        if (line.trim().isEmpty()) throw new NullPointerException("Coordinates.x: Значение не может быть пустым!");
        try {
            Double x = Double.parseDouble(line);
            if (x <= -190) throw new IllegalArgumentException("Coordinates.x: Значение должно превышать -190!");
            this.x = x;
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("Coordinates.x: Неверный формат ввода! Требуется числовое значение.");
        }
    }

    public void setY(String line) {
        if (line.trim().isEmpty()) throw new NullPointerException("Coordinates.y: Значение не может быть пустым!");
        try {
            this.y = Float.parseFloat(line);
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("Coordinates.y: Неверный формат ввода! Требуется числовое значение.");
        }
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int compareTo(Coordinates coords) {
        if (Double.compare(x, coords.x) != 0) {
            return Double.compare(x, coords.x);
        }
        return Float.compare(y, coords.y);
    }
}
