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

    @Suppress("ReturnCount")
    override fun getLogoHash(issuerInfoHash: String): String? {
        logoHashes[issuerInfoHash]?.let { return it }
        val request = UriComponentsBuilder.fromUriString(applicationProperties.ipfs.frontendApi)
            .queryParam("hash", issuerInfoHash).build().toUri()
        try {
            val response = restTemplate.postForEntity<IssuerInfoResponse>(request)
            if (response.statusCode.is2xxSuccessful) {
                val logoHash = response.body?.logo ?: run {
                    logger.error { "Missing body in response to: $request" }
                    return null
                }
                logoHashes[issuerInfoHash] = logoHash
                return logoHash
            } else {
                logger.warn { "Unsuccessful request to frontend api: $request" }
                return null
            }
        } catch (ex: RestClientException) {
            logger.warn { "Unexpected exception occurred: ${ex.message}" }
            return null
        }
    }

    private data class IssuerInfoResponse(
        val version: String,
        val name: String,
        val logo: String
    )
}
