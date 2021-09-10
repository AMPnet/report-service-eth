package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.config.ApplicationProperties
import com.ampnet.reportserviceeth.service.IpfsService
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.util.UriComponentsBuilder

@Service
class IpfsServiceImpl(
    private val applicationProperties: ApplicationProperties,
    private val restTemplate: RestTemplate
) : IpfsService {

    private val logoHashes = mutableMapOf<String, String>()

    companion object : KLogging()

    override fun getLogoHash(issuerInfoHash: String): String? {
        logoHashes[issuerInfoHash]?.let { return it }
        val request = UriComponentsBuilder.fromUriString(applicationProperties.ipfs.frontendApi)
            .queryParam("hash", issuerInfoHash).build().toUri()
        return try {
            val response = restTemplate.postForEntity<IssuerInfoResponse>(request)
            if (response.statusCode.is2xxSuccessful) {
                response.body?.logo?.let {
                    logoHashes[issuerInfoHash] = it
                    it
                } ?: run {
                    logger.error { "Missing body in response to: $request" }
                    null
                }
            } else {
                logger.warn { "Unsuccessful request to frontend api: $request" }
                null
            }
        } catch (ex: RestClientException) {
            logger.warn { "Unexpected exception occurred: ${ex.message}" }
            null
        }
    }

    private data class IssuerInfoResponse(
        val version: String,
        val name: String,
        val logo: String
    )
}
