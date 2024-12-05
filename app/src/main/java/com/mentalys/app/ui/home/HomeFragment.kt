package com.mentalys.app.ui.home

import android.content.Intent
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
import com.mentalys.app.ui.mental.MentalTestActivity
import com.mentalys.app.ui.adapters.ClinicAdapter
import com.mentalys.app.ui.adapters.ClinicItem
import com.mentalys.app.ui.adapters.PsychiatristsAdapter
import com.mentalys.app.ui.adapters.PsychiatristsItem
import com.mentalys.app.ui.specialist.SpecialistActivity

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
            .load(R.drawable.konsultasi_psikiater)
            .transform(CircleCrop())
            .into(binding.iconMenuKonsultasi)
        Glide.with(this)
            .load(R.drawable.klinik_psikologi)
            .transform(CircleCrop())
            .into(binding.iconMenuKlinik)
        Glide.with(this)
            .load(R.drawable.cek_mental_health)
            .transform(CircleCrop())
            .into(binding.iconMenuCekMental)
        Glide.with(this)
            .load(R.drawable.artikel_mental_health)
            .transform(CircleCrop())
            .into(binding.iconMenuBlm)

        // Go to mental check menu
        binding.topMentalCheckMenu.setOnClickListener{
            val intent = Intent(requireContext(), MentalTestActivity::class.java)
            startActivity(intent)
        }

        binding.topConsultasionMenu.setOnClickListener {
            startActivity(Intent(requireContext(), SpecialistActivity::class.java))
        }

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

        setupArticleRecyclerView()
    }

    private fun setupArticleRecyclerView() {

//        val articleItems = listOf(
//            ArticleItem(
//                R.drawable.image_depression,
//                "Breaking Through the Fog: Understanding and Coping with Depression",
//                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
//                "by Muhammad Ibnu",
//                "07 Nov",
//                "• 10 minutes read",
//                "#depression #stress"
//            ),
//            ArticleItem(
//                R.drawable.image_depression,
//                "2Breaking Through the Fog: Understanding and Coping with Depression",
//                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
//                "by Muhammad Ibnu",
//                "07 Nov",
//                "• 10 minutes read",
//                "#depression #stress"
//            ),
//            ArticleItem(
//                R.drawable.image_depression,
//                "3Breaking Through the Fog: Understanding and Coping with Depression",
//                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
//                "by Muhammad Ibnu",
//                "07 Nov",
//                "• 10 minutes read",
//                "#depression #stress"
//            ),
//        )

        // Set up the article adapter
//        val articleAdapter = ArticleAdapter(articleItems)
//        binding.rvInsights.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        binding.rvInsights.adapter = articleAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}