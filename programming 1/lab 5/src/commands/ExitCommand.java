package commands;

/**
 * Команда для завершения работы приложения.
 */
public class ExitCommand extends Command{
    public ExitCommand () {
        super("exit", "завершить работу");
    }

    /**
     * Выполняет команду без аргументов.
     * @param arg аргумент команды (в данной команде не используется).
     */
    @Override
    public void execute(String arg) {
        if (arg != null) System.err.printf("Команда %s не принимает аргументы\n", getName());
        else {
            System.out.println("Завершение работы...");
            System.exit(0);
        }
    }
}
