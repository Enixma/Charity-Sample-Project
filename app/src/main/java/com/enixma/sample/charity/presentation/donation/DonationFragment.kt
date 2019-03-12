package com.enixma.sample.charity.presentation.donation

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omise.android.models.Token
import com.bumptech.glide.Glide
import com.enixma.sample.charity.BuildConfig
import com.enixma.sample.charity.R
import com.enixma.sample.charity.data.di.ServiceFactoryModule
import com.enixma.sample.charity.data.di.CharityDataModule
import com.enixma.sample.charity.databinding.LayoutDonationFragmentBinding
import com.enixma.sample.charity.presentation.creditcard.AddCreditCardActivity
import com.enixma.sample.charity.presentation.donation.di.DaggerDonationComponent
import com.enixma.sample.charity.presentation.donation.di.DonationModule
import com.enixma.sample.charity.presentation.success.SuccessActivity
import org.json.JSONObject
import javax.inject.Inject


class DonationFragment : Fragment(), DonationContract.View {

    private lateinit var binding: LayoutDonationFragmentBinding
    private lateinit var viewModel: DonationViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var amountArray: Array<String>
    private var alertDialog: AlertDialog? = null

    @Inject
    lateinit var presenter: DonationContract.Action

    companion object {
        private const val REQUEST_CC = 100
        private const val REQUEST_FINAL = 200
        const val NAME = "name"
        const val IMAGE_URL = "image"

        fun newInstance(name: String, imageUrl: String): DonationFragment {
            val fragment = DonationFragment()
            val bundle = Bundle()
            bundle.putString(NAME, name)
            bundle.putString(IMAGE_URL, imageUrl)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DonationViewModel::class.java)
        viewModel.name = arguments.getString(NAME)
        viewModel.imageURL = arguments.getString(IMAGE_URL)

        DaggerDonationComponent.builder()
                .serviceFactoryModule(ServiceFactoryModule(activity))
                .charityDataModule(CharityDataModule(activity))
                .donationModule(DonationModule(this, this, viewModel))
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater!!, R.layout.layout_donation_fragment, container, false)
        binding.model = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initProgressDialog()
        initDonationAmountSelection()
        populateImage()
        binding.editAmount.setOnClickListener {
            displayAmountSelectionDialog()
        }
        binding.buttonDonate.setOnClickListener {
            if(!BuildConfig.DEBUG){
                displayConfirmDialog()
            } else {
                // skip add credit card
                presenter.donate(Token(JSONObject()))
                progressDialog.show()
            }
        }
    }

    override fun displayLoading() {
        progressDialog.show()
    }

    override fun dismissLoading() {
        progressDialog.dismiss()
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.alert_message_submit_donation))
    }

    private fun initDonationAmountSelection() {
        amountArray = resources.getStringArray(R.array.amount_array);
        viewModel.amount.set(amountArray[0])
    }

    private fun populateImage() {
        Glide.with(activity)
                .load(viewModel.imageURL)
                .fitCenter()
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .into(binding.imageOrganization)
    }

    private fun displayAmountSelectionDialog() {

        alertDialog = AlertDialog.Builder(activity).apply {
            this.setSingleChoiceItems(amountArray, amountArray.indexOf(viewModel.amount.get()), DialogInterface.OnClickListener { dialog, item ->
                viewModel.amount.set(amountArray[item])
                dialog.dismiss()
            })
        }.create()
        alertDialog?.show()

    }

    private fun displayConfirmDialog() {
        val message = getString(R.string.alert_message_confirm, viewModel.amount.get(), viewModel.name)
        alertDialog = AlertDialog.Builder(activity).apply {
            this.setTitle(R.string.alert_title_confirm)
            this.setMessage(message)
            this.setPositiveButton(R.string.alert_button_donate,
                    DialogInterface.OnClickListener { dialog, _ ->
                        goToAddCreditCard()
                        dialog.dismiss()
                    })
            this.setNegativeButton(R.string.alert_button_cancel,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
            this.setCancelable(false)
        }.create()
        alertDialog?.show()
    }

    private fun goToAddCreditCard() {
        startActivityForResult(AddCreditCardActivity.getIntent(activity), REQUEST_CC)
    }

    override fun goToSuccessScreen() {
        startActivityForResult(SuccessActivity.getIntent(activity, viewModel.name, viewModel.amount.get()), REQUEST_FINAL)
    }

    override fun displayError(errorMessage: String) {
        alertDialog = AlertDialog.Builder(activity).apply {
            var message = if (errorMessage.isNullOrBlank()) getString(R.string.alert_message_communication_error) else errorMessage
            this.setTitle(R.string.alert_title_error)
            this.setMessage(message)
            this.setPositiveButton(R.string.alert_button_ok,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
            this.setCancelable(false)
        }.create()
        alertDialog?.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CC -> {
                if (resultCode == AddCreditCardActivity.RESULT_CANCEL) {
                    return
                }

                val token = data!!.getParcelableExtra<Token>(AddCreditCardActivity.EXTRA_TOKEN_OBJECT)
                presenter.donate(token)
                progressDialog.show()
            }

            REQUEST_FINAL -> {
                activity.finish()
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

