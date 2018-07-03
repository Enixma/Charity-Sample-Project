package com.enixma.sample.charity

import com.enixma.sample.charity.data.entity.DonationEntity
import com.enixma.sample.charity.data.entity.DonationResultEntity
import com.enixma.sample.charity.data.repository.ICharityRepository
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCase
import com.enixma.sample.charity.domain.createdonation.CreateDonationUseCaseRequest
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class CreateDonationUseCaseTest {

    var charityRepository: ICharityRepository = Mockito.mock(ICharityRepository::class.java)

    lateinit var useCase: CreateDonationUseCase

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = CreateDonationUseCase(charityRepository!!)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun execute_status201AndIsSuccess_expectSuccess() {
        val donation = DonationEntity().apply {
            this.name = "John"
            this.token = "token_test"
            this.amount = 100

        }
        val request = CreateDonationUseCaseRequest(donation)

        val resultObservable = DonationResultEntity().let {
            it.statusCode = 201
            it.isSuccess = true
            Observable.just(it)
        }

        Mockito.`when`(charityRepository.createDonation(donation)).thenReturn(resultObservable)
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(CreateDonationUseCase.STATUS.SUCCESS, result.status)
    }

    @Test
    @Throws(Exception::class)
    fun execute_status201AndIsNotSuccess_expectFail() {
        val donation = DonationEntity().apply {
            this.name = "John"
            this.token = "token_test"
            this.amount = 100

        }
        val request = CreateDonationUseCaseRequest(donation)

        val errorMessage = "insufficient funds"
        val resultObservable = DonationResultEntity().let {
            it.statusCode = 201
            it.isSuccess = false
            it.errorMessage = errorMessage
            Observable.just(it)
        }

        Mockito.`when`(charityRepository.createDonation(donation)).thenReturn(resultObservable)
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(CreateDonationUseCase.STATUS.FAIL, result.status)
        Assert.assertEquals(errorMessage, result.errorMessage)
    }

    @Test
    @Throws(Exception::class)
    fun execute_statusNot201_expectFail() {
        val donation = DonationEntity().apply {
            this.name = "John"
            this.token = "token_test"
            this.amount = 100

        }
        val request = CreateDonationUseCaseRequest(donation)

        val errorMessage = "insufficient funds"
        val resultObservable = DonationResultEntity().let {
            it.statusCode = 401
            it.isSuccess = false
            it.errorMessage = errorMessage
            Observable.just(it)
        }

        Mockito.`when`(charityRepository.createDonation(donation)).thenReturn(resultObservable)
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(CreateDonationUseCase.STATUS.FAIL, result.status)
        Assert.assertEquals(errorMessage, result.errorMessage)
    }
}
