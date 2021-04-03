package com.suy.drop.ui.autocomplete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suy.drop.R
import com.suy.drop.databinding.ItemPlaceBinding
import com.suy.drop.model.data.AutoCompleteResult

class AutoCompleteAdapter(
    private val list: List<AutoCompleteResult>,
    private val listener: AutoCompleteListener
) : RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(autoCompleteResult: AutoCompleteResult) {
            with(binding) {
                tvPlaceName.text =
                    autoCompleteResult.format?.mainText
                        ?: itemView.context.getString(R.string.text_name_not_found)
                tvPlaceAddress.text =
                    autoCompleteResult.format?.secondaryText
                        ?: itemView.context.getString(R.string.text_address_not_found)
                itemView.setOnClickListener { listener.onPlaceClicked(autoCompleteResult.placeId) }
            }
        }
    }
}