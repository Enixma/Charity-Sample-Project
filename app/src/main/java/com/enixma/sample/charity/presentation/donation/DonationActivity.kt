package com.enixma.sample.charity.presentation.donation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.enixma.sample.charity.R

class DonationActivity : AppCompatActivity() {

    companion object {
        const val NAME = "name"
        const val IMAGE_URL = "image"

        fun getIntent(context: Context, name: String, imageUrl: String): Intent {
            var intent = Intent(context, DonationActivity::class.java)
            intent.putExtra(NAME, name)
            intent.putExtra(IMAGE_URL, imageUrl)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_donation_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        if (intent.hasExtra(NAME) && intent.hasExtra(IMAGE_URL)) {
            title = intent.getStringExtra(NAME)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_host, DonationFragment.newInstance(intent.getStringExtra(NAME), intent.getStringExtra(IMAGE_URL)))
                    .commit()
        } else {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
