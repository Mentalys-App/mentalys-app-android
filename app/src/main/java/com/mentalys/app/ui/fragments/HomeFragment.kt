package com.mentalys.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentHomeBinding
import com.mentalys.app.ui.adapters.ClinicAdapter
import com.mentalys.app.ui.adapters.ClinicItem
import com.mentalys.app.ui.adapters.PsychiatristsAdapter
import com.mentalys.app.ui.adapters.PsychiatristsItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .transform(CircleCrop())
            .into(binding.iconMenuKonsultasi)
        Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .transform(CircleCrop())
            .into(binding.iconMenuKlinik)
        Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .transform(CircleCrop())
            .into(binding.iconMenuCekMental)
        Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .transform(CircleCrop())
            .into(binding.iconMenuBlm)

        val clinicItems = listOf(
            ClinicItem(
                R.drawable.img_clinic,
                "Psikiater DR. Dr. Anak Ayu Sri Wahyuni, SpKJ",
                "5.00–8.00 pm",
                "Jl. Belimbing No.74, Dangin Puri Kaja, Kec. Denpasar Utara, Kota Denpasar, Bali 80234"
            ),
            ClinicItem(
                R.drawable.img_clinic,
                "Psikiater DR. Dr. Anak Ayu Sri Wahyuni, SpKJ",
                "5.00–8.00 pm",
                "Jl. Belimbing No.74, Dangin Puri Kaja, Kec. Denpasar Utara, Kota Denpasar, Bali 80234"
            ),
            ClinicItem(
                R.drawable.img_clinic,
                "Psikiater DR. Dr. Anak Ayu Sri Wahyuni, SpKJ",
                "5.00–8.00 pm",
                "Jl. Belimbing No.74, Dangin Puri Kaja, Kec. Denpasar Utara, Kota Denpasar, Bali 80234"
            )
        )
        val clinicAdapter = ClinicAdapter(clinicItems)
        binding.rvNearbyClinics.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvNearbyClinics.adapter = clinicAdapter

        val psychiatristsItem = listOf(
            PsychiatristsItem(
                R.drawable.img_clinic,
                "Dr Made Agus Buydiartaaaa",
                "08.00 - 18.00",
                "Rp 20.000,00"
            ),
            PsychiatristsItem(
                R.drawable.img_clinic,
                "Dr Made Agus",
                "08.00 - 18.00",
                "Rp 20.000,00"
            ),
            PsychiatristsItem(
                R.drawable.img_clinic,
                "Dr Made Agus",
                "08.00 - 18.00",
                "Rp 20.000,00"
            ),
        )
        val psychiatristsAdapter = PsychiatristsAdapter(psychiatristsItem)
        binding.rvPsychiatrists.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPsychiatrists.adapter = psychiatristsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}