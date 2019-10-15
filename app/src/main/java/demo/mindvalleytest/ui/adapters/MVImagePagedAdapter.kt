package demo.mindvalleytest.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.classic.mvloader.MVLoader
import demo.mindvalleytest.R
import demo.mindvalleytest.data.models.local.MvImagesLocal
import demo.mindvalleytest.utilities.MVImageDiffCallBacks

class MVImagePagedAdapter(mvImageDiffCallBacks: MVImageDiffCallBacks) :PagedListAdapter<MvImagesLocal,MVImagePagedAdapter.MVImageViewHolder>(mvImageDiffCallBacks) {

    override fun onBindViewHolder(holder: MVImageViewHolder, position: Int) {
        val mvImagesLocal: MvImagesLocal? = getItem(position)

        // Note that "concert" is a placeholder if it's null.
        holder.bindTo(mvImagesLocal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVImageViewHolder =
        MVImageViewHolder(parent)



    class MVImageViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.image_list_item, parent, false)) {

        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        private val nameText=itemView.findViewById<TextView>(R.id.userName)
        var mvImagesLocal: MvImagesLocal? = null


        fun bindTo(mvImagesLocal: MvImagesLocal?) {
            this.mvImagesLocal = mvImagesLocal

            mvImagesLocal?.urls?.regular?.let {
                MVLoader.getInstance(imageView.context).loadInto(imageView, it)
            }

            nameText.text= mvImagesLocal?.user?.username ?: ""
        }

    }
}