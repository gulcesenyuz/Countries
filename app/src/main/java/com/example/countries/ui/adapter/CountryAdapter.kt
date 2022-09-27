package com.example.countries.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.data.model.CountryModel
import com.example.countries.databinding.RvItemBinding
import com.example.countries.util.FavouriteState
import com.example.countries.util.OnClick

class CountryAdapter constructor(
    private val clickListener: OnClick,
    private val favStateListener: FavouriteState
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    private var favCountries: ArrayList<CountryModel> = ArrayList()
    private var countries: ArrayList<CountryModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : CountryAdapter.CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)

        return CountryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val current = countries[position]
        holder.bind(current)
        holder.bindView.textViewCountryName.setOnClickListener {
            clickListener.onClickCountry(current)
        }

        holder.bindView.imageViewFavouriteState.setOnClickListener {
            val isCountryInList = favState(current)
            if (isCountryInList) {
                favCountries.remove(current)
                holder.bindView.imageViewFavouriteState.setImageResource(R.drawable.ic_star_empty)
                Log.d("FAV:", favCountries.toString())
                favStateListener.checkFavState(current, false)
            } else {
                favCountries.add(current)
                holder.bindView.imageViewFavouriteState.setImageResource(R.drawable.ic_star_full)
                Log.d("FAV:", favCountries.toString())
                favStateListener.checkFavState(current, true)

            }
        }

    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun setCountriesList(countryList: List<CountryModel>) {
        this.countries.apply {
            countries.clear()
            addAll(countryList)
            notifyDataSetChanged()

        }
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bindView = RvItemBinding.bind(itemView)
        fun bind(countryModel: CountryModel) {
            bindView.textViewCountryName.text = countryModel.name
        }
    }


    private fun favState(country: CountryModel): Boolean {

            for (item in favCountries) {
            if (country.code == item.code) {
                return true
            }
        }
        return false
    }


}
