package org.example.back4.connection

import com.datastax.oss.driver.api.core.CqlSession
import jakarta.annotation.PreDestroy
import java.net.InetSocketAddress

object CassandraSessionManager {

    private var session: CqlSession? = null

    fun getSession(): CqlSession {
        return session ?: CqlSession.builder()
            .addContactPoint(InetSocketAddress("127.0.0.1", 9042))
            .withKeyspace("web4")
            .withLocalDatacenter("datacenter1")
            .build()
    }

    @PreDestroy
    fun closeSession() {
        session?.close()
        session = null
    }
}