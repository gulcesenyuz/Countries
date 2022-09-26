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
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.example.countries.data.model.CountryModel
import com.example.countries.databinding.FragmentDetailBinding
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.util.Constant
import com.example.countries.util.loadUrl
import com.example.countries.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentDetailBinding
    private var countryDetail: CountryModel? = null

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

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCountryDetailToUI()

    }

    private fun setCountryDetailToUI() {
        //https://commons.wikimedia.org/wiki/Special:FilePath/Flag%20of%20Ghana.svg
        var url = ""
        viewModel.countryDetail.observe(viewLifecycleOwner) {
            if (it != null) {
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
}

