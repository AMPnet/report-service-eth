package com.ampnet.reportserviceeth.controller

import com.ampnet.reportserviceeth.blockchain.properties.Chain
import com.ampnet.reportserviceeth.controller.pojo.PeriodServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TransactionsServiceRequest
import com.ampnet.reportserviceeth.controller.pojo.TxHistoryResponse
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InvalidRequestException
import com.ampnet.reportserviceeth.service.EventService
import com.ampnet.reportserviceeth.service.data.EventServiceResponse
import mu.KLogging
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class TxHistoryController(private val eventService: EventService) {

    companion object : KLogging()

    @GetMapping("/tx_history/{chainId}/{issuer}")
    fun getTransactionForChain(
        @PathVariable chainId: Long,
        @PathVariable issuer: String,
        @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): ResponseEntity<TxHistoryResponse> {
        if (Chain.fromId(chainId) == null) {
            throw InvalidRequestException(ErrorCode.BLOCKCHAIN_ID, "Invalid blockchain id: $chainId")
        }
        val address = ControllerUtils.getAddressFromSecurityContext()
        logger.debug {
            "Received request to get transactions for address: $address on chain: $chainId for issuer: $issuer"
        }
        val period = PeriodServiceRequest(from, to)
        val request = TransactionsServiceRequest(address, chainId, issuer, period)
        val events = eventService.getTransactions(request).map { EventServiceResponse(it) }
        return ResponseEntity.ok(TxHistoryResponse(events))
    }
}
