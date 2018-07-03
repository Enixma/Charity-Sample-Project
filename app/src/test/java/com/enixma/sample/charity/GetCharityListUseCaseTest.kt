package com.enixma.sample.charity

import com.enixma.sample.charity.data.entity.CharityEntity
import com.enixma.sample.charity.data.entity.CharityListEntity
import com.enixma.sample.charity.data.repository.ICharityRepository
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCase
import com.enixma.sample.charity.domain.getcharitylist.GetCharityListUseCaseRequest
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.ArrayList

class GetCharityListUseCaseTest {

    @Mock
    var charityRepository: ICharityRepository? = null

    lateinit var useCase: GetCharityListUseCase

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = GetCharityListUseCase(charityRepository!!)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenOneEntityExists_expectSuccess() {
        val request = GetCharityListUseCaseRequest()

        val charityList = CharityListEntity()
        val charities = ArrayList<CharityEntity>()
        val charity = CharityEntity()
        charity.id = 1
        charities.add(charity)
        charityList.total = 1
        charityList.data = charities
        val observable = Observable.just(charityList)

        Mockito.`when`(charityRepository!!.getCharityList()).thenReturn(observable)
        val result = useCase.execute(request).blockingSingle()
        assertEquals(GetCharityListUseCase.STATUS.SUCCESS, result.status)
        assertEquals(1, result.charityList.size)
    }

    @Test
    @Throws(Exception::class)
    fun execute_whenNoDataExists_expectNoDataFound() {
        val request = GetCharityListUseCaseRequest()
        val charityList = CharityListEntity()
        val observable = Observable.just(charityList)

        Mockito.`when`(charityRepository!!.getCharityList()).thenReturn(observable)
        val result = useCase.execute(request).blockingSingle()
        assertEquals(GetCharityListUseCase.STATUS.NO_DATA_FOUND, result.status)
        assertEquals(0, result.charityList.size)
    }
}

