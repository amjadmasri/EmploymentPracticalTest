package com.classic.mvloader.requestsManagement

import android.widget.ImageView

interface RequestsManager {

    fun requestImage(imageView: ImageView, url: String): String?

    fun requestResource(
        url: String,
        loadSuccess: (ByteArray) -> Unit,
        loadFailure: (String) -> Unit
    ): String?
}