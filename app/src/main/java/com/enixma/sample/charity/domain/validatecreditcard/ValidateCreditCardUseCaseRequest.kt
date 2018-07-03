package com.enixma.sample.charity.domain.validatecreditcard

import com.enixma.sample.charity.domain.UseCase

class ValidateCreditCardUseCaseRequest(val cardNumber: String, val nameOnCard: String, val cvv: String) : UseCase.Request {

}