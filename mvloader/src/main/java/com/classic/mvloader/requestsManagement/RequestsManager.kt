package com.classic.mvloader.requestsManagement

import android.widget.ImageView

interface RequestsManager {

    /**
     * request an image resource and load it into an imageView as well as caching it
     * @param imageView ImageView
     * @param url String
     * @return String?
     */
    fun requestImage(imageView: ImageView, url: String): String?

    /**
     * request a generic file resource and invoke the callback with the result as well as caching it
     * @param url String
     * @param loadSuccess Function1<ByteArray, Unit>
     * @param loadFailure Function1<String, Unit>
     * @return String?
     */
    fun requestResource(
        url: String,
        loadSuccess: (ByteArray) -> Unit,
        loadFailure: (String) -> Unit
    ): String?
}