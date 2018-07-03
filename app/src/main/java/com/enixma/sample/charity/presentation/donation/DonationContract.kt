package com.enixma.sample.charity.presentation.donation

import co.omise.android.models.Token

interface DonationContract {
    interface View {
        fun displayError(errorMessage: String)

        fun goToSuccessScreen()
    }

    interface Action {
        fun donate(token: Token)
    }
}