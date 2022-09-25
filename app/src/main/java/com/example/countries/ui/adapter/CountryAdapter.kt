package com.example.countries.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.data.model.CountryModel
import com.example.countries.databinding.RvItemBinding
import com.example.countries.util.OnClick

class CountryAdapter constructor(
    private val clickListener: OnClick
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private var countryListt: ArrayList<CountryModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    :CountryAdapter.CountryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)

        return CountryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val current = countryListt[position]
        holder.bind(current)
        holder.bindView.countryItem.setOnClickListener {
            clickListener.onClickCountry(current)
        }
    }

    override fun getItemCount(): Int {
        return countryListt.size
    }

    fun setCountriesList(countryList: List<CountryModel>){
        this.countryListt.apply {
            countryListt.clear()
            addAll(countryList)
            notifyDataSetChanged()

        }
        Log.d("countryListt", countryListt.toString())

    }

    class  CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bindView= RvItemBinding.bind(itemView)
        fun bind(countryModel:CountryModel){
            bindView.textViewCountryName.text=countryModel.name
        }
    }

}