package com.enixma.sample.charity.presentation.creditcard

import co.omise.android.models.Token
import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCase

interface AddCreditCardContract {
    interface View {
        fun displayTokenRequestError(throwable: Throwable)

        fun displayFieldError(errorList: List<ValidateCreditCardUseCase.ERROR>)

        fun proceedWithDonation(token: Token)
    }

    interface Action {
        fun validateCreditCard(pKey: String)
    }
}