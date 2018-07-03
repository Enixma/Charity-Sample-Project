package com.enixma.sample.charity.presentation.charitylist

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enixma.sample.charity.R
import com.enixma.sample.charity.data.di.ServiceFactoryModule
import com.enixma.sample.charity.data.di.CharityDataModule
import com.enixma.sample.charity.data.entity.CharityEntity
import com.enixma.sample.charity.databinding.LayoutCharityListFragmentBinding
import com.enixma.sample.charity.presentation.charitylist.di.CharityListModule
import com.enixma.sample.charity.presentation.charitylist.di.DaggerCharityListComponent
import com.enixma.sample.charity.presentation.charitylist.mapper.CharityEntityToListItemMapper
import com.enixma.sample.charity.presentation.donation.DonationActivity
import java.util.*
import javax.inject.Inject

class CharityListFragment : Fragment(), CharityListContract.View {
    private lateinit var binding: LayoutCharityListFragmentBinding
    private lateinit var viewModel: CharityListViewModel
    private var adapter: CharityListAdapter? = null
    private var items: ArrayList<CharityListItem> = ArrayList()

    @Inject
    lateinit var presenter: CharityListContract.Action

    companion object {
        fun newInstance(): CharityListFragment {
            return CharityListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CharityListViewModel::class.java)
        DaggerCharityListComponent.builder()
                .serviceFactoryModule(ServiceFactoryModule(activity))
                .charityDataModule(CharityDataModule(activity))
                .charityListModule(CharityListModule(this))
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater!!, R.layout.layout_charity_list_fragment, container, false)
        binding.model = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycle.addObserver(presenter as CharityListPresenter)
        binding.swipe.setOnRefreshListener { presenter.getCharityList() }
        binding.swipe.isRefreshing = true
        presenter.getCharityList()
    }

    override fun populateList(charityList: List<CharityEntity>) {
        binding.swipe.isRefreshing = false
        viewModel.hasData.set(true)

        with(items) {
            this.clear()
            this.addAll(getCharityListItems(charityList))

            adapter = CharityListAdapter(this, object : CharityListAdapter.OnItemListener {
                override fun onItemClick(item: CharityListItem) {
                    goToDonation(item)
                }
            })

            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            binding.listView.layoutManager = layoutManager
            binding.listView.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
    }

    private fun getCharityListItems(charityList: List<CharityEntity>): ArrayList<CharityListItem> {
        return ArrayList<CharityListItem>().apply {
            for (charityEntity in charityList) {
                this.add(CharityEntityToListItemMapper.map(charityEntity))
            }
        }
    }

    private fun goToDonation(item: CharityListItem) {
        startActivity(DonationActivity.getIntent(activity, item.name, item.imageUrl))
    }

    override fun displayNoData() {
        binding.swipe.isRefreshing = false
        viewModel.hasData.set(false)
    }
}
