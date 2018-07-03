package com.enixma.sample.charity.presentation.donation.di

import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCase
import com.enixma.sample.charity.presentation.donation.DonationContract
import com.enixma.sample.charity.presentation.donation.DonationPresenter
import com.enixma.sample.charity.presentation.donation.DonationViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class DonationModule(private val view: DonationContract.View, private val viewModel: DonationViewModel) {
    @Provides
    @Inject
    fun provideDonationPresenter(createDonationUseCase: CreateDonationUseCase): DonationContract.Action {
        return DonationPresenter(view, viewModel, createDonationUseCase)
    }
}

