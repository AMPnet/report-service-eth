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
            "AND event.issuer = :issuer " +
            "AND (:from IS NULL OR :from < event.timestamp) " +
            "AND (:to IS NULL OR :to > event.timestamp)" +
            "ORDER BY event.timestamp DESC"
    )
    fun findForAddressInPeriod(address: String, chainId: Long, issuer: String, from: Long?, to: Long?): List<Event>

    @Query(
        "SELECT event FROM Event event " +
            "WHERE event.hash = :txHash " +
            "AND event.issuer = :issuer " +
            "AND (event.fromAddress = :address OR event.toAddress = :address) " +
            "AND event.chainId = :chainId"
    )
    fun findForTxHash(txHash: String, issuer: String, address: String, chainId: Long): Event?

    fun findByChainId(chainId: Long): List<Event>

    @Query(
        nativeQuery = true,
        value = "SELECT inner_table.* FROM ( " +
            "  SELECT *, rank() OVER (PARTITION BY from_address ORDER BY timestamp DESC) AS rank " +
            "  FROM event " +
            "  WHERE issuer = :issuerAddress " +
            "  AND contract = :campaignAddress " +
            ") AS inner_table " +
            "WHERE inner_table.rank = 1 " +
            "AND (inner_table.type = 'RESERVE_INVESTMENT' OR inner_table.type = 'COMPLETED_INVESTMENT')"
    )
    fun findLatestSuccessfulInvestmentEventsByIssuerAndCampaign(
        issuerAddress: String,
        campaignAddress: String
    ): List<Event>
}
