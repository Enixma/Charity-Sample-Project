package com.enixma.sample.charity.domain.createdonation

import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.domain.UseCase

class CreateDonationUseCaseRequest(val donationEntity: DonationEntity) : UseCase.Request {

}
