package com.enixma.sample.charity.presentation.charitylist

import android.arch.lifecycle.*
import android.util.Log
import android.widget.Toast
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCase
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCaseRequest
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCaseResult

class CharityListPresenter(private val view: CharityListContract.View,
                           private val lifecycleOwner: LifecycleOwner,
                           private val getCharityListUseCase: GetCharityListUseCase) : CharityListContract.Action {

    private var result: LiveData<GetCharityListUseCaseResult> = MutableLiveData()
    private var resultObserver: Observer<GetCharityListUseCaseResult>

    init {
        resultObserver = Observer<GetCharityListUseCaseResult> { result ->
            processGetCharityListResult(result)
        }
    }

    override fun getCharityList() {

        if (result.value?.status == GetCharityListUseCase.STATUS.LOADING) {
            return
        }

        result = LiveDataReactiveStreams
                .fromPublisher(getCharityListUseCase.execute(GetCharityListUseCaseRequest()))

        result.observe(lifecycleOwner, resultObserver)

    }

    private fun processGetCharityListResult(result: GetCharityListUseCaseResult?) {
        result?.let {
            when (result.status) {
                GetCharityListUseCase.STATUS.LOADING -> {
                    // do nothing
                }
                GetCharityListUseCase.STATUS.SUCCESS -> {
                    view.populateList(result.charityList)
                }
                else -> {
                    view.displayNoData()
                }
            }
        }
    }
}