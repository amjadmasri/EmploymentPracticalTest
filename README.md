# MindValleyPracticalTest
practical test for MindValley application process 
Usage :
1. To get the singleton instance :
    - Image ```var requestId =MVLoader.getInstance(context).loadInto(imageView,"your url here")```
    - File ``` var requestId =MVLoader.getInstance(context).loadInto("your url here",{ bytes ->
                jsonText.text= String(bytes)
            },{ message ->
                Toast.makeText(context,message,Toast.LENGTH_LONG).show()
            })```
            
    - Change maximum cache capacity  ``` MVLoader.getInstance(context).setMaximumCacheCapacity(newMaximumCacheCapacity)```
    - Cancel a requst ``` MVLoader.getInstance(context).cancelRequest("request url",requestId)  ```
2.  To use the Builder : ``` mvLoader:MVLoader =MVLoader.Builder(context)
                                                        .setMaxCacheCapacity(maxCacheCapacity)
                                                         .build()```
   
