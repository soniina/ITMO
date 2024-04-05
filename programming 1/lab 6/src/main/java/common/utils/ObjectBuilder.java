package common.utils;

import common.models.*;
import exceptions.EnumConstantNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Утилитарный класс для создания объектов StudyGroup.
 */
public class ObjectBuilder {

    /**
     * Запрашивает ввод пользователя с заданным приглашением и возвращает введенную строку.
     * @param prompt приглашение для ввода данных.
     */
    public static String prompt(String prompt) {
        System.out.println(prompt);
        return (new Scanner(System.in)).nextLine();
    }

    /**
     * Создает и возвращает объект StudyGroup на основе ввода пользователя.
     */
    public static StudyGroup create() {
        StudyGroup studyGroup = new StudyGroup();

        while (true) {
            try {
                studyGroup.setName(prompt("Введите название группы:"));
                break;
            } catch (NullPointerException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }


        Coordinates coordinates = new Coordinates();
        while (true) {
            try {
                coordinates.setX(prompt("Введите координату x:"));
                break;
            } catch (NullPointerException | IllegalArgumentException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                coordinates.setY(prompt("Введите координату y:"));
                break;
            } catch (NullPointerException | NumberFormatException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }
        studyGroup.setCoordinates(coordinates);
        studyGroup.setCreationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        while (true) {
            try {
                studyGroup.setStudentsCount(prompt("Введите число студентов:"));
                break;
            } catch (NullPointerException | IllegalArgumentException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                studyGroup.setFormOfEducation(prompt("Введите форму обучения. Доступны: дистанционная, очная, вечерняя"));
                break;
            } catch (NullPointerException | EnumConstantNotFoundException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                studyGroup.setSemester(prompt("Введите семестр. Доступны: 1, 3, 7, 8"));
                break;
            } catch (NumberFormatException | EnumConstantNotFoundException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        System.out.println("Введите данные админа группы.");
        Person groupAdmin = new Person();

        while (true) {
            try {
                groupAdmin.setName(prompt("Введите его имя:"));
                break;
            } catch (NullPointerException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                groupAdmin.setBirthday(prompt("Введите его день рождения в формате dd.MM.yyyy:"));
                break;
            } catch (NullPointerException | NumberFormatException | DateTimeException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                groupAdmin.setEyeColor(prompt("Введите его цвет глаз. Доступны: синий, жёлтый, оранжевый, белый"));
                break;
            } catch (EnumConstantNotFoundException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                groupAdmin.setNationality(prompt("Введите его страну происхождения. Доступны: Россия, Великобритания, США, Северная Корея"));
                break;
            } catch (NullPointerException | EnumConstantNotFoundException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        System.out.println("Введите его местоположение.");
        Location adminLocation = new Location();

        while (true) {
            try {
                adminLocation.setName(prompt("Введите название локации:"));
                break;
            } catch (NullPointerException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                adminLocation.setX(prompt("Введите координату x:"));
                break;
            } catch (NullPointerException | NumberFormatException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                adminLocation.setY(prompt("Введите координату y:"));
                break;
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                adminLocation.setZ(prompt("Введите координату z:"));
                break;
            } catch (NullPointerException | NumberFormatException e) {
                System.err.println(e.getMessage() + " Попробуйте ещё раз.");
            }
        }

        groupAdmin.setLocation(adminLocation);
        studyGroup.setGroupAdmin(groupAdmin);
        return studyGroup;
    }

    /**
     * Создает и возвращает объект StudyGroup на основе данных из файла.
     * @param reader для чтения данных из файла.
     */
    public static StudyGroup create(BufferedReader reader) throws IOException {
        StudyGroup studyGroup = new StudyGroup();

        studyGroup.setName(reader.readLine());

        Coordinates coordinates = new Coordinates();
        coordinates.setX(reader.readLine());
        coordinates.setY(reader.readLine());
        studyGroup.setCoordinates(coordinates);

        studyGroup.setCreationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        studyGroup.setStudentsCount(reader.readLine());
        studyGroup.setFormOfEducation(reader.readLine());
        studyGroup.setSemester(reader.readLine());

        Person groupAdmin = new Person();
        groupAdmin.setName(reader.readLine());
        groupAdmin.setBirthday(reader.readLine());
        groupAdmin.setEyeColor(reader.readLine());
        groupAdmin.setNationality(reader.readLine());

        Location adminLocation = new Location();
        adminLocation.setName(reader.readLine());
        adminLocation.setX(reader.readLine());
        adminLocation.setY(reader.readLine());
        adminLocation.setZ(reader.readLine());
        groupAdmin.setLocation(adminLocation);

        studyGroup.setGroupAdmin(groupAdmin);
        return studyGroup;
    }
}


