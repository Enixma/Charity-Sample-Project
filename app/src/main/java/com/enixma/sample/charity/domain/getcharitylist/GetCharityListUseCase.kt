package com.enixma.sample.charity.domain.getcharitylist

import com.enixma.sample.charity.data.repository.ICharityRepository
import com.enixma.sample.charity.domain.UseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetCharityListUseCase @Inject constructor(private val charityRepository: ICharityRepository) : UseCase<GetCharityListUseCaseRequest, GetCharityListUseCaseResult> {

    enum class STATUS {
        SUCCESS,
        NO_DATA_FOUND
    }

    override fun execute(request: GetCharityListUseCaseRequest): Observable<GetCharityListUseCaseResult> {
        return charityRepository.getCharityList()
                .flatMap {
                    val result = it.let {
                        val status = if (it.total <= 0) STATUS.NO_DATA_FOUND else STATUS.SUCCESS
                        val charityList = it.data
                        GetCharityListUseCaseResult(status, charityList)
                    }
                    Observable.just(result)
                }
    }
}