package dao

import entity.Point
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import jakarta.transaction.Transactional
import java.util.*

class PointDAO {

    private var entityManagerFactory: EntityManagerFactory

    init {
        val info = Properties()
        info.load(PointDAO::class.java.classLoader.getResourceAsStream("db.cfg"))
        entityManagerFactory = Persistence.createEntityManagerFactory("default", info)
    }

    fun addPoint(point: Point) {
        val entityManager = entityManagerFactory.createEntityManager()
        try {
            entityManager.transaction.begin()
            entityManager.persist(point)
            entityManager.transaction.commit()
        } finally {
            entityManager.close()
        }
    }

    val allPoints: Collection<Point>
        get() {
            val entityManager = entityManagerFactory.createEntityManager()
            try {
                val criteriaQuery = entityManager.criteriaBuilder.createQuery(Point::class.java)
                val root = criteriaQuery.from(Point::class.java)
                return entityManager.createQuery(criteriaQuery.select(root)).resultList
            } finally {
                entityManager.close()
            }
        }
}