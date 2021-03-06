package com.ikarimeister.loginapp.data.network

import java.io.File
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.apache.commons.io.FileUtils
import org.hamcrest.MatcherAssert
import org.hamcrest.core.StringContains
import org.junit.After
import org.junit.Assert
import org.junit.Before

open class MockWebServerTest {

    companion object {
        private val FILE_ENCODING = "UTF-8"
    }

    private var server: MockWebServer = MockWebServer()

    @Before
    open fun setup() {
        server.start()
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    fun enqueueMockResponse(code: Int = 200, fileName: String? = null) {
        val mockResponse = MockResponse()
        val fileContent = getContentFromFile(fileName)
        mockResponse.setResponseCode(code)
        mockResponse.setBody(fileContent)
        server.enqueue(mockResponse)
    }

    protected fun assertRequestSentTo(url: String) {
        val request = server.takeRequest()
        Assert.assertEquals(url, request.path)
    }

    protected fun assertGetRequestSentTo(url: String) {
        val request = server.takeRequest()
        Assert.assertEquals(url, request.path)
        Assert.assertEquals("GET", request.method)
    }

    protected fun assertPostRequestSentTo(url: String) {
        val request = server.takeRequest()
        Assert.assertEquals(url, request.path)
        Assert.assertEquals("POST", request.method)
    }

    protected fun assertPutRequestSentTo(url: String) {
        val request = server.takeRequest()
        Assert.assertEquals(url, request.path)
        Assert.assertEquals("PUT", request.method)
    }

    protected fun assertDeleteRequestSentTo(url: String) {
        val request = server.takeRequest()
        Assert.assertEquals(url, request.path)
        Assert.assertEquals("DELETE", request.method)
    }

    protected fun assertRequestSentToContains(vararg paths: String) {
        val request = server.takeRequest()

        for (path in paths) {
            MatcherAssert.assertThat(request.path, StringContains.containsString(path))
        }
    }

    fun assertRequestContainsHeader(key: String, expectedValue: String, requestIndex: Int = 0) {
        val recordedRequest = getRecordedRequestAtIndex(requestIndex)
        val value = recordedRequest!!.getHeader(key)
        Assert.assertEquals(expectedValue, value)
    }

    protected val baseEndpoint: String
        get() = server.url("/").toString()

    protected fun assertRequestBodyEquals(jsonFile: String) {
        val request = server.takeRequest()
        Assert.assertEquals(getContentFromFile(jsonFile), request.body.readUtf8())
    }

    private fun getContentFromFile(fileName: String? = null): String {
        return if (fileName == null) {
            ""
        } else {
            val file = File(javaClass.getResource("/" + fileName).file)
            val lines = FileUtils.readLines(file, FILE_ENCODING)
            val stringBuilder = StringBuilder()
            for (line in lines) {
                stringBuilder.append(line)
            }
            stringBuilder.toString()
        }
    }

    private fun getRecordedRequestAtIndex(requestIndex: Int): RecordedRequest? =
            (0..requestIndex).map { server.takeRequest() }.last()
}