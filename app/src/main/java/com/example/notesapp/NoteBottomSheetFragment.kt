package com.example.notesapp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.FragmentNoteBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_note_bottom_sheet.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoteBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteBottomSheetFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentNoteBottomSheetBinding
    var behaviour: BottomSheetBehavior<*>?=null

    var noteId:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        noteId = requireArguments().getInt("noteId",-1)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_note_bottom_sheet, container, false)
        var view = LayoutInflater.from(context).inflate(R.layout.fragment_note_bottom_sheet, null)


        if (noteId!= -1){
            binding.deleteLinearLayout.visibility = View.VISIBLE
        }else{
            binding.deleteLinearLayout.visibility = View.GONE
        }

        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal: View? = d.findViewById(R.id.FragmentNoteBottomSheet)
            behaviour = BottomSheetBehavior.from(bottomSheetInternal!!)
            //use this behaviour

            behaviour!!.state=BottomSheetBehavior.STATE_EXPANDED
            behaviour!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

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
                            behaviour!!.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                    }

                }
            })

            onClickListner(behaviour!!)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onClickListner(behaviour:BottomSheetBehavior<*>) {
        binding.apply {
            fNote1.setOnClickListener {
                tick1.isVisible = true
                tick2.isVisible = false
                tick3.isVisible = false
                tick4.isVisible = false
                tick5.isVisible = false
                tick6.isVisible = false
                Log.d("arjun","is clicked")
                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "note_color_1")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            fNote2.setOnClickListener {
                tick1.isVisible = false
                tick2.isVisible = true
                tick3.isVisible = false
                tick4.isVisible = false
                tick5.isVisible = false
                tick6.isVisible = false

                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "note_color_2")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            fNote3.setOnClickListener {
                tick1.isVisible = false
                tick2.isVisible = false
                tick3.isVisible = true
                tick4.isVisible = false
                tick5.isVisible = false
                tick6.isVisible = false

                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "note_color_3")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            fNote4.setOnClickListener {
                tick1.isVisible = false
                tick2.isVisible = false
                tick3.isVisible = false
                tick4.isVisible = true
                tick5.isVisible = false
                tick6.isVisible = false

                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "note_color_4")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            fNote5.setOnClickListener {
                tick1.isVisible = false
                tick2.isVisible = false
                tick3.isVisible = false
                tick4.isVisible = false
                tick5.isVisible = true
                tick6.isVisible = false

                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "note_color_5")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            fNote6.setOnClickListener {
                tick1.isVisible = false
                tick2.isVisible = false
                tick3.isVisible = false
                tick4.isVisible = false
                tick5.isVisible = false
                tick6.isVisible = true

                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "note_color_6")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            deleteLinearLayout.setOnClickListener {
                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "delete")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                dismiss()
                findNavController().navigate(R.id.action_noteBottomSheetFragment_to_homeFragment)
            }

            imageLinearLayout.setOnClickListener {
                val intent = Intent()
                intent.action = "com.example.Broadcast"
                intent.putExtra("color", "insertImage")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                dismiss()
            }

        }

    }


    companion object {


        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                NoteBottomSheetFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

}
