package com.ampnet.reportserviceeth.service

import com.ampnet.reportserviceeth.service.data.Translations

interface TranslationService {
    fun getTranslations(language: String = DEFAULT_LANGUAGE): Translations
}

const val DEFAULT_LANGUAGE = "en"
