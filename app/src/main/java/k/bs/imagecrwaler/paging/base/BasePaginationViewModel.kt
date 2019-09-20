package k.bs.imagecrwaler.paging.base

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BasePaginationViewModel<T : DataSource.Factory<Int, K>, K> {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected lateinit var dataSourceFactory: T
    private var pagedObservable: Observable<PagedList<K>>? = null

    val clearDataSubject = BehaviorSubject.create<Event<Unit>>()
    val recyclerViewLoadingSubject = BehaviorSubject.create<Event<Boolean>>()
    val errorToastSubject = BehaviorSubject.create<Event<Unit>>()

    abstract fun getPageSize(): Int

    fun clearData() {
        this.clearDataSubject.onNext(Event(Unit))
    }

    fun clearDataSource() {
        dataSourceFactory.create()
        createPagedObservable()
    }

    fun getItems(): Observable<PagedList<K>>? {
        if (pagedObservable == null) {
            createPagedObservable()
        }
        return pagedObservable
    }

    private fun createPagedObservable() {
        pagedObservable = RxPagedListBuilder(
            dataSourceFactory,
            PagedList.Config.Builder()
                .setPageSize(getPageSize())
                .setInitialLoadSizeHint(getPageSize() * 3) // default setInitialLoadSizeHint = pageSize*3
                .setEnablePlaceholders(false)
                .build()
        )
            .buildObservable()
    }

    protected fun getListener(): OnDataSourceLoading {
        return object : OnDataSourceLoading {
            override fun onFirstFetch() {
                showRecyclerLoading()
            }

            override fun onFirstFetchEndWithData() {
                hideRecyclerLoading()
            }

            override fun onFirstFetchEndWithoutData() {
                hideRecyclerLoading()
            }

            override fun onDataLoading() {
                showRecyclerLoading()
            }

            override fun onDataLoadingEnd() {
                hideRecyclerLoading()
            }

            override fun onInitialError() {
                hideRecyclerLoading()
                showErrorToast()
            }

            override fun onError() {
                hideRecyclerLoading()
                showErrorToast()
            }
        }
    }

    fun showRecyclerLoading() {
        recyclerViewLoadingSubject.onNext(Event(true))
    }

    fun hideRecyclerLoading() {
        recyclerViewLoadingSubject.onNext(Event(false))
    }

    fun showErrorToast() {
        errorToastSubject.onNext(Event(Unit))
    }

    fun addDisposable(d: Disposable) = compositeDisposable.add(d)

    fun cleared() {
        compositeDisposable.clear()
    }
}