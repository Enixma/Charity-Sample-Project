package com.enixma.sample.charity.domain.validatecreditcard

import com.enixma.sample.charity.domain.UseCase

class ValidateCreditCardUseCaseResult(val status: ValidateCreditCardUseCase.STATUS): UseCase.Result{
    var errorList: List<ValidateCreditCardUseCase.ERROR> = ArrayList()
}