package com.enixma.sample.charity.presentation.charitylist

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.enixma.sample.charity.R
import com.enixma.sample.charity.databinding.LayoutCharityListItemBinding

class CharityListAdapter(private val items: ArrayList<CharityListItem>, private val onItemListener: OnItemListener) : RecyclerView.Adapter<CharityListAdapter.ViewHolder>() {

    private lateinit var binding: LayoutCharityListItemBinding

    interface OnItemListener {
        fun onItemClick(item: CharityListItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_charity_list_item, parent, false)
        return CharityListAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        setItemThumbnail(holder, position)
        setViewListener(holder, item)
    }

    private fun setItemThumbnail(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.imageView.context)
                .load(items[position].imageUrl)
                .fitCenter()
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .into(holder.binding.imageView)
    }

    private fun setViewListener(holder: ViewHolder, item: CharityListItem) {
        holder.binding.buttonSelect.setOnClickListener { onItemListener.onItemClick(item) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val binding: LayoutCharityListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CharityListItem) {
            binding.model = item
            binding.executePendingBindings()
        }
    }
}