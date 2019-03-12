package com.enixma.sample.charity.domain.createdonation

import com.enixma.sample.charity.data.repository.ICharityRepository
import com.enixma.sample.charity.domain.UseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class CreateDonationUseCase @Inject constructor(private val charityRepository: ICharityRepository) : UseCase<CreateDonationUseCaseRequest, CreateDonationUseCaseResult> {

    enum class STATUS {
        SUCCESS,
        FAIL
    }

    override fun execute(request: CreateDonationUseCaseRequest): Flowable<CreateDonationUseCaseResult> {
        return charityRepository.createDonation(request.donationEntity)
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap {
                    val result = it.let {
                        val status = if (it.statusCode == 201 && it.isSuccess) STATUS.SUCCESS else STATUS.FAIL
                        CreateDonationUseCaseResult(status).apply {
                            if (this.status == STATUS.FAIL) this.errorMessage = it.errorMessage
                        }
                    }
                    Flowable.just(result)
                }
    }
}