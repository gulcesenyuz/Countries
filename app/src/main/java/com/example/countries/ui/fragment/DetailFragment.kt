package com.example.countries.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.example.countries.R
import com.example.countries.data.model.CountryModel
import com.example.countries.databinding.FragmentDetailBinding
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.util.Constant
import com.example.countries.util.FavouriteState
import com.example.countries.util.loadUrl
import com.example.countries.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(), FavouriteState {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var favouriteState: FavouriteState
    private lateinit var binding: FragmentDetailBinding
    private var countryDetail: CountryModel? = null
    private lateinit var countryModel: CountryModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleArguments()
        countryDetail?.let { viewModel.getCountryDetail(it.code) }
        countryDetail?.let { viewModel.getDetail(it.code) }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCountryDetailToUI()
        countryDetail?.let { getDetailCountry(it.code) }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()

        }

        if (countryDetail?.isFav == true) {
            binding.ivFav.setImageResource(R.drawable.ic_star_full)

        } else {
            binding.ivFav.setImageResource(R.drawable.ic_star_empty)

        }

        checkFavState()


    }

    private fun checkFavState(){
        binding.ivFav.setOnClickListener {
            if (countryDetail?.isFav == true) {
                checkFavState(countryModel, false)
                binding.ivFav.setImageResource(R.drawable.ic_star_empty)

            } else {
                checkFavState(countryModel, true)
                binding.ivFav.setImageResource(R.drawable.ic_star_full)

            }
        }
    }

    private fun getDetailCountry(code: String) {
        countryModel = viewModel.getDetail(code)
    }

    private fun setCountryDetailToUI() {
        //https://commons.wikimedia.org/wiki/Special:FilePath/Flag%20of%20Ghana.svg
        var url = ""
        viewModel.countryDetail.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvToolbar.text = it.data.name
                val urlWiki: Uri =
                    Uri.parse(Constant.WIKI_BASE + it.data.wikiDataId)
                binding.tvCodeInput.text = it.data.code.toString()
                it.data.flagImageUri?.let { it1 ->
                    url = it1.replace("http", "https")
                }
                binding.ivFlag.loadUrl(url)
//https://stackoverflow.com/questions/55473076/class-com-bumptech-glide-load-engine-glideexception-failed-to-load-resource
                // context?.let { it -> Glide.with(it).load(url).into(binding.ivFlag) }
                binding.btnMoreInfo.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, urlWiki)
                    context?.startActivity(intent)
                }

            }
        }


    }


    private fun getBundleArguments() {
        countryDetail = arguments?.getParcelable("country")
    }

    override fun checkFavState(country: CountryModel, isFav: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (isFav) {
                viewModel.saveCountry(requireContext(), country)
            } else {
                viewModel.deleteCountry(requireContext(), country)
            }

        }
    }
}

