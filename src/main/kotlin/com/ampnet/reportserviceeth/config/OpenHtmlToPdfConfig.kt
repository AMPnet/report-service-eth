package com.ampnet.reportserviceeth.config

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.slf4j.Slf4jLogger
import com.openhtmltopdf.util.XRLog
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.annotation.PostConstruct

@Configuration
class OpenHtmlToPdfConfig {

    @Bean
    fun pdfRenderer(): PdfRendererBuilder {
        val openSansRegular = ResourceUtils.getFile("classpath:fonts/OpenSans-Regular.ttf")
        return PdfRendererBuilder().useFastMode()
            .useFont(openSansRegular, "OpenSans")
    }

    @PostConstruct
    fun setConfig() {
        XRLog.setLoggerImpl(Slf4jLogger())
        DecimalFormat.getInstance().roundingMode = RoundingMode.HALF_UP
    }
}
