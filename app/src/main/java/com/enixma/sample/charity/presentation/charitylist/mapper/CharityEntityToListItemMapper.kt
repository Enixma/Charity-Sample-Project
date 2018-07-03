package com.enixma.sample.charity.presentation.charitylist.mapper

import com.enixma.sample.charity.data.entity.CharityEntity
import com.enixma.sample.charity.presentation.charitylist.CharityListItem


class CharityEntityToListItemMapper {
    companion object {
        fun map(charityEntity: CharityEntity): CharityListItem {
            return CharityListItem(charityEntity.id, charityEntity.name, charityEntity.logoUrl)
        }
    }
}