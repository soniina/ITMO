package server.managers;

import common.models.*;
import exceptions.EnumConstantNotFoundException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.exceptions.MissingFileException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Менеджер, отвечающий за загрузку и сохранение элементов коллекции в формате XML.
 */

public class DumpManager {

    private final PriorityQueue<StudyGroup> collection;
    private PriorityQueue<Long> availableIds = new PriorityQueue<>();
    private Set<Long> usedIds = new HashSet<>();
    private Long nextId = 1L;
    public DumpManager(PriorityQueue<StudyGroup> collection) {
        this.collection = collection;
    }

    public boolean idIsFree(Long id) {
        return !(usedIds.contains(id));
    }

    public Long getId() {
        Long id;
        if (!availableIds.isEmpty()) {
            id = availableIds.poll();
        } else {
            id = nextId;
            nextId += 1;
            usedIds.add(id);
        }
        return id;
    }


    /**
     * Создает элемент XML и добавляет его к родительскому элементу.
     * @param doc объект Document, представляющий XML-документ.
     * @param parentElement родительский элемент, к которому добавляется новый элемент.
     * @param tagName имя нового элемента.
     * @param textContent текстовое содержимое нового элемента.
     */
    private void createElement(Document doc, Element parentElement, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent);
        parentElement.appendChild(element);
    }

    /**
     * Сохраняет коллекцию в XML-файл.
     * @param filePath путь к файлу, в который будет сохранена коллекция.
     */
    public void saveCollection(String filePath) throws Exception {
        if (filePath == null) throw new MissingFileException("Не указан файл сохранения коллекции!");
        else {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element rootElement = doc.createElement("StudyGroups");
            doc.appendChild(rootElement);

            for (StudyGroup studyGroup : collection) {
                Element studyGroupElement = doc.createElement("StudyGroup");
                rootElement.appendChild(studyGroupElement);

                createElement(doc, studyGroupElement, "id", String.valueOf(studyGroup.getId()));
                createElement(doc, studyGroupElement, "name", studyGroup.getName());

                Element coordinatesElement = doc.createElement("coordinates");
                studyGroupElement.appendChild(coordinatesElement);
                createElement(doc, coordinatesElement, "x", String.valueOf(studyGroup.getCoordinates().getX()));
                createElement(doc, coordinatesElement, "y", String.valueOf(studyGroup.getCoordinates().getY()));

                createElement(doc, studyGroupElement, "creationDate", studyGroup.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                createElement(doc, studyGroupElement, "studentsCount", String.valueOf(studyGroup.getStudentsCount()));
                createElement(doc, studyGroupElement, "formOfEducation", studyGroup.getFormOfEducation().name());
                createElement(doc, studyGroupElement, "semesterEnum", studyGroup.getSemesterEnum().name());

                Element groupAdminElement = doc.createElement("groupAdmin");
                studyGroupElement.appendChild(groupAdminElement);
                Person groupAdmin = studyGroup.getGroupAdmin();
                createElement(doc, groupAdminElement, "name", groupAdmin.getName());
                createElement(doc, groupAdminElement, "birthday", groupAdmin.getBirthday().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                createElement(doc, groupAdminElement, "eyeColor", groupAdmin.getEyeColor().name());
                createElement(doc, groupAdminElement, "nationality", groupAdmin.getNationality().name());

                Element locationElement = doc.createElement("location");
                groupAdminElement.appendChild(locationElement);
                Location location = groupAdmin.getLocation();
                createElement(doc, locationElement, "x", String.valueOf(location.getX()));
                createElement(doc, locationElement, "y", String.valueOf(location.getY()));
                createElement(doc, locationElement, "z", String.valueOf(location.getZ()));
                createElement(doc, locationElement, "name", location.getName());
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));
        }
    }

    /**
     * Загружает коллекцию из XML-файла.
     * @param filePath путь к файлу, из которого будет загружена коллекция.
     */
    public void loadCollection(String filePath) throws Exception {

        if (filePath == null) throw new MissingFileException("Не указан файл загрузки коллекции!");
        else {
            File file = new File(filePath);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

            NodeList nodeGroupsList = doc.getElementsByTagName("StudyGroups");
            if (nodeGroupsList.getLength() > 0) {
                Node nodeStudyGroups = nodeGroupsList.item(0);
                if (nodeStudyGroups.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementStudyGroups = (Element) nodeStudyGroups;

                    NodeList nodeGroupList = elementStudyGroups.getElementsByTagName("StudyGroup");
                    for (int i = 0; i < nodeGroupList.getLength(); i++) {
                        Node nodeStudyGroup = nodeGroupList.item(i);
                        if (nodeStudyGroup.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementStudyGroup = (Element) nodeStudyGroup;

                            try {
                                StudyGroup studyGroup = new StudyGroup();
                                Long id;
                                if (elementStudyGroup.getElementsByTagName("id").item(0) != null) {
                                    id = Long.parseLong(elementStudyGroup.getElementsByTagName("id").item(0).getTextContent());
                                    if (!idIsFree(id)) {
                                        System.err.println("Id должен быть уникальным у каждого элемента!");
                                        return;
                                    }
                                    usedIds.add(id);
                                    availableIds.remove(id);
                                } else {
                                    id = getId();
                                }
                                studyGroup.setId(id.toString());

                                studyGroup.setName(elementStudyGroup.getElementsByTagName("name").item(0).getTextContent());

                                Node nodeCoordinates = elementStudyGroup.getElementsByTagName("coordinates").item(0);
                                if (nodeCoordinates.getNodeType() == Node.ELEMENT_NODE) {
                                    Element elementCoordinates = (Element) nodeCoordinates;
                                    Coordinates coordinates = new Coordinates();
                                    coordinates.setX(elementCoordinates.getElementsByTagName("x").item(0).getTextContent());
                                    coordinates.setY(elementCoordinates.getElementsByTagName("y").item(0).getTextContent());
                                    studyGroup.setCoordinates(coordinates);
                                    studyGroup.setCreationDate(elementStudyGroup.getElementsByTagName("creationDate").item(0).getTextContent());

                                    studyGroup.setStudentsCount(elementStudyGroup.getElementsByTagName("studentsCount").item(0).getTextContent());
                                    studyGroup.setFormOfEducation(elementStudyGroup.getElementsByTagName("formOfEducation").item(0).getTextContent());
                                    studyGroup.setSemester(elementStudyGroup.getElementsByTagName("semesterEnum").item(0).getTextContent());

                                    Node nodeGroupAdmin = elementStudyGroup.getElementsByTagName("groupAdmin").item(0);
                                    if (nodeGroupAdmin.getNodeType() == Node.ELEMENT_NODE) {
                                        Element elementGroupAdmin = (Element) nodeGroupAdmin;
                                        Person admin = new Person();
                                        admin.setName(elementGroupAdmin.getElementsByTagName("name").item(0).getTextContent());
                                        admin.setBirthday(elementGroupAdmin.getElementsByTagName("birthday").item(0).getTextContent());
                                        admin.setEyeColor(elementGroupAdmin.getElementsByTagName("eyeColor").item(0).getTextContent());
                                        admin.setNationality(elementGroupAdmin.getElementsByTagName("nationality").item(0).getTextContent());

                                        Node nodeLocation = elementGroupAdmin.getElementsByTagName("location").item(0);
                                        if (nodeLocation.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elementLocation = (Element) nodeLocation;
                                            Location location = new Location();
                                            location.setName(elementLocation.getElementsByTagName("name").item(0).getTextContent());
                                            location.setX(elementLocation.getElementsByTagName("x").item(0).getTextContent());
                                            location.setY(elementLocation.getElementsByTagName("y").item(0).getTextContent());
                                            location.setZ(elementLocation.getElementsByTagName("z").item(0).getTextContent());

                                            admin.setLocation(location);
                                            studyGroup.setGroupAdmin(admin);
                                            collection.add(studyGroup);
                                            System.out.println("Элемент с id=" + studyGroup.getId() + " успешно добавлен!");
                                        }
                                    }
                                }
                            } catch (NullPointerException | EnumConstantNotFoundException | DateTimeException | IllegalArgumentException e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
}