package k.bs.imagecrwaler

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import k.bs.imagecrwaler.model.ModelImage
import org.jsoup.Jsoup

class MainVm {
    private val disposable = CompositeDisposable()
    val adapter = ImageAdapter(R.layout.item_image, BR.vm)

    init {

        scrap()
            .filter(String::isNotEmpty)
            .map { listOf(ImageItemVm(ModelImage(imageUrl = it))) }
            .reduce { t1: List<ImageItemVm>, t2: List<ImageItemVm> -> t1 + t2 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                Log.d("bsjo","bsjo ${items}")
                adapter.insertAll(items)
            }, {
                Log.e("bsjo", "bsjo error \n${it.message}")
            })
            .addTo(disposable)

    }

    private fun scrap(): Observable<String> {
        return Observable.create<String> { emitter ->
            val doc = Jsoup
                .connect(
                    " https://www.gettyimages.com/photos/collaboration?" +
                            "mediatype=photography&page=1&phrase=collaboration&sort=mostpopular"
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

    fun clearDisposable() {
        disposable.clear()
    }
}