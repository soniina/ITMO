package enums;

public enum Parameter {
    FORCE(" с силой"),
    TENSION(" с напряжением"),
    ALITTLE(" немного"),
    STRICT(" строго"),
    INSTANTLY(" мгновенно"),
    NOTAFRAID(" не испуганный"),
    DEFAULT("");

    private final String description;
    Parameter(String parameter) {
        description = parameter;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
