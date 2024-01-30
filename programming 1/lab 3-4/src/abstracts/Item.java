package abstracts;

public abstract class Item {
    private String name;

    @Override
    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public abstract String mean();

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + this.mean().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item that = (Item) o;

        return this.name.equals(that.name) && this.mean().equals(that.mean());
    }
}
