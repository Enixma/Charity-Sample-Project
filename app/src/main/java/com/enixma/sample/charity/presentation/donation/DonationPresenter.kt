package com.enixma.sample.charity.presentation.donation

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import co.omise.android.models.Token
import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCase
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCaseRequest
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCaseResult
import io.reactivex.disposables.Disposable

class DonationPresenter(private val view: DonationContract.View,
                        private val viewModel: DonationViewModel,
                        private val createDonationUseCase: CreateDonationUseCase) : DonationContract.Action, LifecycleObserver {

    private var createDonationDisposable: Disposable? = null
    private var isLoading: Boolean = false

    override fun donate(token: Token) {
        if (isLoading) {
            return
        }
        isLoading = true

        val request = DonationEntity().let {
            it.name = viewModel.name
            it.token = token.id
            it.amount = Integer.parseInt(viewModel.amount.get())
            CreateDonationUseCaseRequest(it)
        }
        createDonationDisposable = createDonationUseCase.execute(request)
                .doOnNext { result -> processDonationResult(result) }
                .subscribe()
    }

    private fun processDonationResult(result: CreateDonationUseCaseResult) {
        isLoading = false
        if (result.status == CreateDonationUseCase.STATUS.SUCCESS) {
            view.goToSuccessScreen()
        } else {
            view.displayError(result.errorMessage)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onPresenterDestroy() {
        createDonationDisposable?.dispose()
    }
}