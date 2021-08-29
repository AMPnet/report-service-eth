package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.controller.pojo.TxHistoryRequest
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.service.EventService
import com.ampnet.reportserviceeth.service.data.EventServiceResponse
import org.springframework.stereotype.Service
import java.time.ZoneOffset

@Service
class EventServiceImpl(private val eventRepository: EventRepository) : EventService {

    override fun getTransactions(request: TxHistoryRequest): List<EventServiceResponse> {
        val from = request.period.from?.toEpochSecond(ZoneOffset.UTC)
        val to = request.period.to?.toEpochSecond(ZoneOffset.UTC)
        val events = eventRepository
            .findForAddressInPeriod(request.address.lowercase(), request.chainId, request.issuer, from, to)
        return events.map { EventServiceResponse(it) }
    }
}
