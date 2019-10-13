package com.classic.mvloader

import android.widget.ImageView
import java.lang.ref.WeakReference
import java.util.*

class ResourceRequest(
    var url:String,
    var imageView: WeakReference<ImageView>?=null,
    var loadSuccess: ((ByteArray) -> Unit)?=null,
    var loadFailure: ((String) -> Unit)?=null,
    isImageRequest:Boolean =false
) {
    var id=createUniqueId()
        private set
    var data:ByteArray? = null

    var isImageRequest:Boolean
        private set

    init {
        this.isImageRequest=isImageRequest
    }

    private fun createUniqueId(): String {
        return UUID.randomUUID().toString()
    }


}