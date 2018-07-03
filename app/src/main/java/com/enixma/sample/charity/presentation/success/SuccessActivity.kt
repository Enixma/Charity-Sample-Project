package com.enixma.sample.charity.presentation.success

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.enixma.sample.charity.R
import kotlinx.android.synthetic.main.layout_success_activity.*

class SuccessActivity : AppCompatActivity() {
    companion object {
        const val NAME = "name"
        const val AMOUNT = "amount"

        fun getIntent(context: Context, name: String, amount: String): Intent {
            var intent = Intent(context, SuccessActivity::class.java)
            intent.putExtra(NAME, name)
            intent.putExtra(AMOUNT, amount)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.title_success)
        setContentView(R.layout.layout_success_activity)
        if (intent.hasExtra(NAME) && intent.hasExtra(AMOUNT)) {
            text_thank_you_message.text = getString(R.string.message_thank_you, intent.getStringExtra(AMOUNT), intent.getStringExtra(NAME))
        } else {
            goBackToCharityList()
        }

        button_exit.setOnClickListener { goBackToCharityList() }
    }

    override fun onBackPressed() {
        goBackToCharityList()
    }

    private fun goBackToCharityList() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}