package com.classic.mvloader.requestsManagement

import android.widget.ImageView
import java.lang.ref.WeakReference
import java.util.*

/**
 * A wrapper class over a request of either an image type or a resource type
 * @property url String
 * @property imageView WeakReference<ImageView>?
 * @property loadSuccess Function1<ByteArray, Unit>?
 * @property loadFailure Function1<String, Unit>?
 * @property isImageRequest Boolean
 * @property id String
 * @property data ByteArray?
 * @constructor
 */
data class ResourceRequest(
    var url: String,
    var imageView: WeakReference<ImageView>? = null,
    var loadSuccess: ((ByteArray) -> Unit)? = null,
    var loadFailure: ((String) -> Unit)? = null,
    val isImageRequest: Boolean = false
) {
    var id = createUniqueId()
        private set
    var data: ByteArray? = null

    /**
     * generate a unique ID for each request
     * @return String
     */
    private fun createUniqueId(): String {
        return UUID.randomUUID().toString()
    }


}