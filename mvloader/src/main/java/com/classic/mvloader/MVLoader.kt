package com.classic.mvloader

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.classic.mvloader.memoryCacheManagement.MVLoaderCacheManager
import com.classic.mvloader.requestsManagement.NetworkRequestManager
import com.classic.mvloader.utilities.SingletonHolder


class MVLoader private constructor(private var context: Context) {
    companion object : SingletonHolder<MVLoader, Context>(::MVLoader)

    private var networkRequestManager: NetworkRequestManager =
        NetworkRequestManager(
            MVLoaderCacheManager()
        )

    init {
        this.context = context.applicationContext
    }


    constructor(context: Context, cacheSize: Int) : this(context) {
        this.networkRequestManager = NetworkRequestManager(
            MVLoaderCacheManager(
                cacheSize
            )
        )
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCasheSize(cacheSize: Int): MVLoader {
        networkRequestManager.cache.resize(cacheSize)
        return this
    }

    fun loadInto(imageView: ImageView, url: String): String? {
        return networkRequestManager.requestImage(imageView, url)
    }

    fun loadInto(
        url: String,
        loadSuccess: (ByteArray) -> Unit,
        loadFailure: (String) -> Unit
    ): String? {
        return networkRequestManager.requestResource(url, loadSuccess, loadFailure)
    }

    fun cancelRequest(url: String, requestId: String) {
        networkRequestManager.cancelRequest(url, requestId)
    }


    class Builder(
        private var context: Context
        , private var cacheSize: Int? = null
    ) {
        fun setCacheSize(cacheSize: Int) = apply { this.cacheSize = cacheSize }

        fun build(): MVLoader {
            return MVLoader(context, cacheSize ?: 10)
        }
    }
}