package models;

import exceptions.EnumConstantNotFoundException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Person implements Comparable<Person> {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private LocalDateTime birthday; //Поле не может быть null
    private Color eyeColor; //Поле может быть null
    private Country nationality; //Поле не может быть null
    private Location location; //Поле не может быть null

    public Person() {};

    public String getName() {
        return name;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setName(String name) {
        if (name.trim().isEmpty()) throw new NullPointerException("Person.name: Значение не может быть пустым!");
        else this.name = name;
    }

    public void setBirthday(String date) {
        if (date.trim().isEmpty()) throw new NullPointerException("Person.birthday: Значение не может быть пустым!");
        else {
            String[] parts = date.split("\\.");

            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                this.birthday = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.MIDNIGHT);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new NumberFormatException("Person.birthday: Некорректный формат даты!");
            } catch (DateTimeException e) {
                throw new DateTimeException("Person.birthday: Некорректная дата!");
            }
        }
    }

    public void setEyeColor(String color) {
        if (color.trim().isEmpty()) {
            this.eyeColor = null;
            return;
        }
        for (Color value: Color.values()) {
            if (value.getColor().equals(color.toUpperCase().replace('ё', 'е')) || value.name().equals(color.toUpperCase())) {
                this.eyeColor = value;
                return;
            }
        }
        throw new EnumConstantNotFoundException("Person.eyeColor: Выбранный цвет не найден!");
    }

    public void setNationality(String country) {
        if (country.trim().isEmpty()) throw new NullPointerException("Person.nationality: Значение не может быть пустым!");
        for (Country value: Country.values()) {
            if (value.getCountry().equals(country.toUpperCase()) || value.name().equals(country.toUpperCase())) {
                this.nationality = value;
                return;
            }
        }
        throw new EnumConstantNotFoundException("Person.nationality: Выбранная страна не найдена!");
    }

    public void setLocation(Location location) {
        if (location == null) throw new NullPointerException("Person.location: Значение не может быть пустым!");
        else this.location = location;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", eyeColor=" + eyeColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}';
    }

    @Override
    public int compareTo(Person person) {
        if (name.compareTo(person.name) != 0) {
            return name.compareTo(person.name);
        }
        else if (birthday.compareTo(person.birthday) != 0) {
            return birthday.compareTo(person.birthday);
        }
        return location.compareTo(person.location);
    }
}
