package com.example.notesapp.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.notesapp.NoteBottomSheetFragment
import com.example.notesapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoteBottomSheetFragment:BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        var view = LayoutInflater.from(context).inflate(R.layout.fragment_note_bottom_sheet, null)

        dialog.setContentView(R.layout.fragment_note_bottom_sheet)

        val behaviour = BottomSheetBehavior.from(view)

        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                var state = ""
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        state = "DRAGGING"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        state = "COLLAPSED"
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        state = "EXPANDED"
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        state = "HIDDEN"
                        dismiss()
                        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        state = "SETTLING"
                    }
                }

            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}