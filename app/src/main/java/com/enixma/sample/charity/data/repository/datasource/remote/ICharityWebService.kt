package com.enixma.sample.charity.data.repository.datasource.remote

import com.enixma.sample.charity.data.entity.CharityListEntity
import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.data.entity.DonationResultEntity
import io.reactivex.Observable
import retrofit2.http.*

interface ICharityWebService {

    @POST("{endpoint}")
    fun createDonation(@Path("endpoint") endPoint: String,
                       @Header("Accept") accept: String,
                       @Header("Content-Type") contentType: String,
                       @Body donationEntity: DonationEntity): Observable<DonationResultEntity>

    @GET("{endpoint}")
    fun getCharityList(@Path("endpoint") endPoint: String,
                       @Header("Accept") accept: String): Observable<CharityListEntity>

}