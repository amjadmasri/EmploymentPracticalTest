package com.classic.mvloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.os.Looper
import android.util.Log
import okhttp3.*
import java.io.IOException
import java.lang.ref.WeakReference
import android.util.Patterns


class NetworkRequestManager(val cache:MVLoaderCacheManager):RequestsManager {
    val requestMap:HashMap<String,ArrayList<ResourceRequest>> =HashMap<String,ArrayList<ResourceRequest>>()
    var mainHandler: android.os.Handler = android.os.Handler(Looper.getMainLooper())
    var networkClient = OkHttpClient()

    fun loadInto(imageView: ImageView,url:String):String?{

        require(imageView != null) {
            "MVLoader:loadInto - ImageView should not be null."
        }

        require(url != null && url.isNotEmpty() ) {
            "MVLoader:loadInto - Image Url should not be empty"
        }

        require(Patterns.WEB_URL.matcher(url).matches()){
            "MVLoader:loadInto - resource Url should be a valid URL "
        }
        val bitmap = checkImageInCache(url)
        bitmap?.let {
            setImageView(imageView, cache.imageCache.get(url)!!)
            return null
        } ?: run {
            val request=ResourceRequest(url,WeakReference<ImageView>(imageView),isImageRequest = true)
            handleRequest(url,request)
            return request.id
        }
    }

    fun loadInto(url: String,loadSuccess:(ByteArray)->Unit,loadFailure:(String)->Unit):String?{

        require(url != null && url.isNotEmpty()) {
            "MVLoader:loadInto - resource Url should not be empty"
        }

        require(Patterns.WEB_URL.matcher(url).matches()){
            "MVLoader:loadInto - resource Url should be a valid URL "
        }

        val byteArray = checkFileInCache(url)
        byteArray?.let {
            invokeSuccessFileCallback(byteArray,loadSuccess)
            return null
        } ?: run {
            val request=ResourceRequest(url,loadSuccess = loadSuccess,loadFailure = loadFailure)
            handleRequest(url,request)
            return request.id
        }
    }

    @Synchronized
    private fun checkImageInCache(imageUrl: String): Bitmap? = cache.imageCache.get(imageUrl)

    @Synchronized
    private fun checkFileInCache(url: String): ByteArray? = cache.cache.get(url)

    private fun invokeSuccessFileCallback(data: ByteArray, loadSuccess:(ByteArray)->Unit) {
        mainHandler.post {
            loadSuccess.invoke(data)
        }
    }

    private fun handleRequest(url: String, request: ResourceRequest) {
        val list :ArrayList<ResourceRequest>? = requestMap[url]
        if(list==null){
            val newList=ArrayList<ResourceRequest>()
            newList.add(request)
            requestMap[url] = newList
            requestResource(request)
        }
        else{
            list.add(request)
        }
    }

    private fun requestResource(resourceRequest: ResourceRequest) {
        Log.d("TAG","loading resource ")
        val request = Request.Builder()
            .url(resourceRequest.url)
            .tag(resourceRequest.id)
            .build()

        val call = networkClient.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TAG","failed ")
                removeRequestFromMap(resourceRequest)
                resourceRequest.loadFailure?.invoke(e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("TAG","success for "+resourceRequest.url)
                if(response.isSuccessful) {
                    if(resourceRequest.isImageRequest)
                        response.body?.bytes()?.let { cache.imageCache.set(resourceRequest.url, scaleBitmapForLoad(it)) }
                    else{
                        response.body?.bytes()?.let { cache.cache.set(resourceRequest.url, it) }
                    }
                    notifySuccessRequest(resourceRequest.url)
                }
            }
        })
    }

    private fun scaleBitmapForLoad(byteArray: ByteArray): Bitmap {
        Log.d("Thread",Thread.currentThread().toString())
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        byteArray.size.let {BitmapFactory.decodeByteArray(byteArray,0, it, options) }


        options.inSampleSize = calculateInSampleSize(options, 500, 500)
        options.inJustDecodeBounds = false

        return byteArray.size.let { BitmapFactory.decodeByteArray(byteArray, 0, it, options) }
    }

    private fun notifySuccessRequest(url: String) {
        val list = requestMap[url]
        if(list!=null) {
            val itr = list.iterator()
            while (itr.hasNext()) {
                val x = itr.next()
                if (x.isImageRequest) {
                    x.imageView?.get()?.let { loadInto(imageView = it,url = x.url) }
                } else {
                    x.loadSuccess?.let { loadInto(url, it, x.loadFailure!!) }
                }

            }

            requestMap.remove(url)
        }
    }

    private fun removeRequestFromMap(resourceRequest: ResourceRequest) {
        val list:ArrayList<ResourceRequest>? =requestMap[resourceRequest.url]
        if(list!=null) {
            val itr = list.iterator()
            while (itr.hasNext()) {
                val x = itr.next()
                if (x.id == resourceRequest.id)
                    itr.remove()
            }
        }
    }

    private fun setImageView(imageView: ImageView, data: Bitmap) {
        mainHandler.post {
             imageView.setImageBitmap(data)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) inSampleSize *= 2
        }

        return inSampleSize
    }


    fun cancelRequest(url: String, requestId: String) {
        Log.d("tag","canceling request "+requestId)
        var list:ArrayList<ResourceRequest>? =requestMap[url]
        Log.d("Tag","array list sizze "+ (list?.size ?: -1))
        if(list!=null) {
            val itr = list.iterator()
            while (itr.hasNext()) {
                val x = itr.next()
                if (x.id == requestId)
                    itr.remove()
            }
            if(list.size==0) {
                cancelNetworkCall(url)
            }
        }
    }

    private fun cancelNetworkCall(url: String) {
        for (call in networkClient.dispatcher.queuedCalls()) {
            Log.d("TAG","looping queued ")
            if (call.request().url.toString() == url) {
                Log.d("TAG","call queued canceled")
                call.cancel()
            }
        }


        for (call in networkClient.dispatcher.runningCalls()) {
            Log.d("TAG","looping running "+call.request().url +" my tag "+url)
            if (call.request().url.toString()==url) {
                Log.d("TAG","call running canceled")
                call.cancel()
            }
        }
    }

}