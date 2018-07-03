package com.enixma.sample.charity.data.repository

import com.enixma.sample.charity.data.entity.CharityListEntity
import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.data.entity.DonationResultEntity
import com.enixma.sample.charity.data.repository.datasource.remote.ICharityCloudDataStore
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class CharityRepository constructor(private val charityCloudDataStore: ICharityCloudDataStore) : ICharityRepository {

    override fun getCharityList(): Observable<CharityListEntity> {
        return charityCloudDataStore.getCharityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .onErrorReturn { CharityListEntity() }
    }

    override fun createDonation(donationEntity: DonationEntity): Observable<DonationResultEntity> {
        return charityCloudDataStore.createDonation(donationEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .onErrorReturn { t: Throwable ->
                    val errorResult = t.let {
                        DonationResultEntity().apply {
                            this.statusCode = if (t is HttpException) t.code() else 500
                            this.isSuccess = false
                        }
                    }
                    errorResult
                }
    }
}
