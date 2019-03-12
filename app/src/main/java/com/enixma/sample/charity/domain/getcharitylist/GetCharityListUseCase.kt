package com.enixma.sample.charity.domain.getcharitylist

import com.enixma.sample.charity.data.repository.ICharityRepository
import com.enixma.sample.charity.domain.UseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class GetCharityListUseCase @Inject constructor(private val charityRepository: ICharityRepository) : UseCase<GetCharityListUseCaseRequest, GetCharityListUseCaseResult> {

    enum class STATUS {
        LOADING,
        SUCCESS,
        NO_DATA_FOUND
    }

    override fun execute(request: GetCharityListUseCaseRequest): Flowable<GetCharityListUseCaseResult> {
        return Flowable.just(GetCharityListUseCaseResult(STATUS.LOADING, mutableListOf())).concatWith(charityRepository.getCharityList()
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap {
                    val result = it.let {
                        val status = if (it.total <= 0) STATUS.NO_DATA_FOUND else STATUS.SUCCESS
                        val charityList = it.data
                        GetCharityListUseCaseResult(status, charityList)
                    }
                    Flowable.just(result)
                })
    }
}