package com.ampnet.reportserviceeth.service.impl

import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.service.TranslationService
import com.ampnet.reportserviceeth.service.data.Translations
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class TranslationServiceImpl(
    @Qualifier("camelCaseObjectMapper") private val objectMapper: ObjectMapper
) : TranslationService {

    private val allTranslations by lazy {
        val json = javaClass.classLoader.getResource("templates/translations.json")?.readText()
            ?: throw InternalException(
                ErrorCode.INT_GENERATING_PDF,
                "Could not find translations.json"
            )
        objectMapper.readValue<Map<String, Map<String, String>>>(json)
    }

    override fun getTranslations(language: String): Translations {
        val translations = allTranslations[language] ?: allTranslations["en"]
            ?: throw InternalException(
                ErrorCode.INT_GENERATING_PDF,
                "Could not find default[en] translation"
            )
        return Translations(translations)
    }
}
