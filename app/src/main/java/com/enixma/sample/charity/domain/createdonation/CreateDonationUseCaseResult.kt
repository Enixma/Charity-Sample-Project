package com.enixma.sample.charity.domain.createdonation

import com.enixma.sample.charity.domain.UseCase

class CreateDonationUseCaseResult(val status: CreateDonationUseCase.STATUS) : UseCase.Result {
    var errorMessage: String = ""
}