package models;

public class Location implements Comparable<Location> {
    private Double x; //Поле не может быть null
    private long y;
    private Float z; //Поле не может быть null
    private String name; //Поле не может быть null

    public void setName(String name) {
        if (name.trim().isEmpty()) throw new NullPointerException("Location.name: Значение не может быть пустым!");
        this.name = name;
    }

    public void setY(String line) {
        if (line.trim().isEmpty()) {
            this.y = 0;
            return;
        }
        try {
            this.y = Long.parseLong(line);
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("Location.y: Неверный формат ввода! Требуется числовое значение.");
        }
    }

    public void setX(String line) {
        if (line.trim().isEmpty()) throw new NullPointerException("Location.x: Значение не может быть пустым!");
        try {
            this.x = Double.parseDouble(line);
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("Location.x: Неверный формат ввода! Требуется числовое значение.");
        }
    }

    public void setZ(String line) {
        if (line.trim().isEmpty()) throw new NullPointerException("Location.z: Значение не может быть пустым!");
        try {
            this.z = Float.parseFloat(line);
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("Location.z: Неверный формат ввода! Требуется числовое значение.");
        }
    }

    public Double getX() {
        return x;
    }

    public Float getZ() {
        return z;
    }

    public long getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z + '\'' +
                '}';
    }

    @Override
    public int compareTo(Location location) {
        if (Double.compare(x, location.x) != 0) {
            return Double.compare(x, location.x);
        }
        else if (y != location.y) {
            return Long.compare(y, location.y);
        }
        else if (Float.compare(z, location.z) != 0) {
            return Float.compare(z, location.z);
        }
        return name.compareTo(location.name);
    }
}
