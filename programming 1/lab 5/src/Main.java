import managers.CollectionManager;
import managers.CommandManager;
import models.StudyGroup;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Set<String> availableFields = new LinkedHashSet<>(Arrays.asList("id", "название", "координаты", "дата создания", "число студентов", "форма обучения", "семестр", "админ группы"));
        System.out.println("Введите поля для сортировки коллекции через запятую. Доступны: " + String.join(", ", availableFields) + ".");
        String[] fields = sc.nextLine().split(",");
        Set<String> sortFields = new HashSet<>();
        for (String field : fields) {
            if (availableFields.contains(field.trim().toLowerCase())) {
                sortFields.add(field.trim().toLowerCase());
            } else {
                System.err.println("Неизвестное поле для сортировки: " + field);
            }
        }

        if (sortFields.isEmpty()) {
            System.out.println("Коллекция сохраняет порядок добавления элементов, без дополнительной сортировки.");
        } else {
            System.out.println("Коллекция будет сортироваться по полю(-ям): " + String.join(", ", sortFields));
        }

        PriorityQueue<StudyGroup> collection = new PriorityQueue<>((group1, group2) -> {
            int result = 0;
            for (String field : fields) {
                switch (field.trim().toLowerCase()) {
                    case "id" -> result += group1.getId().compareTo(group2.getId());
                    case "название" -> result += group1.getName().compareTo(group2.getName());
                    case "координаты" -> result += group1.getCoordinates().compareTo(group2.getCoordinates());
                    case "дата создания" -> result += group1.getCreationDate().compareTo(group2.getCreationDate());
                    case "число студентов" -> result += Long.compare(group1.getStudentsCount(), group2.getStudentsCount());
                    case "форма обучения" -> result += group1.getFormOfEducation().compareTo(group2.getFormOfEducation());
                    case "семестр" -> result += group1.getSemesterEnum().compareTo(group2.getSemesterEnum());
                    case "админ группы" -> result += group1.getGroupAdmin().compareTo(group2.getGroupAdmin());
                }
            }
            return Math.max(Math.min(result, 1), -1);
        });

        String loadFile = null;
        if (args.length > 0) loadFile = args[0];
        CollectionManager collectionManager = new CollectionManager(collection, loadFile);
        CommandManager commandManager = new CommandManager(collectionManager);

        while (sc.hasNext()) {
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) commandManager.execute(line);
        }
    }
}