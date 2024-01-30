package abstracts;

import creatures.Bendum;
import enums.Parameter;
import enums.Place;

public abstract class Creature {
    private String name;
    private Parameter status = Parameter.DEFAULT;

    @Override
    public String toString() {
        return name + status;
    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status.toString();
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setStatus(Parameter status) {
        this.status = status;
    }

    public String say() {
        return toString() + " сказал";
    }
    public String say(String phrase) {
        return toString() + " сказал: \"" + phrase + "\"";
    }

    public String takeAlarm() {
        return toString() + " всполошился";
    }
    public String rush() {
        return toString() + " бросился";
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Creature that = (Creature) o;

        return this.name.equals(that.name) && this.status == that.status;
    }
}
