package com.enixma.sample.charity.presentation.charitylist

import com.enixma.sample.charity.data.entity.CharityEntity


interface CharityListContract {
    interface View {
        fun populateList(charityList: List<CharityEntity>)

        fun displayNoData()
    }

    interface Action {
        fun getCharityList()
    }
}