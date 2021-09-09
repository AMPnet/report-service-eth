package com.ampnet.reportserviceeth.persistence.repository

import com.ampnet.reportserviceeth.persistence.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface TaskRepository : JpaRepository<Task, UUID> {
    @Query("SELECT * from task WHERE chain_id = :chainId ORDER BY block_number DESC LIMIT 1;", nativeQuery = true)
    fun findFirstByBlockNumberForChain(chainId: Long): Task?
}
