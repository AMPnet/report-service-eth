package com.ampnet.reportserviceeth.persistence.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "task")
@Suppress("LongParameterList")
class Task(
    @Id
    val uuid: UUID,

    @Column(nullable = false)
    val blockNumber: Long,

    @Column(nullable = false)
    val timestamp: Long
)
