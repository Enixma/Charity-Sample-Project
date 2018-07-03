package com.enixma.sample.charity.data.di

import android.content.Context
import com.enixma.sample.charity.data.repository.ICharityRepository
import com.enixma.sample.charity.data.repository.CharityRepository
import com.enixma.sample.charity.data.repository.datasource.remote.ICharityCloudDataStore
import com.enixma.sample.charity.data.repository.datasource.remote.ICharityWebService
import com.enixma.sample.charity.data.repository.datasource.remote.CharityCloudDataStore
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Module
class CharityDataModule constructor(private val context: Context) {

    @Provides
    @Singleton
    @Inject
    internal fun provideCharityWebService(restAdapter: Retrofit): ICharityWebService {
        return restAdapter.create<ICharityWebService>(ICharityWebService::class.java)
    }

    @Provides
    @Singleton
    @Inject
    internal fun provideCharityCloudDataStore(charityWebService: ICharityWebService): ICharityCloudDataStore {
        return CharityCloudDataStore(context, charityWebService)
    }

    @Provides
    @Singleton
    @Inject
    internal fun provideCharityRepository(charityCloudDataStore: ICharityCloudDataStore): ICharityRepository {
        return CharityRepository(charityCloudDataStore)
    }
}