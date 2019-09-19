package k.bs.imagecrwaler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup


class MainActivity : AppCompatActivity() {

    private val disposeable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scrap()
            .filter(String::isNotEmpty)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("bsjo", "bsjo $it")
            }, {
                Log.e("bsjo", "bsjo error \n${it.message}")
            })
            .addTo(disposeable)
    }

    private fun scrap(): Observable<String> {
        return Observable.create<String> { emitter ->
            val doc = Jsoup
                .connect(" https://www.gettyimages.com/photos/collaboration?mediatype=photography&page=1&phrase=collaboration&sort=mostpopular")
                .userAgent("Chrome")
                .get()

            val elements = doc.select(".search-content__gallery-assets").select("img")

            for (e in elements) {
                emitter.onNext(e.attr("src"))
            }
            emitter.onComplete()
        }
    }

    override fun onDestroy() {
        disposeable.clear()
        super.onDestroy()
    }
}
