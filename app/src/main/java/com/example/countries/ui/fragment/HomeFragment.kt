package com.example.countries.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countries.R
import com.example.countries.data.model.CountryModel
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.ui.adapter.CountryAdapter
import com.example.countries.util.Constant
import com.example.countries.util.FavouriteState
import com.example.countries.util.OnClick
import com.example.countries.viewmodel.MainViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(), OnClick, FavouriteState {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var countryAdapter: CountryAdapter = CountryAdapter(this, this)

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
        binding.rvCountryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryAdapter
        }
        collectCountriesModel()
        //  countryDao.insertCountry(countryAdapter.hashMap[])
    }

    private fun collectCountriesModel() {
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
        val fragment: Fragment = DetailFragment()
        fragment.arguments = bundle
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.nav_host_fragment, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()

    }

    private fun saveToFireStore(country: CountryModel, code: String) {
        val db = Firebase.firestore
        val countryItem: MutableMap<String, Any> = HashMap()
        countryItem["code"] = country.code
        countryItem["name"] = country.name

        db.collection("countries").document(code).set(countryItem)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Country saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failure to save", Toast.LENGTH_SHORT).show()

            }
    }

    private fun deleteFromFirestore(code: String) {

        val db = Firebase.firestore
        db.collection("countries").document(code)
            .delete()
            .addOnSuccessListener {
                Log.d("DEE", id.toString())
                Toast.makeText(requireContext(), "Country unsaved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Country couldn't unsaved", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun checkFavState(country: CountryModel, isFav: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val code = country.code
            if (isFav) {
                saveToFireStore(country, code)
            } else {
                deleteFromFirestore(code)
            }

        }
    }


}