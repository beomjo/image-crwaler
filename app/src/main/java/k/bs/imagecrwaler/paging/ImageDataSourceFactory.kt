package k.bs.imagecrwaler.paging

import android.util.Log
import androidx.paging.DataSource
import k.bs.imagecrwaler.ImageItemVm
import k.bs.imagecrwaler.paging.base.OnDataSourceLoading

class ImageDataSourceFactory(
    private var loading: OnDataSourceLoading
) : DataSource.Factory<Int, ImageItemVm>() {

    lateinit var source: ImageDataSource

    override fun create(): DataSource<Int, ImageItemVm> {
        Log.d("${this::class.java.canonicalName} ", "ImageDataSourceFactory.create call ")

        source = ImageDataSource()
        source.onDataSourceLoading = loading
        return source
    }
}