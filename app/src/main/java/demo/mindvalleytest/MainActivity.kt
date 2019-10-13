package demo.mindvalleytest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.classic.mvloader.MVLoader
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image_recycler_view.layoutManager=LinearLayoutManager(this)
        var list:ArrayList<String> = ArrayList<String>()
        list.add("https://picsum.photos/id/1/200/300")
        list.add("https://picsum.photos/id/2/200/300")
        list.add("https://picsum.photos/id/1/5616/3744")
        list.add("https://picsum.photos/id/2/5616/3744")
        list.add("https://picsum.photos/id/3/5616/3744")
        list.add("https://picsum.photos/id/4/5616/3744")
        list.add("https://picsum.photos/id/5/5616/3744")
        list.add("https://picsum.photos/id/6/5616/3744")
        list.add("https://picsum.photos/id/7/5616/3744")
        list.add("https://picsum.photos/id/8/5616/3744")
        list.add("https://picsum.photos/id/9/5616/3744")
        list.add("https://picsum.photos/id/10/5616/3744")
        list.add("https://picsum.photos/id/11/5616/3744")
        list.add("https://picsum.photos/id/12/5616/3744")

        list.add("https://picsum.photos/id/1/5616/3744")
        list.add("https://picsum.photos/id/2/5616/3744")
        image_recycler_view.adapter=imageAdapter(list)


        var id :String? =MVLoader.getInstance(this).loadInto("https://pastebin.com/raw/wgkJgazE",{
            Log.d("amjad2",it.size.toString())
            textView.setText(String(it))
        },{
            Log.d("amjad2",it)
        })

        var id2 :String? =MVLoader.getInstance(this).loadInto("https://pastebin.com/raw/wgkJgazE",{
            Log.d("amjad2",it.size.toString())
            textView.setText(String(it))
        },{
            Log.d("amjad2",it)
        })

        if (id != null) {
            MVLoader.getInstance(this).cancelRequest("https://pastebin.com/raw/wgkJgazE",id)
        }


        button.setOnClickListener {

        }
    }
}
