package com.enixma.sample.charity.presentation.donation.di

import com.enixma.sample.charity.data.di.ServiceFactoryModule
import com.enixma.sample.charity.data.di.CharityDataModule
import com.enixma.sample.charity.presentation.donation.DonationFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceFactoryModule::class, CharityDataModule::class, DonationModule::class])
interface DonationComponent {
    fun inject(donationFragment: DonationFragment)
}