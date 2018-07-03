package com.enixma.sample.charity.presentation.charitylist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.enixma.sample.charity.R

class CharityListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.app_name)
        setContentView(R.layout.layout_charity_list_activity)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_host, CharityListFragment.newInstance())
                .commit()
    }
}

