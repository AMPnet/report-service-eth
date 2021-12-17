package com.ampnet.reportserviceeth.service.impl

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.grpc.userservice.UserService
import com.ampnet.reportserviceeth.service.XlsxService
import com.ampnet.reportserviceeth.service.data.IssuerCampaignRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.data.secondsToLocalDateTime
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.format.DateTimeFormatter

@Service
class XlsxServiceImpl(private val userService: UserService) : XlsxService {

    companion object {
        const val headerFontSize: Short = 16
        const val dataFontSize: Short = 14
    }

    private lateinit var workbook: XSSFWorkbook
    private lateinit var sheet: XSSFSheet

    @Throws(InternalException::class)
    override fun generateXlsx(issuerRequest: IssuerRequest): ByteArray {
        return generateXlsx(issuerRequest, userService::getUsersForIssuer)
    }

    @Throws(InternalException::class)
    override fun generateXlsx(issuerCampaignRequest: IssuerCampaignRequest): ByteArray {
        return generateXlsx(issuerCampaignRequest, userService::getUsersForIssuerAndCampaign)
    }

    @Throws(InternalException::class)
    private fun <T> generateXlsx(request: T, fetchUsers: (T) -> List<UserResponse>): ByteArray {
        try {
            workbook = XSSFWorkbook()
            val users = fetchUsers(request)
            writeHeaderLine()
            writeDataLines(users)
            val outputStream = ByteArrayOutputStream()
            workbook.write(outputStream)
            outputStream.close()
            workbook.close()
            return outputStream.toByteArray()
        } catch (exception: IOException) {
            throw InternalException(ErrorCode.INT_GENERATING_XLSX, "Failed to generate xlsx file", exception)
        }
    }

    private fun writeHeaderLine() {
        sheet = workbook.createSheet("Users")
        val font = workbook.createFont().apply {
            bold = true
            fontHeightInPoints = headerFontSize
        }
        val style = workbook.createCellStyle().apply { setFont(font) }
        val row = sheet.createRow(0)
        var columnCount = 0
        createCell(row, columnCount++, "Wallet address", style)
        createCell(row, columnCount++, "E-mail", style)
        createCell(row, columnCount++, "First Name", style)
        createCell(row, columnCount++, "Last Name", style)
        createCell(row, columnCount++, "Registration Date", style)
        createCell(row, columnCount++, "Date of Birth", style)
        createCell(row, columnCount++, "Document Number", style)
        createCell(row, columnCount++, "Date of Issue", style)
        createCell(row, columnCount++, "Date of Expiry", style)
        createCell(row, columnCount, "Personal Number", style)
    }

    private fun writeDataLines(users: List<UserResponse>) {
        val font = workbook.createFont().apply { fontHeightInPoints = dataFontSize }
        val style = workbook.createCellStyle().apply { setFont(font) }
        var rowCount = 1
        var columnCount = 0
        for (user in users) {
            val row: Row = sheet.createRow(rowCount++)
            columnCount = 0
            createCell(row, columnCount++, user.address, style)
            createCell(row, columnCount++, user.email, style)
            createCell(row, columnCount++, user.firstName, style)
            createCell(row, columnCount++, user.lastName, style)
            createCell(row, columnCount++, user.createdAt.toDateString(), style)
            createCell(row, columnCount++, user.dateOfBirth, style)
            createCell(row, columnCount++, user.documentNumber, style)
            createCell(row, columnCount++, user.documentValidFrom, style)
            createCell(row, columnCount++, user.documentValidUntil, style)
            createCell(row, columnCount, user.personalNumber, style)
        }
        for (i in 0..columnCount) {
            sheet.autoSizeColumn(i, true)
        }
    }

    private fun createCell(row: Row, columnCount: Int, value: String, style: CellStyle) {
        row.createCell(columnCount).apply {
            setCellValue(value)
            cellStyle = style
        }
    }
}

fun Long.toDateString(): String =
    secondsToLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
