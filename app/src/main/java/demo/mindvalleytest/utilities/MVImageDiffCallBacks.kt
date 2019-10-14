package demo.mindvalleytest.utilities

import androidx.recyclerview.widget.DiffUtil
import demo.mindvalleytest.data.models.local.MvImagesLocal

class MVImageDiffCallBacks : DiffUtil.ItemCallback<MvImagesLocal>() {
    override fun areItemsTheSame(oldItem: MvImagesLocal, newItem: MvImagesLocal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun getChangePayload(oldItem: MvImagesLocal, newItem: MvImagesLocal): Any? {
        return super.getChangePayload(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: MvImagesLocal, newItem: MvImagesLocal): Boolean {
        return oldItem.equals(newItem)
    }
}