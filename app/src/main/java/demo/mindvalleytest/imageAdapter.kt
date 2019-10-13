package demo.mindvalleytest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.classic.mvloader.MVLoader
import kotlinx.android.synthetic.main.image_list_item.view.*

class imageAdapter (val items : ArrayList<String>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(items[position])
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder (var view: View) : RecyclerView.ViewHolder(view) {

    fun bind(url:String){
        MVLoader.getInstance(view.context).loadInto(view.imageView,url)
    }
}