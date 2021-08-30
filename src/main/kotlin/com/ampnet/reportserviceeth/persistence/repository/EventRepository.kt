package com.ampnet.reportserviceeth.persistence.repository

import com.ampnet.reportserviceeth.persistence.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface EventRepository : JpaRepository<Event, UUID> {
    @Query(
        "SELECT event FROM Event event " +
            "WHERE (event.fromAddress = :address OR event.toAddress = :address) " +
            "AND event.chainId = :chainId " +
            "AND (:from IS NULL OR :from < event.timestamp) " +
            "AND (:to IS NULL OR :to > event.timestamp)"
    )
    fun findForAddressInPeriod(address: String, chainId: Long, from: Long?, to: Long?): List<Event>

    @Query(
        "SELECT event FROM Event event " +
            "WHERE event.contract = :issuer " +
            "AND (event.fromAddress = :address OR event.toAddress = :address) " +
            "AND event.chainId = :chainId " +
            "AND (:from IS NULL OR :from < event.timestamp) " +
            "AND (:to IS NULL OR :to > event.timestamp)"
    )
    fun findForAddressAndIssuerInPeriod(
        issuer: String, address: String, chainId: Long, from: Long?, to: Long?
    ): List<Event>

    @Query(
        "SELECT event FROM Event event " +
            "WHERE event.hash = :txHash " +
            "AND event.contract = :issuer " +
            "AND (event.fromAddress = :address OR event.toAddress = :address) " +
            "AND event.chainId = :chainId"
    )
    fun findForTxHash(txHash: String, issuer: String, address: String, chainId: Long): Event?
}
