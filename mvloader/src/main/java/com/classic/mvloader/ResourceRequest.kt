package com.classic.mvloader

import android.widget.ImageView
import java.lang.ref.WeakReference
import java.util.*

data class ResourceRequest(
    var url:String,
    var imageView: WeakReference<ImageView>?=null,
    var loadSuccess: ((ByteArray) -> Unit)?=null,
    var loadFailure: ((String) -> Unit)?=null,
    val isImageRequest:Boolean =false
) {
    var id=createUniqueId()
        private set
    var data:ByteArray? = null


    private fun createUniqueId(): String {
        return UUID.randomUUID().toString()
    }


}