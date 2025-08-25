package server.managers

import common.models.StudyGroup
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class ThreadPoolManager {
    val requestReaderExecutor: ExecutorService = Executors.newFixedThreadPool(5)
    val requestProcessorExecutor: ExecutorService = Executors.newFixedThreadPool(5)
    val responseSenderExecutor: ExecutorService = Executors.newCachedThreadPool()
    val objectQueue = LinkedBlockingQueue<StudyGroup>()
}