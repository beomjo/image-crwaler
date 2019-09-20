package k.bs.imagecrwaler

import k.bs.imagecrwaler.model.ModelImage

class ImageItemVm(private val model: ModelImage) {
    val imageUrl get() = model.imageUrl
}