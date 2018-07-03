package com.enixma.sample.charity.presentation.creditcard.di

import com.enixma.sample.charity.presentation.creditcard.AddCreditCardActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AddCreditCardModule::class])
interface AddCreditCardComponent {
    fun inject(addCreditCardActivity: AddCreditCardActivity)
}