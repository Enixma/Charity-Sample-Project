package com.enixma.sample.charity

import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCase
import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCaseRequest
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class ValidateCreditCardUseCaseTest {

    lateinit var useCase: ValidateCreditCardUseCase

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = ValidateCreditCardUseCase()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenAllEmpty_expectAllEmptyErrors() {
        val request = ValidateCreditCardUseCaseRequest("", "", "")
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(ValidateCreditCardUseCase.STATUS.INVALID, result.status)
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CARD_NUMBER))
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_NAME))
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CVV))
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenCardEmpty_expectCardEmptyError() {
        val request = ValidateCreditCardUseCaseRequest("", "ABC", "123")
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(ValidateCreditCardUseCase.STATUS.INVALID, result.status)
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CARD_NUMBER))
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenCardAndNameEmpty_expectCardAndNameEmptyErrors() {
        val request = ValidateCreditCardUseCaseRequest("", "", "123")
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(ValidateCreditCardUseCase.STATUS.INVALID, result.status)
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CARD_NUMBER))
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_NAME))
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenCardAndCvvEmpty_expectCardAndCvvErrors() {
        val request = ValidateCreditCardUseCaseRequest("", "ABC", "")
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(ValidateCreditCardUseCase.STATUS.INVALID, result.status)
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CARD_NUMBER))
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CVV))
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenNameAndCvvEmpty_expectNameAndCvvErrors() {
        val request = ValidateCreditCardUseCaseRequest("123456", "", "")
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(ValidateCreditCardUseCase.STATUS.INVALID, result.status)
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.INVALID_LUHN))
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_NAME))
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CVV))
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenInvalidLuhn_expectLuhnError() {
        val request = ValidateCreditCardUseCaseRequest("123456", "ABC", "123")
        val result = useCase.execute(request).blockingSingle()
        Assert.assertEquals(ValidateCreditCardUseCase.STATUS.INVALID, result.status)
        Assert.assertTrue(result.errorList.contains(ValidateCreditCardUseCase.ERROR.INVALID_LUHN))
    }
}

