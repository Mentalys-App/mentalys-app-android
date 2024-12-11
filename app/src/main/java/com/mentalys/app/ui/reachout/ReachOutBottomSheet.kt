package com.mentalys.app.ui.reachout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mentalys.app.R
import com.mentalys.app.databinding.LayoutBottomSheetReachOutBinding

class ReachOutBottomSheet(private val onFeelingSelected: (String) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: LayoutBottomSheetReachOutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetReachOutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emotionsKeys = listOf(
            getString(R.string.state_overwhelmed),
            getString(R.string.state_anxious),
            getString(R.string.state_stressed),
            getString(R.string.state_sad),
            getString(R.string.state_lonely),
            getString(R.string.state_hopeless),
            getString(R.string.state_tired),
            getString(R.string.state_frustrated),
            getString(R.string.state_empty),
            getString(R.string.state_lost),
            getString(R.string.state_excited),
            getString(R.string.state_grateful),
            getString(R.string.state_hopeful)
        )

        val feelings = emotionsKeys.map { key ->
            getString(resources.getIdentifier(key, "string", requireContext().packageName))
        }

        // Set FlexboxLayoutManager
        val flexboxLayoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            flexWrap = FlexWrap.WRAP
        }
        binding.reachOutRecyclerView.layoutManager = flexboxLayoutManager

        val adapter = ReachOutAdapter(feelings) { selectedFeeling ->
            onFeelingSelected(selectedFeeling)
            dismiss()
        }
        binding.reachOutRecyclerView.adapter = adapter

    }

}
