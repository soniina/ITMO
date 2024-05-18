package common.models;

import exceptions.EnumConstantNotFoundException;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;

public class StudyGroup implements Serializable, Comparable<StudyGroup> {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long studentsCount; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле не может быть null
    private Semester semesterEnum; //Поле может быть null
    private Person groupAdmin; //Поле не может быть null

    public StudyGroup() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public long getStudentsCount() {
        return studentsCount;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public void setId(String string) {
        if (string == null) throw new NullPointerException("id: Значение не может быть пустым!");
        try {
            long id = Long.parseLong(string);
            if (id <= 0) throw new IllegalArgumentException("id: Значение должно быть больше 0!");
            else this.id = id;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id: Неверный формат ввода! Требуется числовое значение.");
        }
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new NullPointerException("name: Значение не может быть пустым!");
        else this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new NullPointerException("coordinates: Значение не может быть пустым!");
        else this.coordinates = coordinates;
    }

    public void setCreationDate(String date) {
        if (date.trim().isEmpty()) throw new NullPointerException("creationDate: Значение не может быть пустым!");
        else {
            String[] parts = date.split("\\.");

            try {
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                this.creationDate = LocalDate.of(year, month, day);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new NumberFormatException("creationDate: Некорректный формат даты!");
            } catch (DateTimeException e) {
                throw new DateTimeException("creationDate: Некорректная дата!");
            }
        }
    }

    public void setStudentsCount(String string) {
        if (string.trim().isEmpty()) throw new NullPointerException("studentsCount: Значение не может быть пустым!");
        try {
            long studentsCount = Long.parseLong(string);
            if (studentsCount <= 0) throw new IllegalArgumentException("studentsCount: Значение должно превышать 0!");
            else this.studentsCount = studentsCount;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("studentsCount: Неверный формат ввода! Требуется числовое значение.");
        }
    }

    public void setFormOfEducation(String form) {
        if (form.trim().isEmpty()) throw new NullPointerException("formOfEducation: Значение не может быть пустым!");
        for (FormOfEducation value : FormOfEducation.values()) {
            if (value.getForm().equals(form.toUpperCase()) || value.name().equals(form.toUpperCase())) {
                this.formOfEducation = value;
                return;
            }
        }
        throw new EnumConstantNotFoundException("formOfEducation: Выбранная форма обучения не найдена!");
    }

    public void setSemester(String string) {
        if (string.trim().isEmpty()) {
            this.semesterEnum = null;
            return;
        }
        try {
            Integer num = Integer.parseInt(string);
            for (Semester value: Semester.values()) {
                if (value.getNum().equals(num)) {
                    this.semesterEnum = value;
                    return;
                }
            }
        } catch (NumberFormatException e) {
            for (Semester value : Semester.values()) {
                if (value.name().equals(string.toUpperCase())) {
                    this.semesterEnum = value;
                    return;
                }
            }
            throw new NumberFormatException("semesterEnum: Неверный формат ввода! Требуется числовое значение.");
        }
        throw new EnumConstantNotFoundException("semesterEnum: Выбранный семестр не найден!");
    }

    public void setGroupAdmin(Person groupAdmin) {
        if (groupAdmin == null) throw new NullPointerException("groupAdmin: Значение не может быть пустым!");
        else this.groupAdmin = groupAdmin;
    }


    @Override
    public int compareTo(StudyGroup studyGroup) {

        if (studentsCount != studyGroup.studentsCount) {
            return Long.valueOf(studentsCount).compareTo(Long.valueOf(studyGroup.studentsCount));
        }
        else if (semesterEnum != null && studyGroup.semesterEnum != null && semesterEnum.compareTo(studyGroup.semesterEnum) != 0) {
            return semesterEnum.compareTo(studyGroup.semesterEnum);
        }
        else if (formOfEducation.compareTo(studyGroup.formOfEducation) != 0) {
            return formOfEducation.compareTo(studyGroup.formOfEducation);
        }
        return Long.compare(id, studyGroup.id);
    }

    @Override
    public String toString() {
        return "StudyGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", studentsCount=" + studentsCount +
                ", formOfEducation=" + formOfEducation +
                ", semesterEnum=" + semesterEnum +
                ", groupAdmin=" + groupAdmin +
                '}';
    }
}
