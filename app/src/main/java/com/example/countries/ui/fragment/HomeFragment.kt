package com.example.countries.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.data.model.CountryModel
import com.example.countries.util.NetworkResponse
import com.example.countries.ui.adapter.CountryAdapter
import com.example.countries.util.Constant
import com.example.countries.util.OnClick
import com.example.countries.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class HomeFragment : Fragment(), OnClick {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    private var countryAdapter: CountryAdapter = CountryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCountries(Constant.LIMIT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
         recyclerview= binding.rvCountriesList
         recyclerview.layoutManager=LinearLayoutManager(requireContext())
         recyclerview.adapter=adapter
         */
        binding.rvCountryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryAdapter
        }
        collectCountriesModel()

    }

    private fun collectCountriesModel() {
        /*
         lifecycleScope.launch{
             Log.d("collectCountriesModel: ",viewModel.countriesList.toString())
             adapter.setCountriesList(viewModel.countriesList)
         }
         */

        viewModel.countriesList.observe(viewLifecycleOwner) {
            Log.d("collectCountriesModel: ", it.toString())
            if (it != null) {
                countryAdapter.setCountriesList(it.data)


            }

        }
    }


    override fun onClickCountry(country: CountryModel) {
        val bundle = Bundle()
        bundle.putParcelable("country", country)
        view?.let {
            //ERROR
            val navHostFragment = parentFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }


}