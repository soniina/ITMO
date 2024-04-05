package commands;

/**
 * Абстрактный класс, представляющий команду.
 */
public abstract class Command {

    /**
     * Выполняет команду с переданным аргументом.
     * @param arg аргумент команды.
     */
    abstract public void execute(String arg);
    private final String name;
    private final String descr;
    Command(String name, String descr) {
        this.name = name;
        this.descr = descr;
    }

    public String getName() {
        return name;
    }
    public String getDescr() {
        return descr;
    }
}
