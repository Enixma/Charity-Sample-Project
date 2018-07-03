package com.enixma.sample.charity.presentation.charitylist.di

import com.enixma.sample.charity.data.di.ServiceFactoryModule
import com.enixma.sample.charity.data.di.CharityDataModule
import com.enixma.sample.charity.presentation.charitylist.CharityListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceFactoryModule::class, CharityDataModule::class, CharityListModule::class])
interface CharityListComponent {
    fun inject(charityListFragment: CharityListFragment)
}