package com.enixma.sample.charity.presentation.donation

import android.arch.lifecycle.*
import co.omise.android.models.Token
import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCase
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCaseRequest
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCaseResult

class DonationPresenter(private val view: DonationContract.View,
                        private val lifecycleOwner: LifecycleOwner,
                        private val viewModel: DonationViewModel,
                        private val createDonationUseCase: CreateDonationUseCase) : DonationContract.Action {

    private var result: LiveData<CreateDonationUseCaseResult> = MutableLiveData()
    private var resultObserver: Observer<CreateDonationUseCaseResult>

    init {
        resultObserver = Observer<CreateDonationUseCaseResult> { result ->
            processDonationResult(result)
        }
    }

    override fun donate(token: Token) {

        if(result.value?.status == CreateDonationUseCase.STATUS.LOADING){
            return
        }

        val request = DonationEntity().let {
            it.name = viewModel.name
            it.token = token.id ?: ""
            it.amount = Integer.parseInt(viewModel.amount.get())
            CreateDonationUseCaseRequest(it)
        }

        result = LiveDataReactiveStreams.fromPublisher(createDonationUseCase.execute(request))
        result.observe(lifecycleOwner, resultObserver)
    }

    private fun processDonationResult(result: CreateDonationUseCaseResult?) {
        result?.let {
            when(it.status) {
                CreateDonationUseCase.STATUS.LOADING -> {
                    view.displayLoading()
                }
                CreateDonationUseCase.STATUS.SUCCESS -> {
                    view.dismissLoading()
                    view.goToSuccessScreen()
                }
                else -> {
                    view.dismissLoading()
                    view.displayError(it.errorMessage)
                }
            }
        }
    }
}