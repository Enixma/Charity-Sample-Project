package com.enixma.sample.charity.presentation.charitylist.di

import android.arch.lifecycle.LifecycleOwner
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCase
import com.enixma.sample.charity.presentation.charitylist.CharityListContract
import com.enixma.sample.charity.presentation.charitylist.CharityListPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class CharityListModule(private val view: CharityListContract.View, private val lifecycleOwner: LifecycleOwner) {
    @Provides
    @Inject
    fun provideCharityListPresenter(getCharityListUseCase: GetCharityListUseCase): CharityListContract.Action {
        return CharityListPresenter(view, lifecycleOwner, getCharityListUseCase)
    }
}
