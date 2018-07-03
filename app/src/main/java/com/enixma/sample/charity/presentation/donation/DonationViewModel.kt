package com.enixma.sample.charity.presentation.donation

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

class DonationViewModel : ViewModel() {
    var name = ""
    var imageURL = ""
    var amount = ObservableField<String>("")
}

