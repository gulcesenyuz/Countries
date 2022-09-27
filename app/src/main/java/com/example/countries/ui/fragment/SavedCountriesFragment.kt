package com.example.countries.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countries.R
import com.example.countries.data.model.CountryModel
import com.example.countries.databinding.FragmentDetailBinding
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.databinding.FragmentSavedCountriesBinding
import com.example.countries.ui.adapter.CountryAdapter
import com.example.countries.util.*
import com.example.countries.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedCountriesFragment : Fragment(), OnClick, FavouriteState {
    private var countryAdapter: CountryAdapter = CountryAdapter(this, this)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentSavedCountriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedCountriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFavouriteCountries()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCountryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryAdapter
        }
        collectCountriesModel()
    }

    private fun collectCountriesModel() {
        viewModel.countriesFav.observe(viewLifecycleOwner) {
            Log.d("collectCountriesModel: ", it.toString())
            if (it != null) {
                countryAdapter.setCountriesList(it)
            }

        }
    }

    override fun onClickCountry(country: CountryModel) {
        val bundle = Bundle()
        bundle.putParcelable("country", country)
        val fragment: Fragment = DetailFragment()
        fragment.arguments = bundle
        val ft = requireFragmentManager().beginTransaction()
        ft.replace(R.id.nav_host_fragment, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun checkFavState(country: CountryModel, isFav: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val code = country.code
            if (isFav) {
                viewModel.saveCountry(requireContext(), country, code)
            } else {
                viewModel.deleteCountry(requireContext(), code)
            }

        }
    }
}