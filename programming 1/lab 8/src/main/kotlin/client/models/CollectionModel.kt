package client.models

import client.network.Client
import common.communication.RequestCommand
import common.communication.ResponseStatus
import common.models.StudyGroup
import java.util.PriorityQueue

class CollectionModel {
    val collection: PriorityQueue<StudyGroup> = PriorityQueue()
    val myGroups = ArrayList<Long>()

    init {
        fill()
    }

    fun fill() {
        collection.clear()
        Client.sendRequest(RequestCommand("show", null))
        val response = Client.readResponse()
        if (response.status == ResponseStatus.SUCCESS) {
            val collectionSize = response.descr.toInt()
            for (i in 0 until collectionSize) {
                collection.add(Client.readObject())
            }
        }
        val myGroupsResponse = Client.readResponse().descr.trim()
        if (myGroupsResponse.isNotBlank()) {
            for (group in myGroupsResponse.split(' ')) {
                myGroups.add(group.toLong())
            }
        }
    }

    fun get(id: Long): StudyGroup? {
        for (studyGroup in collection) {
            if (studyGroup.id == id) {
                return studyGroup
            }
        }
        return null
    }
}