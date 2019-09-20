package k.bs.imagecrwaler.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun imageLoad(imageView: ImageView, imageUrl: String?) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .into(imageView)
        }
    }
}
