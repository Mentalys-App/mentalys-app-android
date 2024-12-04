package com.mentalys.app.ui.mental.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mentalys.app.ui.mental.history.HandwritingTestHistoryFragment
import com.mentalys.app.ui.mental.history.QuizTestHistoryFragment
import com.mentalys.app.ui.mental.history.VoiceTestHistoryFragment

class ViewPagerAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VoiceTestHistoryFragment()
            1 -> HandwritingTestHistoryFragment()
            2 -> QuizTestHistoryFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}