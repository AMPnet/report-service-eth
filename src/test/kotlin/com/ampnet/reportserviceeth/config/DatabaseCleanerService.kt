package com.ampnet.reportserviceeth.config

import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
class DatabaseCleanerService(val em: EntityManager) {

    @Transactional
    fun deleteAllTasks() {
        em.createNativeQuery("DELETE FROM task").executeUpdate()
    }

    @Transactional
    fun deleteAllEvents() {
        em.createNativeQuery("DELETE FROM event").executeUpdate()
    }
}
