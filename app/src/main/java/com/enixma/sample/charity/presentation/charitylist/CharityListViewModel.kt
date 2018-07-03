package com.enixma.sample.charity.presentation.charitylist

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

class CharityListViewModel : ViewModel() {
    var hasData = ObservableField<Boolean>(true)
}
