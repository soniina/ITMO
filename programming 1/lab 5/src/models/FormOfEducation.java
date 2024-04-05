package models;

public enum FormOfEducation {
    FULL_TIME_EDUCATION("ОЧНАЯ"),
    DISTANCE_EDUCATION("ДИСТАНЦИОННАЯ"),
    EVENING_CLASSES("ВЕЧЕРНЯЯ");

    private final String form;

    FormOfEducation(String form) {
        this.form = form;
    }

    public String getForm() {
        return form;
    }
}
