package com.enixma.sample.charity.data.repository

import com.enixma.sample.charity.data.entity.CharityListEntity
import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.data.entity.DonationResultEntity
import io.reactivex.Observable

interface ICharityRepository {

    fun getCharityList(): Observable<CharityListEntity>

    fun createDonation(donationEntity: DonationEntity): Observable<DonationResultEntity>

}