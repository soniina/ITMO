package server.managers

import common.models.*
import client.exceptions.EnumConstantNotFoundException
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import server.exceptions.MissingFileException
import java.io.File
import java.time.DateTimeException
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * Менеджер, отвечающий за загрузку и сохранение элементов коллекции в формате XML.
 */
class DumpManager(private val collection: PriorityQueue<StudyGroup>) {
    private val availableIds = PriorityQueue<Long>()
    private val usedIds: MutableSet<Long> = HashSet()
    private var nextId = 1L

    private fun idIsFree(id: Long): Boolean {
        return !(usedIds.contains(id))
    }

    private val id: Long
        get() {
            val id: Long
            if (!availableIds.isEmpty()) {
                id = availableIds.poll()
            } else {
                id = nextId
                nextId += 1
                usedIds.add(id)
            }
            return id
        }


    /**
     * Создает элемент XML и добавляет его к родительскому элементу.
     * @param doc объект Document, представляющий XML-документ.
     * @param parentElement родительский элемент, к которому добавляется новый элемент.
     * @param tagName имя нового элемента.
     * @param textContent текстовое содержимое нового элемента.
     */
    private fun createElement(doc: Document, parentElement: Element, tagName: String, textContent: String?) {
        val element = doc.createElement(tagName)
        element.textContent = textContent
        parentElement.appendChild(element)
    }

    /**
     * Сохраняет коллекцию в XML-файл.
     * @param filePath путь к файлу, в который будет сохранена коллекция.
     */
    fun saveCollection(filePath: String?) {
        if (filePath == null) throw MissingFileException("Не указан файл сохранения коллекции!")
        else {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()

            val rootElement = doc.createElement("StudyGroups")
            doc.appendChild(rootElement)

            for (studyGroup in collection) {
                val studyGroupElement = doc.createElement("StudyGroup")
                rootElement.appendChild(studyGroupElement)

                createElement(doc, studyGroupElement, "id", studyGroup.id.toString())
                createElement(doc, studyGroupElement, "name", studyGroup.name)

                val coordinatesElement = doc.createElement("coordinates")
                studyGroupElement.appendChild(coordinatesElement)
                createElement(doc, coordinatesElement, "x", studyGroup.coordinates?.x.toString())
                createElement(doc, coordinatesElement, "y", studyGroup.coordinates?.y.toString())

                createElement(
                    doc,
                    studyGroupElement,
                    "creationDate",
                    studyGroup.creationDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                )
                createElement(doc, studyGroupElement, "studentsCount", studyGroup.studentsCount.toString())
                createElement(doc, studyGroupElement, "formOfEducation", studyGroup.formOfEducation?.name)
                createElement(doc, studyGroupElement, "semesterEnum", studyGroup.semesterEnum?.name)

                val groupAdminElement = doc.createElement("groupAdmin")
                studyGroupElement.appendChild(groupAdminElement)
                val groupAdmin = studyGroup.groupAdmin
                createElement(doc, groupAdminElement, "name", groupAdmin?.name)
                createElement(
                    doc,
                    groupAdminElement,
                    "birthday",
                    groupAdmin?.birthday?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                )
                createElement(doc, groupAdminElement, "eyeColor", groupAdmin?.eyeColor?.name)
                createElement(doc, groupAdminElement, "nationality", groupAdmin?.nationality?.name)

                val locationElement = doc.createElement("location")
                groupAdminElement.appendChild(locationElement)
                val location = groupAdmin?.location
                createElement(doc, locationElement, "x", location?.x.toString())
                createElement(doc, locationElement, "y", location?.y.toString())
                createElement(doc, locationElement, "z", location?.z.toString())
                createElement(doc, locationElement, "name", location?.name)
            }

            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.transform(DOMSource(doc), StreamResult(File(filePath)))
        }
    }

    /**
     * Загружает коллекцию из XML-файла.
     * @param filePath путь к файлу, из которого будет загружена коллекция.
     */
    fun loadCollection(filePath: String?) {
        if (filePath == null) throw MissingFileException("Не указан файл загрузки коллекции!")
        else {
            val file = File(filePath)
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

            val nodeGroupsList = doc.getElementsByTagName("StudyGroups")
            if (nodeGroupsList.length > 0) {
                val nodeStudyGroups = nodeGroupsList.item(0)
                if (nodeStudyGroups.nodeType == Node.ELEMENT_NODE) {
                    val elementStudyGroups = nodeStudyGroups as Element

                    val nodeGroupList = elementStudyGroups.getElementsByTagName("StudyGroup")
                    for (i in 0 until nodeGroupList.length) {
                        val nodeStudyGroup = nodeGroupList.item(i)
                        if (nodeStudyGroup.nodeType == Node.ELEMENT_NODE) {
                            val elementStudyGroup = nodeStudyGroup as Element

                            try {
                                val studyGroup = StudyGroup()
                                var id: Long
                                if (elementStudyGroup.getElementsByTagName("id").item(0) != null) {
                                    id = elementStudyGroup.getElementsByTagName("id").item(0).textContent.toLong()
                                    if (!idIsFree(id)) {
                                        System.err.println("Id должен быть уникальным у каждого элемента!")
                                        return
                                    }
                                    usedIds.add(id)
                                    availableIds.remove(id)
                                } else {
                                    id = this.id
                                }
                                studyGroup.setId(id.toString())

                                studyGroup.name = elementStudyGroup.getElementsByTagName("name").item(0).textContent

                                val nodeCoordinates = elementStudyGroup.getElementsByTagName("coordinates").item(0)
                                if (nodeCoordinates.nodeType == Node.ELEMENT_NODE) {
                                    val elementCoordinates = nodeCoordinates as Element
                                    val coordinates = Coordinates()
                                    coordinates.setX(elementCoordinates.getElementsByTagName("x").item(0).textContent)
                                    coordinates.setY(elementCoordinates.getElementsByTagName("y").item(0).textContent)
                                    studyGroup.coordinates = coordinates
                                    studyGroup.setCreationDate(
                                        elementStudyGroup.getElementsByTagName("creationDate").item(0).textContent
                                    )

                                    studyGroup.setStudentsCount(
                                        elementStudyGroup.getElementsByTagName("studentsCount").item(0).textContent
                                    )
                                    studyGroup.setFormOfEducation(
                                        elementStudyGroup.getElementsByTagName("formOfEducation").item(0).textContent
                                    )
                                    studyGroup.setSemester(
                                        elementStudyGroup.getElementsByTagName("semesterEnum").item(0).textContent
                                    )

                                    val nodeGroupAdmin = elementStudyGroup.getElementsByTagName("groupAdmin").item(0)
                                    if (nodeGroupAdmin.nodeType == Node.ELEMENT_NODE) {
                                        val elementGroupAdmin = nodeGroupAdmin as Element
                                        val admin = Person()
                                        admin.name = elementGroupAdmin.getElementsByTagName("name").item(0).textContent
                                        admin.setBirthday(
                                            elementGroupAdmin.getElementsByTagName("birthday").item(0).textContent
                                        )
                                        admin.setEyeColor(
                                            elementGroupAdmin.getElementsByTagName("eyeColor").item(0).textContent
                                        )
                                        admin.setNationality(
                                            elementGroupAdmin.getElementsByTagName("nationality").item(0).textContent
                                        )

                                        val nodeLocation = elementGroupAdmin.getElementsByTagName("location").item(0)
                                        if (nodeLocation.nodeType == Node.ELEMENT_NODE) {
                                            val elementLocation = nodeLocation as Element
                                            val location = Location()
                                            location.name =
                                                elementLocation.getElementsByTagName("name").item(0).textContent
                                            location.setX(elementLocation.getElementsByTagName("x").item(0).textContent)
                                            location.setY(elementLocation.getElementsByTagName("y").item(0).textContent)
                                            location.setZ(elementLocation.getElementsByTagName("z").item(0).textContent)

                                            admin.location = location
                                            studyGroup.groupAdmin = admin
                                            collection.add(studyGroup)
                                            println("Элемент с id=" + studyGroup.id + " успешно добавлен!")
                                        }
                                    }
                                }
                            } catch (e: NullPointerException) {
                                System.err.println(e.message)
                            } catch (e: EnumConstantNotFoundException) {
                                System.err.println(e.message)
                            } catch (e: DateTimeException) {
                                System.err.println(e.message)
                            } catch (e: IllegalArgumentException) {
                                System.err.println(e.message)
                            }
                        }
                    }
                }
            }
        }
    }
}