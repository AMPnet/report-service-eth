package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.service.FileService
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.util.UriComponentsBuilder

@Service
class FileServiceImpl(private val restTemplate: RestTemplate) : FileService {

    companion object : KLogging()

    private val frontendApi = "https://amptzr-git-sd-338-create-frontend-api-ampnetx.vercel.app/api/ipfs/issuer"

    override fun getLogoHash(issuerInfoHash: String): String? {
        val request = UriComponentsBuilder.fromUriString(frontendApi)
            .queryParam("hash", issuerInfoHash).build().toUri()
        try {
            val response = restTemplate.postForEntity<IssuerInfoResponse>(request)
            return if (response.statusCode.is2xxSuccessful) {
                response.body?.logo ?: run {
                    logger.error { "Missing body in response to: $request" }
                    null
                }
            } else {
                logger.error { "Unsuccessful request to frontend api: $request" }
                null
            }
        } catch (ex: RestClientException) {
            logger.error { "Unexpected exception occurred: ${ex.message}" }
            return null
        }
    }
}

data class IssuerInfoResponse(
    val version: String,
    val name: String,
    val logo: String
)
