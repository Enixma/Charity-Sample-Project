package com.enixma.sample.charity.presentation.creditcard

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import co.omise.android.Client
import co.omise.android.TokenRequest
import co.omise.android.TokenRequestListener
import co.omise.android.models.Token
import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCase
import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCaseRequest
import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCaseResult
import io.reactivex.disposables.Disposable


class AddCreditCardPresenter(private val view: AddCreditCardContract.View,
                             private val viewModel: AddCreditCardViewModel,
                             private val validateCreditCardUseCase: ValidateCreditCardUseCase) : AddCreditCardContract.Action, LifecycleObserver {

    private var validateCreditCardDisposable: Disposable? = null

    override fun validateCreditCard(pKey: String) {
        val request = ValidateCreditCardUseCaseRequest(
                viewModel.cardNumber.get(),
                viewModel.nameOnCard.get(),
                viewModel.cvv.get())

        validateCreditCardDisposable = validateCreditCardUseCase.execute(request)
                .doOnNext{ result ->
                    processValidationResult(result, pKey)
                }.subscribe()
    }

    private fun processValidationResult(result: ValidateCreditCardUseCaseResult, pKey: String){
        if(result.status == ValidateCreditCardUseCase.STATUS.VALID){
            requestToken(pKey)
        } else{
            view.displayFieldError(result.errorList)
        }
    }

    private fun requestToken(pKey: String) {
        val client = Client(pKey)
        val request = TokenRequest().apply {
            this.number = viewModel.cardNumber.get()
            this.name = viewModel.nameOnCard.get()
            this.expirationMonth = viewModel.expiryMonth.get()
            this.expirationYear = viewModel.expiryYear.get()
            this.securityCode = viewModel.cvv.get()
        }

        client.send(request, object : TokenRequestListener {
            override fun onTokenRequestSucceed(request: TokenRequest, token: Token) {
                view.proceedWithDonation(token)
            }

            override fun onTokenRequestFailed(request: TokenRequest, throwable: Throwable) {
                view.displayTokenRequestError(throwable)
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onPresenterDestroy() {
        validateCreditCardDisposable?.dispose()
    }
}