package k.bs.imagecrwaler

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import k.bs.imagecrwaler.paging.BaseDiffAdapter

class ImageAdapter(
    @LayoutRes val layout: Int,
    private val bindingVariableId: Int? = null
) : BaseDiffAdapter<ImageItemVm, ImageViewHolder>() {

    private val items = mutableListOf<ImageItemVm>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            layout = layout,
            parent = parent,
            bindingVariableId = bindingVariableId
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    fun replaceAll(items: List<ImageItemVm>) {
        this.items.clear()
        this.items.addAll(items)

        notifyDataSetChanged()
    }

    fun insertAll(items: List<ImageItemVm>) {
        val sizeOld = itemCount

        this.items.addAll(items)

        val sizeNew = itemCount

        notifyItemRangeInserted(sizeOld, sizeNew)
    }

    fun add(item: ImageItemVm) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun clear() {
        val size = items.size
        this.items.clear()
        notifyItemRangeRemoved(0, size)
    }
}