package com.enixma.sample.charity.presentation.creditcard.di

import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCase
import com.enixma.sample.charity.presentation.creditcard.AddCreditCardContract
import com.enixma.sample.charity.presentation.creditcard.AddCreditCardPresenter
import com.enixma.sample.charity.presentation.creditcard.AddCreditCardViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class AddCreditCardModule(private val view: AddCreditCardContract.View, private val viewModel: AddCreditCardViewModel) {
    @Provides
    @Inject
    fun provideAddCreditCardPresenter(): AddCreditCardContract.Action {
        return AddCreditCardPresenter(view, viewModel, ValidateCreditCardUseCase())
    }
}
