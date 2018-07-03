package com.enixma.sample.charity.domain.getcharitylist

import com.enixma.sample.charity.data.entity.CharityEntity
import com.enixma.sample.charity.domain.UseCase

class GetCharityListUseCaseResult(val status: GetCharityListUseCase.STATUS, val charityList: List<CharityEntity>) : UseCase.Result {

}