package com.example.notesapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.Adapter.RecyclerViewAdapter
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.entities.Notes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var noteId=-1
    var list:List<Notes>?=null

    lateinit var binding: FragmentHomeBinding
    private var myAdapter=RecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        var job=CoroutineScope(Dispatchers.Main).launch {
            var notesList= NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().getAllNote()
            list=notesList
            myAdapter.setList(notesList)
            binding.recyclerView.adapter=myAdapter

            Log.i("Arjun","Showing recycler view")
            Log.i("Arjun",notesList.toString())
        }
        myAdapter.notifyDataSetChanged()

        myAdapter!!.setOnClickListener(onClicked)
        myAdapter.setHasStableIds(true)
       // binding.recyclerView.adapter!!.notifyDataSetChanged()

        binding.floatingActionButton.setOnClickListener {
            val bundle = bundleOf("noteId" to -1)
            it.findNavController().navigate(R.id.action_homeFragment_to_createNoteFragment,bundle)
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter("Notify recyclerView")
        )

        // Inflate the layout for this fragment
        return binding.root

    }

    val broadcastReceiver:BroadcastReceiver=object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            Log.i("Arjun","Recycle")
            binding.recyclerView.adapter!!.notifyDataSetChanged()

        }
    }

    private val onClicked = object :RecyclerViewAdapter.OnItemClickListener{
        override fun onClicked(noteId: Int) {
            val bundle = bundleOf("noteId" to noteId)
            findNavController().navigate(R.id.action_homeFragment_to_createNoteFragment,bundle)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}