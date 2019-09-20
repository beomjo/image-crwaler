package k.bs.imagecrwaler.paging

import android.util.Log
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import k.bs.imagecrwaler.ImageItemVm
import k.bs.imagecrwaler.model.ModelImage
import k.bs.imagecrwaler.paging.base.BaseItemKeyDataSource
import org.jsoup.Jsoup

class ImageDataSource : BaseItemKeyDataSource<ImageItemVm>() {
    private val TAG = this::class.java.canonicalName
    override val initPreviousPageKey: Int = 1
    override var key: Int = 1

    override fun loadInitialData(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ImageItemVm>
    ) {
        Log.d(TAG, "loadInitialData params.requestedLoadSize ${params.requestedLoadSize}")

        scrap(key)
            .urlMapDataModel()
            .subscribe({ items ->
                submitInitialData(items, params, callback)
            }, {
                Log.e("bsjo", "bsjo error \n${it.message}")
            })
            .addTo(compositeDisposable)
    }

    override fun loadAdditionalData(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ImageItemVm>
    ) {
        Log.d(TAG, "loadAdditionalData params.key ${params.key}")
        Log.d(TAG, "loadAdditionalData params.requestedLoadSize ${params.requestedLoadSize}")

        scrap(++key)
            .urlMapDataModel()
            .subscribe({ items ->
                submitData(items, params, callback)
            }, {
                Log.e("bsjo", "bsjo error \n${it.message}")
            })
            .addTo(compositeDisposable)
    }

    private fun scrap(page: Int): Observable<String> {
        return Observable.create<String> { emitter ->
            val doc = Jsoup
                .connect(
                    " https://www.gettyimages.com/photos/collaboration?" +
                            "mediatype=photography&page=${page}&phrase=collaboration&sort=mostpopular"
                )
                .userAgent("Chrome")
                .get()

            val elements = doc.select(".search-content__gallery-assets").select("img")

            for (e in elements) {
                emitter.onNext(e.attr("src"))
            }
            emitter.onComplete()
        }
    }

    private fun Observable<String>.urlMapDataModel(): Maybe<List<ImageItemVm>> {
        return this.filter(String::isNotEmpty)
            .map { listOf(ImageItemVm(ModelImage(imageUrl = it))) }
            .reduce { t1: List<ImageItemVm>, t2: List<ImageItemVm> -> t1 + t2 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}