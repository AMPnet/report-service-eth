package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.controller.pojo.TransactionServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.persistence.model.Event
import com.ampnet.reportserviceeth.persistence.repository.EventRepository
import com.ampnet.reportserviceeth.service.EventService
import org.springframework.stereotype.Service
import java.time.ZoneOffset

@Service
class EventServiceImpl(private val eventRepository: EventRepository) : EventService {

    override fun getTransactions(request: TransactionsServiceRequest): List<Event> {
        val from = request.period.from?.toEpochSecond(ZoneOffset.UTC)
        val to = request.period.to?.toEpochSecond(ZoneOffset.UTC)
        return eventRepository
            .findForAddressInPeriod(request.address.value, request.chainId.value, request.issuer.value, from, to)
    }

    override fun getTransaction(request: TransactionServiceRequest): Event? =
        eventRepository.findForTxHash(
            request.txHash.value, request.issuer.value, request.address.value, request.chainId.value
        )
}
