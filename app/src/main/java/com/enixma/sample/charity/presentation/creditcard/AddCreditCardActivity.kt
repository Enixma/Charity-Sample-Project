package com.enixma.sample.charity.presentation.creditcard

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import co.omise.android.CardIO
import co.omise.android.CardNumber
import co.omise.android.models.Token
import com.enixma.sample.charity.R
import com.enixma.sample.charity.databinding.LayoutAddCreditCardActivityBinding
import com.enixma.sample.charity.domain.validatecreditcard.ValidateCreditCardUseCase
import com.enixma.sample.charity.presentation.creditcard.di.AddCreditCardModule
import com.enixma.sample.charity.presentation.creditcard.di.DaggerAddCreditCardComponent
import com.enixma.sample.charity.presentation.creditcard.mapper.ApiErrorMapper
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.layout_add_credit_card_activity.*
import javax.inject.Inject


class AddCreditCardActivity : AppCompatActivity(), AddCreditCardContract.View {

    private lateinit var binding: LayoutAddCreditCardActivityBinding
    private lateinit var viewModel: AddCreditCardViewModel
    private lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var presenter: AddCreditCardContract.Action

    companion object {
        const val REQUEST_CODE_CARD_IO = 1000
        const val RESULT_OK = 100
        const val RESULT_CANCEL = 200
        const val EXTRA_TOKEN = "AddCreditCardActivity.token"
        const val EXTRA_TOKEN_OBJECT = "AddCreditCardActivity.tokenObject"
        const val EXTRA_CARD_OBJECT = "AddCreditCardActivity.cardObject"

        fun getIntent(context: Context): Intent {
            return Intent(context, AddCreditCardActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AddCreditCardViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_add_credit_card_activity);
        binding.model = viewModel

        DaggerAddCreditCardComponent.builder()
                .addCreditCardModule(AddCreditCardModule(this, viewModel))
                .build().inject(this)

        setUpToolbar()
        setSecurityFlag()
        initProgressDialog()
        initExpiryMonthSpinner()
        initExpiryYearSpinner()
        binding.buttonSubmit.setOnClickListener {
            submit()
        }

        lifecycle.addObserver(presenter as AddCreditCardPresenter)
    }

    private fun setUpToolbar() {
        setTitle(R.string.title_add_credit_card)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }

    private fun initProgressDialog(){
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(getString(R.string.alert_message_validate_credit_card))
    }

    private fun setSecurityFlag() {
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCEL)
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_credit_card, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.getItem(0).isVisible = CardIO.isAvailable() && button_submit.isEnabled()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCEL)
            finish()
        } else if (item.itemId == R.id.menu_item_card_io) {
            if (CardIO.isAvailable()) {
                val intent = CardIO.buildIntent(this)
                startActivityForResult(intent, REQUEST_CODE_CARD_IO)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CARD_IO) {
            if (data == null || !data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                return
            }

            val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
            applyCardIOResult(scanResult)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initExpiryMonthSpinner() {
        binding.spinnerExpiryMonth.adapter = ExpiryMonthSpinnerAdapter()
    }

    private fun initExpiryYearSpinner() {
        binding.spinnerExpiryYear.adapter = ExpiryYearSpinnerAdapter()
    }

    private fun submit() {
        getExpiryDate()
        clearError()
        disableForm()
        presenter.validateCreditCard(getString(R.string.OMISE_KEY))
    }

    override fun proceedWithDonation(token: Token) {
        val data = Intent().apply {
            this.putExtra(EXTRA_TOKEN, token.id)
            this.putExtra(EXTRA_TOKEN_OBJECT, token)
            this.putExtra(EXTRA_CARD_OBJECT, token.card)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    private fun getExpiryDate() {
        viewModel.expiryMonth.set(binding.spinnerExpiryMonth.selectedItem as Int)
        viewModel.expiryYear.set(binding.spinnerExpiryYear.selectedItem as Int)
    }

    private fun clearError() {
        binding.editCardNumber.error = null
        binding.editCardName.error = null
        binding.editSecurityCode.error = null
        binding.textErrorMessage.visibility = View.GONE
        viewModel.errorMessage.set("")
    }

    override fun displayTokenRequestError(throwable: Throwable) {
        enableForm()
        ApiErrorMapper.getError(this, throwable).let {
            if (!it.isNullOrBlank()) {
                binding.textErrorMessage.visibility = View.VISIBLE
                viewModel.errorMessage.set(it)
            }
        }
    }

    override fun displayFieldError(errorList: List<ValidateCreditCardUseCase.ERROR>) {
        enableForm()
        if (errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CVV)) {
            binding.editSecurityCode.error = getString(R.string.error_required, binding.editSecurityCode.hint)
        }

        if (errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_NAME)) {
            binding.editCardName.error = getString(R.string.error_required, binding.editCardName.hint)
        }

        if (errorList.contains(ValidateCreditCardUseCase.ERROR.INVALID_LUHN)) {
            binding.editCardNumber.error = getString(R.string.error_invalid, binding.editCardNumber.hint)
        }

        if (errorList.contains(ValidateCreditCardUseCase.ERROR.EMPTY_CARD_NUMBER)) {
            binding.editCardNumber.error = getString(R.string.error_required, binding.editCardNumber.hint)
        }
    }

    private fun disableForm() {
        progressDialog.show()
        viewModel.isFormEnable.set(false)
        invalidateOptionsMenu()
    }

    private fun enableForm() {
        progressDialog.dismiss()
        viewModel.isFormEnable.set(true)
        invalidateOptionsMenu()
    }

    private fun applyCardIOResult(data: CreditCard) {
        val numberField = edit_card_number
        val nameField = edit_card_name
        val securityCodeField = edit_security_code

        if (data.cardNumber != null && !data.cardNumber.isEmpty()) {
            viewModel.cardNumber.set(CardNumber.format(data.cardNumber))
        }

        if (data.cardholderName != null && !data.cardholderName.isEmpty()) {
            viewModel.nameOnCard.set(data.cardholderName)
        }

        if (data.isExpiryValid) {
            val monthAdapter = binding.spinnerExpiryMonth.adapter as ExpiryMonthSpinnerAdapter
            binding.spinnerExpiryMonth.setSelection(monthAdapter.getPosition(data.expiryMonth))
            viewModel.expiryMonth.set(data.expiryMonth)

            val yearAdapter = binding.spinnerExpiryYear.adapter as ExpiryYearSpinnerAdapter
            binding.spinnerExpiryYear.setSelection(yearAdapter.getPosition(data.expiryYear))
            viewModel.expiryYear.set(data.expiryYear)
        }

        if (data.cvv != null && !data.cvv.isEmpty()) {
            viewModel.cvv.set(data.cvv)
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (viewModel.cardNumber.get().isNullOrBlank()) {
            binding.editCardNumber.requestFocus()
            imm.showSoftInput(numberField, InputMethodManager.SHOW_IMPLICIT)
        } else if (viewModel.nameOnCard.get().isNullOrBlank()) {
            binding.editCardName.requestFocus()
            imm.showSoftInput(nameField, InputMethodManager.SHOW_IMPLICIT)
        } else if (viewModel.cvv.get().isNullOrBlank()) {
            binding.editSecurityCode.requestFocus()
            imm.showSoftInput(securityCodeField, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}