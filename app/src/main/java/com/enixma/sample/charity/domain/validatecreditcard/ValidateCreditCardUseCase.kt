package com.enixma.sample.charity.domain.validatecreditcard

import co.omise.android.CardNumber
import com.enixma.sample.charity.domain.UseCase
import io.reactivex.Flowable
import io.reactivex.Observable

class ValidateCreditCardUseCase : UseCase<ValidateCreditCardUseCaseRequest, ValidateCreditCardUseCaseResult> {

    enum class STATUS {
        VALID,
        INVALID
    }

    enum class ERROR {
        EMPTY_CARD_NUMBER,
        EMPTY_NAME,
        EMPTY_CVV,
        INVALID_LUHN
    }

    override fun execute(request: ValidateCreditCardUseCaseRequest): Flowable<ValidateCreditCardUseCaseResult> {

        val errorList = ArrayList<ERROR>()
        validateCard(request.cardNumber, errorList)
        validateName(request.nameOnCard, errorList)
        validateCVV(request.cvv, errorList)

        return errorList.let {
            val status = if (it.isEmpty()) STATUS.VALID else STATUS.INVALID
            Flowable.just(ValidateCreditCardUseCaseResult(status).apply { this.errorList = it })
        }
    }

    private fun validateCard(value: String, errorList: ArrayList<ERROR>) {
        if (value.isNullOrBlank()) {
            errorList.add(ERROR.EMPTY_CARD_NUMBER)
            return;
        }
        if (!CardNumber.luhn(value)) {
            errorList.add(ERROR.INVALID_LUHN)
        }
    }

    private fun validateName(value: String, errorList: ArrayList<ERROR>) {
        if (value.isNullOrBlank()) {
            errorList.add(ERROR.EMPTY_NAME)
        }
    }

    private fun validateCVV(value: String, errorList: ArrayList<ERROR>) {
        if (value.isNullOrBlank()) {
            errorList.add(ERROR.EMPTY_CVV)
        }
    }

    private fun normalize(number: String?): String {
        return number?.replace("[^0-9]".toRegex(), "") ?: ""
    }

    private fun isLuhn(value: String): Boolean {
        val number = normalize(value)
        val var1 = number.toCharArray()
        val var2 = IntArray(var1.size)

        var var3: Int
        var3 = 0
        while (var3 < var1.size) {
            var2[var3] = var1[var3].toInt() - 48
            ++var3
        }

        var3 = 0
        var var4 = 0

        var var5: Int
        var5 = var2.size - 1
        while (var5 >= 0) {
            var3 += var2[var5]
            var5 -= 2
        }

        var5 = var2.size - 2
        while (var5 >= 0) {
            var4 += var2[var5] * 2
            if (var2[var5] > 4) {
                var4 -= 9
            }
            var5 -= 2
        }

        return (var3 + var4) % 10 == 0
    }
}