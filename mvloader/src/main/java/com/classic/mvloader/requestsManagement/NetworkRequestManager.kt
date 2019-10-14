package com.classic.mvloader.requestsManagement

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Patterns
import android.widget.ImageView
import com.classic.mvloader.memoryCacheManagement.MVLoaderCacheManager
import okhttp3.*
import java.io.IOException
import java.lang.ref.WeakReference


class NetworkRequestManager(val cache: MVLoaderCacheManager) :
    RequestsManager {
    private val requestMap: HashMap<String, ArrayList<ResourceRequest>> = HashMap()
    var mainHandler: android.os.Handler = android.os.Handler(Looper.getMainLooper())
    var networkClient = OkHttpClient()

    override fun requestImage(imageView: ImageView, url: String): String? {


        require(url.isNotEmpty()) {
            "MVLoader:requestImage - Image Url should not be empty"
        }

        require(Patterns.WEB_URL.matcher(url).matches()) {
            "MVLoader:requestImage - resource Url should be a valid URL "
        }
        val bitmap = checkImageInCache(url)
        bitmap?.let {
            setImageView(imageView, cache.imageCache.get(url)!!)
            return null
        } ?: run {
            val request = ResourceRequest(
                url,
                WeakReference<ImageView>(imageView),
                isImageRequest = true
            )
            handleRequest(url, request)
            return request.id
        }
    }

    override fun requestResource(
        url: String,
        loadSuccess: (ByteArray) -> Unit,
        loadFailure: (String) -> Unit
    ): String? {

        require(url.isNotEmpty()) {
            "MVLoader:requestImage - resource Url should not be empty"
        }

        require(Patterns.WEB_URL.matcher(url).matches()) {
            "MVLoader:requestImage - resource Url should be a valid URL "
        }

        val byteArray = checkFileInCache(url)
        byteArray?.let {
            invokeSuccessFileCallback(byteArray, loadSuccess)
            return null
        } ?: run {
            val request = ResourceRequest(
                url,
                loadSuccess = loadSuccess,
                loadFailure = loadFailure
            )
            handleRequest(url, request)
            return request.id
        }
    }

    @Synchronized
    private fun checkImageInCache(imageUrl: String): Bitmap? = cache.imageCache.get(imageUrl)

    @Synchronized
    private fun checkFileInCache(url: String): ByteArray? = cache.cache.get(url)

    private fun invokeSuccessFileCallback(data: ByteArray, loadSuccess: (ByteArray) -> Unit) {
        mainHandler.post {
            loadSuccess.invoke(data)
        }
    }

    private fun handleRequest(url: String, request: ResourceRequest) {
        val list: ArrayList<ResourceRequest>? = requestMap[url]
        if (list == null) {
            val newList = ArrayList<ResourceRequest>()
            newList.add(request)
            requestMap[url] = newList
            requestResource(request)
        } else {
            list.add(request)
        }
    }

    private fun requestResource(resourceRequest: ResourceRequest) {
        val request = Request.Builder()
            .url(resourceRequest.url)
            .tag(resourceRequest.id)
            .build()

        val call = networkClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                removeRequestFromMap(resourceRequest)
                resourceRequest.loadFailure?.invoke(e.localizedMessage!!)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        if (resourceRequest.isImageRequest)
                            response.body?.bytes()?.let {
                                cache.imageCache.set(
                                    resourceRequest.url,
                                    scaleBitmapForLoad(it)
                                )
                            }
                        else {
                            response.body?.bytes()?.let { cache.cache.set(resourceRequest.url, it) }
                        }
                        notifySuccessRequest(resourceRequest.url)
                    }
                } catch (exception: Exception) {
                    removeRequestFromMap(resourceRequest)
                    resourceRequest.loadFailure?.invoke(exception.message!!)
                }

            }
        })
    }

    @Throws(Exception::class)
    private fun scaleBitmapForLoad(byteArray: ByteArray): Bitmap {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            byteArray.size.let { BitmapFactory.decodeByteArray(byteArray, 0, it, options) }


            options.inSampleSize = calculateInSampleSize(options, 500, 500)
            options.inJustDecodeBounds = false

            return byteArray.size.let { BitmapFactory.decodeByteArray(byteArray, 0, it, options) }
        } catch (exception: Exception) {
            throw Exception("failed to decode bitmap")
        }

    }

    private fun notifySuccessRequest(url: String) {
        val list = requestMap[url]
        if (list != null) {
            val itr = list.iterator()
            while (itr.hasNext()) {
                val x = itr.next()
                if (x.isImageRequest) {
                    x.imageView?.get()?.let { requestImage(imageView = it, url = x.url) }
                } else {
                    x.loadSuccess?.let { requestResource(url, it, x.loadFailure!!) }
                }

            }

            requestMap.remove(url)
        }
    }

    private fun removeRequestFromMap(resourceRequest: ResourceRequest) {
        val list: ArrayList<ResourceRequest>? = requestMap[resourceRequest.url]
        if (list != null) {
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

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {

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
        var list: ArrayList<ResourceRequest>? = requestMap[url]
        if (list != null) {
            val itr = list.iterator()
            while (itr.hasNext()) {
                val x = itr.next()
                if (x.id == requestId)
                    itr.remove()
            }
            if (list.size == 0) {
                cancelNetworkCall(url)
            }
        }
    }

    private fun cancelNetworkCall(url: String) {
        for (call in networkClient.dispatcher.queuedCalls()) {
            if (call.request().url.toString() == url) {
                call.cancel()
            }
        }


        for (call in networkClient.dispatcher.runningCalls()) {

            if (call.request().url.toString() == url) {
                call.cancel()
            }
        }
    }

}