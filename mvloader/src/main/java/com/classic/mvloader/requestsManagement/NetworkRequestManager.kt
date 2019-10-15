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

    /**
     * checks the url in the image memory cache
     * if the image exists in the cache load it immediately in the image view
     * else create a new network request and return the new request's id to the client
     * @param imageView ImageView
     * @param url String
     * @return String?
     */
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

    /**
     * checks the url in the file memory cache
     * if the file ByteArray exists in the cache return it immediately
     * else create a new network request and return the new request's id to the client
     * @param url String
     * @param loadSuccess Function1<ByteArray, Unit>
     * @param loadFailure Function1<String, Unit>
     * @return String?
     */
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


    /**
     * when creating a new request check to see if there is an ongoing network request with the same resource URL
     * if there is an existing request add the new one to the list of existing requests to return with the result
     * else create a new network request and create a new list of requests associated with the URL
     * @param url String
     * @param request ResourceRequest
     */
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

    /**
     * enqueue a network request for the resource
     * if the call succeeds then add the result to the appropriate memory cache
     * then notify all requests waiting for this resource that they can get the resource from the cache
     * else if the call fails invoke the failure callback
     * @param resourceRequest ResourceRequest
     */
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

    /**
     * scale the bitmap down by sampling it to reduce size in memory
     * can throw exception if the data couldn't be decoded to a bitmap
     * @param byteArray ByteArray
     * @return Bitmap
     * @throws Exception
     */
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

    /**
     * calculate sample size
     * @param options Options
     * @param reqWidth Int
     * @param reqHeight Int
     * @return Int
     */
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

    /**
     * get list of waiting requests and notify the requests to load the data from the memory cache
     * @param url String
     */
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

    /**
     * remove a request from the list of waiting requests
     * @param resourceRequest ResourceRequest
     */
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

    /**
     * laod image bitmap to the image view on the main UI thread
     * @param imageView ImageView
     * @param data Bitmap
     */
    private fun setImageView(imageView: ImageView, data: Bitmap) {
        mainHandler.post {
            imageView.setImageBitmap(data)
        }
    }

    /**
     * invoke the success callback with the ByteArray data on the main UI thread
     * @param data ByteArray
     * @param loadSuccess Function1<ByteArray, Unit>
     */
    private fun invokeSuccessFileCallback(data: ByteArray, loadSuccess: (ByteArray) -> Unit) {
        mainHandler.post {
            loadSuccess.invoke(data)
        }
    }

    /**
     * remove a request from the list of waiting requests
     * if the canceled request is the last remaining one in the list
     * cancel the ongoing network call
     * @param url String
     * @param requestId String
     */
    fun cancelRequest(url: String, requestId: String) {
        val list: ArrayList<ResourceRequest>? = requestMap[url]
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

    /**
     * search for the queued or ongoing network call using its URL and then cancel it
     * @param url String
     */
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