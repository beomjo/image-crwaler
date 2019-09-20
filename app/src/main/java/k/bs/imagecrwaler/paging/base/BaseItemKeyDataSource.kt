package k.bs.imagecrwaler.paging.base

import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseItemKeyDataSource<T> : PageKeyedDataSource<Int, T>() {
    lateinit var onDataSourceLoading: OnDataSourceLoading
    protected var compositeDisposable = CompositeDisposable()

    abstract val initPreviousPageKey: Int
    abstract var key: Int

    protected abstract fun loadInitialData(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    )

    protected abstract fun loadAdditionalData(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, T>
    )

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        onDataSourceLoading.onFirstFetch()
        loadInitialData(params, callback)
    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        onDataSourceLoading.onDataLoading()
        loadAdditionalData(params, callback)
    }

    protected fun submitInitialData(
        items: List<T>,
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        callback.onResult(items, initPreviousPageKey, params.requestedLoadSize)
        if (items.isNotEmpty()) {
            onDataSourceLoading.onFirstFetchEndWithData()
        } else {
            onDataSourceLoading.onFirstFetchEndWithoutData()
        }
    }

    protected fun submitData(
        items: List<T>,
        params: LoadParams<Int>,
        callback: LoadCallback<Int, T>
    ) {
        /** adjacentPageKey: 후속 페이지로드 (이전 페이지 입력 / 다음 페이지 입력 ) 또는 현재로드 방향으로로드 할 페이지가 더 이상없는 경우 키입니다 */
        val adjacentPageKey = if (items.isEmpty()) null else params.key + items.size
        callback.onResult(items, adjacentPageKey)
        onDataSourceLoading.onDataLoadingEnd()
    }


    protected fun submitInitialError(error: Throwable) {
        onDataSourceLoading.onError()
        error.printStackTrace()
    }


    protected fun submitError(error: Throwable) {
        onDataSourceLoading.onError()
        error.printStackTrace()
    }

    override fun invalidate() {
        compositeDisposable.dispose()
        super.invalidate()
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

}