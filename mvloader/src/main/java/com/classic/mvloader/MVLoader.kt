package com.classic.mvloader

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi


class MVLoader private constructor(context: Context) {
    companion object : SingletonHolder<MVLoader, Context>(::MVLoader)
    constructor(context: Context,cacheSize: Int) : this(context) {
        this.cacheSize=cacheSize
        this.context=context
    }

    private var context:Context
    private var cacheSize:Int

    init {
        this.context=context.applicationContext
        cacheSize=10


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCasheSize(cacheSize:Int) :MVLoader{
        this.cacheSize=cacheSize
        return this
    }


    data class Builder(private var context :Context
    ,private var cacheSize: Int?=null){
        fun setCacheSize(cacheSize: Int)=apply{this.cacheSize=cacheSize}

        fun build():MVLoader{
            return MVLoader(context,cacheSize?:10)
        }
    }
}