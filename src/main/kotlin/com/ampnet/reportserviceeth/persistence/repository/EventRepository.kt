package com.ampnet.reportserviceeth.persistence.repository

import com.ampnet.reportserviceeth.persistence.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EventRepository : JpaRepository<Event, UUID>
