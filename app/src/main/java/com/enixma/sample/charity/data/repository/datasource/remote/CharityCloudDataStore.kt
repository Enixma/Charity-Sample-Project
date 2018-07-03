package com.enixma.sample.charity.data.repository.datasource.remote

import android.content.Context
import com.enixma.sample.charity.R
import com.enixma.sample.charity.data.entity.CharityListEntity
import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.data.entity.DonationResultEntity
import io.reactivex.Observable

class CharityCloudDataStore constructor(private val context: Context, private val charityWebService: ICharityWebService) : ICharityCloudDataStore {

    private var CHARITY_LIST_ENDPOINT = context.getString(R.string.CHARITY_LIST_ENDPOINT)
    private var DONATION_ENDPOINT = context.getString(R.string.DONATION_ENDPOINT)
    private var HEADER_ACCEPT = context.getString(R.string.HEADER_ACCEPT)
    private var HEADER_CONTENT_TYPE = context.getString(R.string.HEADER_CONTENT_TYPE)

    override fun getCharityList(): Observable<CharityListEntity> {
        return charityWebService.getCharityList(CHARITY_LIST_ENDPOINT, HEADER_ACCEPT)
    }

    override fun createDonation(donationEntity: DonationEntity): Observable<DonationResultEntity> {
        return charityWebService.createDonation(DONATION_ENDPOINT, HEADER_ACCEPT, HEADER_CONTENT_TYPE, donationEntity)
    }
}
