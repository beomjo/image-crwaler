package k.bs.imagecrwaler

import k.bs.imagecrwaler.paging.ImageDataSourceFactory
import k.bs.imagecrwaler.paging.base.BasePaginationViewModel

class MainVm(val contract: Contract) :
    BasePaginationViewModel<ImageDataSourceFactory, ImageItemVm>() {
    interface Contract {
        fun toast(content: String)
    }

    override fun getPageSize(): Int = 60

    val adapter = ImageAdapter(R.layout.item_image, BR.vm)

    init {
        dataSourceFactory = ImageDataSourceFactory(getListener())
        submitItems()
        registerObserving()
    }


    private fun submitItems() {
        getItems()
            ?.subscribe(
                { items -> adapter.submitList(items) },
                { /** Error handle*/ }
            )
            .let { addDisposable(it!!) }
    }

    private fun registerObserving() {
        errorToastSubject.subscribe(
            { contract.toast("에러발생") },
            { /** Error handle*/ })
            .let(this::addDisposable)

        clearDataSubject.subscribe(
            {
                clearDataSource()
                submitItems()
                adapter.notifyDataSetChanged()
            }, {
                /** Error handle*/
            }).let(this::addDisposable)


        recyclerViewLoadingSubject
            .subscribe(
                { show -> show?.let { adapter.loading = show.peek() } },
                {}
            )
            .let(this::addDisposable)
    }


    fun clearDisposable() {
        cleared()
    }
}