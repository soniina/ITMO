package common.models;

public enum Semester {
    FIRST(1),
    THIRD(3),
    SEVENTH(7),
    EIGHTH(8);

    private final Integer num;

    Semester(Integer num) {
        this.num = num;
    }

    public Integer getNum() {
        return num;
    }
}
