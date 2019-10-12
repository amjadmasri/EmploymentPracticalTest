package com.classic.mvloader

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi


class MVLoader private constructor(context: Context) {
    companion object : SingletonHolder<MVLoader, Context>(::MVLoader)
    private var context:Context
    private var cacheManager:CacheManager

    /**
     * default constructor used for singleton instance
     * sets default values for managers
     */
    init {
        this.context=context.applicationContext
        this.cacheManager=LRUCacheManager()
    }

    /**
     * secondary constructor used by the builder to create instance with custom values
     */
    constructor(context: Context,cacheSize: Int) : this(context) {
        this.cacheManager=LRUCacheManager(cacheSize)
        this.context=context
    }

    /**
     * set the cache size for the cache manager
     * requires LOLLIPOP
     * if client is less than LOLLIPOP then use the builder to set the cache size when creating the instance
     * but keep in mind that the instance returned by the builder is not a singleton and each call to the builder
     * will return a new instance that the client code should handle
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCasheSize(cacheSize:Int) :MVLoader{
        cacheManager.resize(cacheSize)
        return this
    }


    /**
     * builder class for creating an instance of the MVLoader with custom configurations
     * each call to the builder will create a new instance of the MVLoader and the client should handle
     * keeping the instances singleton if needed
     */
    data class Builder(private var context :Context
    ,private var cacheSize: Int?=null){
        fun setCacheSize(cacheSize: Int)=apply{this.cacheSize=cacheSize}

        fun build():MVLoader{
            return MVLoader(context,cacheSize?:10)
        }
    }
}