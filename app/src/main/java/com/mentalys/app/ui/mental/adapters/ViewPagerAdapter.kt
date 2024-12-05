package com.mentalys.app.ui.mental.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mentalys.app.ui.mental.history.MentalHistoryHandwritingFragment
import com.mentalys.app.ui.mental.history.MentalHistoryQuizFragment
import com.mentalys.app.ui.mental.history.MentalHistoryVoiceFragment

class ViewPagerAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MentalHistoryVoiceFragment()
            1 -> MentalHistoryHandwritingFragment()
            2 -> MentalHistoryQuizFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}