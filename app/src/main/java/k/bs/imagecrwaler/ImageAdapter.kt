package k.bs.imagecrwaler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import k.bs.imagecrwaler.paging.base.BaseDiffAdapter
import k.bs.imagecrwaler.paging.base.VIEW_TYPE_NORMAL

class ImageAdapter(
    @LayoutRes val layout: Int,
    private val bindingVariableId: Int? = null
) : BaseDiffAdapter<ImageItemVm, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL)
            ImageViewHolder(
                layout = layout,
                parent = parent,
                bindingVariableId = bindingVariableId
            )
        else LoadingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL)
            (holder as ImageViewHolder).onBind(getItem(position))
    }
}