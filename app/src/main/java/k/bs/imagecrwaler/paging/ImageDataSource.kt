package k.bs.imagecrwaler.paging

import android.util.Log
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import k.bs.imagecrwaler.ImageItemVm
import k.bs.imagecrwaler.model.ModelImage
import k.bs.imagecrwaler.paging.base.BasePagedKeyDataSource
import org.jsoup.Jsoup

class ImageDataSource : BasePagedKeyDataSource<ImageItemVm>() {
    private val TAG = this::class.java.canonicalName
    override var startPageKey: Int = 1

    override fun loadInitialData(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ImageItemVm>
    ) {
        scrap(startPageKey)
            .urlMapDataModel()
            .subscribe({ items ->
                submitInitialData(items, callback)
            }, {
                Log.e(TAG, "error \n${it.message}")
            })
            .addTo(compositeDisposable)
    }

    override fun loadAdditionalData(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ImageItemVm>
    ) {
        scrap(params.key)
            .urlMapDataModel()
            .subscribe({ items ->
                submitData(items, params, callback)
            }, {
                Log.e(TAG, "error \n${it.message}")
            })
            .addTo(compositeDisposable)
    }

    private fun scrap(page: Int): Observable<String> {
        return Observable.create<String> { emitter ->
            val doc = Jsoup
                .connect(
                    "https://www.gettyimages.com/photos/collaboration?" +
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