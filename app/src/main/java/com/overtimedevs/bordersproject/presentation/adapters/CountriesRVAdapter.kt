package com.overtimedevs.bordersproject.presentation.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.Filter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.overtimedevs.bordersproject.R
import com.overtimedevs.bordersproject.databinding.ItemCountryCardBinding
import com.overtimedevs.bordersproject.presentation.model.CountryCardModel
import kotlin.collections.ArrayList

interface OnClickListener{
    fun onCardClick(countryCardModel: CountryCardModel)
    fun onStarClick(countryCardModel: CountryCardModel)
}

class CountriesRVAdapter() :
    RecyclerView.Adapter<CountriesRVAdapter.ViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<CountryCardModel>() {
        override fun areItemsTheSame(oldItem: CountryCardModel, newItem: CountryCardModel): Boolean {
            return  oldItem.countryId == newItem.countryId &&
                    oldItem.message == newItem.message &&
                    oldItem.borderStatus == newItem.borderStatus &&
                    oldItem.trackStatus == newItem.trackStatus
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CountryCardModel, newItem: CountryCardModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    private var onClickListener : OnClickListener? = null
    var initialDataSet = ArrayList<CountryCardModel>()

    fun setOnClickLister(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    fun setNewList(newData: List<CountryCardModel>) {
        differ.submitList(newData)
        initialDataSet = ArrayList<CountryCardModel>().apply {
           addAll(newData)
        }
    }

    fun removeCountry(countryCardModel: CountryCardModel) {
        var countryId: CountryCardModel? = null
        val prevList = mutableListOf<CountryCardModel>()
        prevList.addAll(differ.currentList)

        for ((index, country) in prevList.withIndex()) {
            if (country.countryId == countryCardModel.countryId) {
                countryId = country
                break
            }
        }

        prevList.remove(countryId)
        differ.submitList(prevList)
    }

    class ViewHolder(
        var binding: ItemCountryCardBinding,
        private val onClickListener: OnClickListener?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setCountryCard(countryModel: CountryCardModel) {
            binding.countryCard = countryModel

            binding.trackStatusIcon.setOnClickListener {
                onClickListener?.onStarClick(countryModel)
                countryModel.onStarClick()
                countryModel.onTrackStatusChanged?.invoke(countryModel)
            }

            binding.cardContainer.setOnClickListener {
                onClickListener?.onCardClick(countryModel)
                countryModel.onCountryClicked?.invoke(countryModel)
            }
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding: ItemCountryCardBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_country_card, viewGroup, false)

        return ViewHolder(binding, onClickListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setCountryCard(differ.currentList[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getFilter(): Filter {
        return cityFilter
    }

    private val cityFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<CountryCardModel> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialDataSet.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase()
                initialDataSet.forEach {
                    if (it.countryName.lowercase().contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                val filteredDataset = results.values as ArrayList<CountryCardModel>
                differ.submitList(filteredDataset)
            }
        }
    }

}