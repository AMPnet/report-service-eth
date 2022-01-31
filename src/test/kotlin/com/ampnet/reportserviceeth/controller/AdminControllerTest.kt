package com.ampnet.reportserviceeth.controller

import com.ampnet.identityservice.proto.UserResponse
import com.ampnet.reportserviceeth.exception.ErrorCode
import com.ampnet.reportserviceeth.exception.InternalException
import com.ampnet.reportserviceeth.security.WithMockCrowdfundUser
import com.ampnet.reportserviceeth.service.data.IssuerCampaignRequest
import com.ampnet.reportserviceeth.service.data.IssuerRequest
import com.ampnet.reportserviceeth.service.impl.toDateString
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.io.ByteArrayInputStream
import java.time.LocalDate

class AdminControllerTest : ControllerTestBase() {

    private val userAccountsSummaryPath = "/admin/${defaultChainId.value}/${issuer.value}/report"
    private val campaignUserAccountsSummaryPath =
        "/admin/${defaultChainId.value}/${issuer.value}/${campaign.value}/report"

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
            Mockito.`when`(userService.getUsersForIssuer(IssuerRequest(issuer, defaultChainId)))
                .thenReturn(listOf(user, secondUser, thirdUser))
        }
        suppose("Blockchain service will return transactions for wallets") {
            Mockito.`when`(blockchainService.getTransactions(userAddress, defaultChainId))
                .thenReturn(createTransactionsResponse())
            Mockito.`when`(blockchainService.getTransactions(secondUserAddress, defaultChainId))
                .thenReturn(emptyList())
            Mockito.`when`(blockchainService.getTransactions(thirdUserAddress, defaultChainId))
                .thenReturn(emptyList())
        }
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
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

            // Uncomment to generate file locally.
//             File(getDownloadDirectory("usersAccountsSummary.pdf")).writeBytes(pdfContent)
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustNotBeAbleToGetReportForOtherIssuer() {
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
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
            Mockito.`when`(userService.getUsersForIssuer(IssuerRequest(issuer, defaultChainId)))
                .thenReturn(listOf(user, secondUser, thirdUser))
        }
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
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
            Mockito.`when`(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
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

    @Test
    @WithMockCrowdfundUser
    fun mustHandleExceptionFromBlockchainServiceWhenFetchingXlsxReport() {
        suppose("Blockchain service will return issuer owner address") {
            given(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
                .willThrow(InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "Failed"))
        }

        verify("Platform manager can get pdf with all user accounts summary") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$userAccountsSummaryPath/xlsx")
            )
                .andExpect(MockMvcResultMatchers.status().isBadGateway)
                .andReturn()
            verifyResponseErrorCode(result, ErrorCode.INT_JSON_RPC_BLOCKCHAIN)
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustBeAbleToDownloadCampaignXlsxForVerifiedUsers() {
        suppose("User service will return a list of users") {
            val user = createUserResponse(userAddress)
            val secondUser = createUserResponse(secondUserAddress)
            val thirdUser = createUserResponse(thirdUserAddress)
            testContext.users = listOf(user, secondUser, thirdUser)
            Mockito.`when`(
                userService.getUsersForIssuerAndCampaign(IssuerCampaignRequest(issuer, campaign, defaultChainId))
            )
                .thenReturn(listOf(user, secondUser, thirdUser))
        }
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
                .thenReturn(userAddress)
        }

        verify("Platform manager can get campaign XLSX report") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$campaignUserAccountsSummaryPath/xlsx")
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
    fun mustNotBeAbleToGetCampaignXlsxReportForOtherIssuer() {
        suppose("Blockchain service will return issuer owner address") {
            Mockito.`when`(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
                .thenReturn(secondUserAddress)
        }

        verify("Other issuer cannot fetch campaign XLSX report") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$campaignUserAccountsSummaryPath/xlsx")
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
            verifyResponseErrorCode(result, ErrorCode.USER_NOT_ISSUER)
        }
    }

    @Test
    @WithMockCrowdfundUser
    fun mustHandleExceptionFromBlockchainServiceWhenCampaignFetchingXlsxReport() {
        suppose("Blockchain service will return issuer owner address") {
            given(blockchainService.getIssuerOwner(IssuerRequest(issuer, defaultChainId)))
                .willThrow(InternalException(ErrorCode.INT_JSON_RPC_BLOCKCHAIN, "Failed"))
        }

        verify("Fetching campaign XLSX report returns RPC error code") {
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("$campaignUserAccountsSummaryPath/xlsx")
            )
                .andExpect(MockMvcResultMatchers.status().isBadGateway)
                .andReturn()
            verifyResponseErrorCode(result, ErrorCode.INT_JSON_RPC_BLOCKCHAIN)
        }
    }

    private fun verifyCellUser(row: Row, user: UserResponse) {
        var column = 0
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.address)
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.email)
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.firstName)
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.lastName)
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.createdAt.toDateString())
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.dateOfBirth)
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.documentNumber)
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.documentValidFrom)
        assertThat(row.getCell(column++).stringCellValue).isEqualTo(user.documentValidUntil)
        assertThat(row.getCell(column).stringCellValue).isEqualTo(user.personalNumber)
    }

    private class TestContext {
        lateinit var users: List<UserResponse>
    }
}
