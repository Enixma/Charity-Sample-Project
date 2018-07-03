package com.enixma.sample.charity.presentation.charitylist

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCase
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCaseRequest
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCaseResult
import io.reactivex.disposables.Disposable

class CharityListPresenter(private val view: CharityListContract.View,
                           private val getCharityListUseCase: GetCharityListUseCase) : CharityListContract.Action, LifecycleObserver {

    private var getCharityListDisposable: Disposable? = null
    private var isLoading: Boolean = false

    override fun getCharityList() {

        if (isLoading) {
            return
        }

        isLoading = true
        getCharityListDisposable = getCharityListUseCase.execute(GetCharityListUseCaseRequest())
                .doOnNext { result -> processGetCharityListResult(result) }
                .subscribe()
    }

    private fun processGetCharityListResult(result: GetCharityListUseCaseResult) {
        isLoading = false
        if (result.status == GetCharityListUseCase.STATUS.SUCCESS) {
            view.populateList(result.charityList)
        } else {
            view.displayNoData()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onPresenterDestroy() {
        getCharityListDisposable?.dispose()
    }
}