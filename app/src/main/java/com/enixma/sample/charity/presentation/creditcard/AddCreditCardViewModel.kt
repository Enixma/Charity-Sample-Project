package com.enixma.sample.charity.presentation.creditcard

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField

class AddCreditCardViewModel: ViewModel(){
    var cardNumber = ObservableField<String>("")
    var nameOnCard = ObservableField<String>("")
    var expiryMonth = ObservableField<Int>(0)
    var expiryYear = ObservableField<Int>(0)
    var cvv = ObservableField<String>("")
    var errorMessage = ObservableField<String>("")
    var isFormEnable = ObservableBoolean(true)
}
