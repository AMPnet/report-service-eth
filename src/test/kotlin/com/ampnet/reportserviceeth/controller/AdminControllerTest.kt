package com.ampnet.reportserviceeth.controller

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.security.WithMockCrowdfundUser
import com.ampnet.reportserviceeth.service.impl.toDateString
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.io.ByteArrayInputStream
import java.time.LocalDate

class AdminControllerTest : ControllerTestBase() {

    private val userAccountsSummaryPath = "/admin/$coop/report"

    private lateinit var testContext: TestContext

    @BeforeEach
    fun init() {
        testContext = TestContext()
    }

    @Test
    @WithMockCrowdfundUser
    fun mustBeAbleToGeneratePdfForAllActiveUsers() {
        suppose("User service will return a list of users") {
            val user = createUserResponse(userAddress)
            val secondUser = createUserResponse(secondUserAddress)
            val thirdUser = createUserResponse(thirdUserAddress)
            Mockito.`when`(userService.getUsersForIssuer(coop))
                .thenReturn(listOf(user, secondUser, thirdUser))
        }
        suppose("Blockchain service will return transactions for wallets") {
            Mockito.`when`(blockchainService.getTransactions(userAddress))
                .thenReturn(createTransactionsResponse())
            Mockito.`when`(blockchainService.getTransactions(secondUserAddress))
                .thenReturn(emptyList())
            Mockito.`when`(blockchainService.getTransactions(thirdUserAddress))
                .thenReturn(emptyList())
        }
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(coop))
                .thenReturn(userAddress)
        }

        verify("Platform manager can get pdf with all user accounts summary") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$userAccountsSummaryPath/user")
                    .param("from", "2019-10-10")
                    .param("to", LocalDate.now().plusDays(2).toString())
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            val pdfContent = result.response.contentAsByteArray
            verifyPdfFormat(pdfContent)
//             File(getDownloadDirectory("usersAccountsSummary.pdf")).writeBytes(pdfContent)
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustNotBeAbleToGetReportForOtherIssuer() {
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(coop))
                .thenReturn(secondUserAddress)
        }

        verify("Platform manager can get pdf with all user accounts summary") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$userAccountsSummaryPath/user")
                    .param("from", "2019-10-10")
                    .param("to", LocalDate.now().plusDays(2).toString())
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
            verifyResponseErrorCode(result, ErrorCode.USER_NOT_ISSUER)
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustBeAbleToDownloadXlsxForVerifiedUsers() {
        suppose("User service will return a list of users") {
            val user = createUserResponse(userAddress)
            val secondUser = createUserResponse(secondUserAddress)
            val thirdUser = createUserResponse(thirdUserAddress)
            testContext.users = listOf(user, secondUser, thirdUser)
            Mockito.`when`(userService.getUsersForIssuer(coop))
                .thenReturn(listOf(user, secondUser, thirdUser))
        }
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(coop))
                .thenReturn(userAddress)
        }

        verify("Platform manager can get xlsx report") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$userAccountsSummaryPath/xlsx")
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            val content = result.response.contentAsByteArray
            val wb = WorkbookFactory.create(ByteArrayInputStream(content))
            assertThat(wb.numberOfSheets).isEqualTo(1)
            val sheet = wb.getSheetAt(0)
            assertThat(sheet.lastRowNum).isEqualTo(3)

            for (i in 1..sheet.lastRowNum) {
                verifyCellUser(sheet.getRow(i), testContext.users[i - 1])
            }

            // Uncomment to generate file locally.
//             File(getDownloadDirectory("test-xlsx.xlsx")).writeBytes(content)
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustNotBeAbleToGetXlsxReportForOtherIssuer() {
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(coop))
                .thenReturn(secondUserAddress)
        }

        verify("Platform manager can get pdf with all user accounts summary") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$userAccountsSummaryPath/xlsx")
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
            verifyResponseErrorCode(result, ErrorCode.USER_NOT_ISSUER)
        }
    }

    private fun verifyCellUser(row: Row, user: UserResponse) {
        assertThat(row.getCell(0).stringCellValue).isEqualTo(user.address)
        assertThat(row.getCell(1).stringCellValue).isEqualTo(user.email)
        assertThat(row.getCell(2).stringCellValue).isEqualTo(user.firstName)
        assertThat(row.getCell(3).stringCellValue).isEqualTo(user.lastName)
        assertThat(row.getCell(4).stringCellValue).isEqualTo(user.createdAt.toDateString())
    }

    private class TestContext {
        lateinit var users: List<UserResponse>
    }
}
