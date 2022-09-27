package com.example.countries.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.countries.R
import com.example.countries.data.model.CountryModel
import com.example.countries.databinding.RvItemBinding
import com.example.countries.util.FavouriteState
import com.example.countries.util.OnClick

class FavAdapter constructor(
    private val clickListener: OnClick,
    private val favStateListener: FavouriteState
) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    private var countries: ArrayList<CountryModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : FavAdapter.FavViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)

        return FavViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val current = countries[position]
        holder.bind(current)
        holder.bindView.textViewCountryName.setOnClickListener {
            clickListener.onClickCountry(current)
        }
        if (current.isFav) {
            holder.bindView.imageViewFavouriteState.setImageResource(R.drawable.ic_star_full)
        } else {
            holder.bindView.imageViewFavouriteState.setImageResource(R.drawable.ic_star_empty)

        }
        holder.bindView.imageViewFavouriteState.setOnClickListener {
            holder.bindView.imageViewFavouriteState.setImageResource(R.drawable.ic_star_empty)
            favStateListener.checkFavState(current, false)

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

    class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bindView = RvItemBinding.bind(itemView)
        fun bind(countryModel: CountryModel) {
            bindView.textViewCountryName.text = countryModel.name
        }
    }



}
