//package com.nazarethlabs.joseph.integration.resend
//
//import com.nazarethlabs.joseph.core.client.HttpClient
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertThrows
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito.any
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`
//
//class ResendClientTest {
//    private lateinit var client: HttpClient
//    private lateinit var resendClient: ResendClient
//    private val batchSize = 2
//    private val emailFrom = "from@example.com"
//
//    @BeforeEach
//    fun setUp() {
//        client = mock(HttpClient::class.java)
//        resendClient = ResendClient(client, batchSize, emailFrom)
//    }
//
//    @Test
//    fun `should send emails in a single batch`() {
//        val emails = listOf("a@a.com", "b@b.com")
//        val subject = "Test"
//        val html = "<b>body</b>"
//        val response = ResendEmailResponse("ok")
//
//        `when`(client.post<ResendEmailResponse>(any(), any(), any(), any())).thenReturn(response)
//
//        val result = resendClient.send(emails, subject, html)
//
//        assertEquals(1, result.size)
//        assertEquals(response, result[0])
//    }
//
//    @Test
//    fun `should send emails in multiple batches`() {
//        val emails = listOf("a@a.com", "b@b.com", "c@c.com")
//        val subject = "Test"
//        val html = "<b>body</b>"
//        val response = ResendEmailResponse("ok")
//
//        `when`(client.post<ResendEmailResponse>(any(), any(), any(), any())).thenReturn(response)
//
//        val result = resendClient.send(emails, subject, html)
//
//        assertEquals(2, result.size)
//        assertEquals(response, result[0])
//        assertEquals(response, result[1])
//    }
//
//    @Test
//    fun `should handle exception in sendWithParams`() {
//        val emails = listOf("a@a.com", "b@b.com")
//        val subject = "Test"
//        val html = "<b>body</b>"
//
//        `when`(client.post<ResendEmailResponse>(any(), any(), any(), any())).thenThrow(RuntimeException("fail"))
//
//        val result = resendClient.send(emails, subject, html)
//
//        assertEquals(1, result.size)
//        val error = result[0] as Map<*, *>
//        assertTrue((error["error"] as String).contains("fail"))
//    }
//
//    @Test
//    fun `should throw if response contains error message`() {
//        val emails = listOf("a@a.com")
//        val subject = "Test"
//        val html = "<b>body</b>"
//        val response = ResendEmailResponse("error: something went wrong")
//
//        `when`(client.post<ResendEmailResponse>(any(), any(), any(), any())).thenReturn(response)
//
//        val ex = assertThrows(Exception::class.java) {
//            resendClient.send(emails, subject, html)
//        }
//        assertTrue(ex.message!!.contains("Error in response of sending email"))
//    }
//}
//
//private data class ResendEmailResponse(val message: String?)
