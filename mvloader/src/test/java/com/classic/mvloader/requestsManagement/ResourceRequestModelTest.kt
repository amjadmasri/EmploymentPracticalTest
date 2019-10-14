package com.classic.mvloader.requestsManagement

import android.widget.ImageView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.ref.WeakReference

@RunWith(MockitoJUnitRunner::class)
class ResourceRequestModelTest {

    private val url = "url_stub"

    @Test
    fun getId() {
        val resourceRequest = ResourceRequest(url)
        assertNotEquals(resourceRequest.id, null)
    }

    @Test
    fun getData() {
        val resourceRequest = ResourceRequest(url)
        resourceRequest.data = ByteArray(10)
        assertEquals(resourceRequest.data!!.size, 10)
    }

    @Test
    fun getUrl() {
        val resourceRequest = ResourceRequest(url)
        assertEquals(resourceRequest.url, url)
    }

    @Test
    fun getImageView() {
        val resourceRequest = ResourceRequest(url)
        resourceRequest.imageView = WeakReference(mock(ImageView::class.java))
        assertNotEquals(resourceRequest.imageView!!.get(), null)
    }

    @Test
    fun isImageRequest() {
        val resourceRequest = ResourceRequest(url, isImageRequest = false)
        assertEquals(resourceRequest.isImageRequest, false)
    }

    @Test(expected = NullPointerException::class)
    fun successLoadIsNull() {
        val resourceRequest = ResourceRequest(url, isImageRequest = false)
        resourceRequest.loadSuccess!!.invoke(ByteArray(10))
    }

    @Test(expected = NullPointerException::class)
    fun successFailedIsNull() {
        val resourceRequest = ResourceRequest(url, isImageRequest = false)
        resourceRequest.loadFailure!!.invoke("")
    }
}