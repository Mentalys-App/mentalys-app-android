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
            "state_overwhelmed", "state_anxious", "state_stressed", "state_sad", "state_lonely",
            "state_hopeless", "state_tired", "state_frustrated", "state_empty", "state_lost",
            "state_excited", "state_grateful", "state_hopeful"
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
