package com.example.notesapp

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.FragmentCreateNoteBinding
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class CreateNoteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var currentDate:String
    lateinit var binding:FragmentCreateNoteBinding
    var color:Int? = null
    var noteId=-1

    val REQUEST_CODE=1024

    private var imagePath:String?=null
    private var READ_STORAGE_PERM = 123

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
        noteId = requireArguments().getInt("noteId",-1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_create_note, container, false)

        val sdf= SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate=sdf.format(Date())


        if (noteId != -1) {
            CoroutineScope(Dispatchers.Main).launch {

                var note = NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().getNote(noteId)
                binding.titleEditText.setText(note.title)
                binding.subTitleEditText.setText(note.sub_title)
                binding.descriptionEditText.setText(note.note_text)
                binding.dataTimeTextBox.text = currentDate
                if(note.img_path!=null) {
                    imagePath=note.img_path
                    binding.imageView.setImageBitmap(BitmapFactory.decodeFile(note.img_path))
                    binding.imageView.visibility = View.VISIBLE
                    Log.i("Arjun","Displaying image: "+note.img_path!!.toString())

                    //binding.imageView.setImageBitmap(BitmapFactory.decodeFile(note.img_path))

                }
            }
        }

        binding.deleteImageView.setOnClickListener {
            deleteImage(noteId)
        }
        binding.tickImg.setOnClickListener {
            if(noteId!=-1){
                updateNote(noteId)
            }else {
                saveNote()
            }
            Log.i("Arjun","Saving note")
        }


        binding.backImg.setOnClickListener {
            it.findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)
        }

        binding.showBottomSheetIcon.setOnClickListener {

            val bundle = bundleOf("noteId" to noteId)
            it.findNavController().navigate(R.id.action_createNoteFragment_to_noteBottomSheetFragment,bundle)
//            var noteBottomSheetFragment = NoteBottomSheetFragment()
//            noteBottomSheetFragment.show(requireActivity().supportFragmentManager,tag)
            //noteBottomSheetFragment.behaviour!!.state=BottomSheetBehavior.STATE_EXPANDED

        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter("com.example.Broadcast")
        )

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun deleteImage(noteId:Int) {
        imagePath=null
        binding.imageView.setImageDrawable(null)
        CoroutineScope(Dispatchers.Main).launch {
            var notes= NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().getNote(noteId)
            notes.img_path=null
            NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().updateNote(notes)
        }

        binding.deleteImageView.isVisible=false

    }
    private fun updateNote(noteId:Int) {
            CoroutineScope(Dispatchers.Main).launch {
                var notes= NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().getNote(noteId)
                notes.title=binding.titleEditText.text.toString()
                notes.sub_title=binding.subTitleEditText.text.toString()
                notes.note_text=binding.descriptionEditText.text.toString()
                notes.data_time=currentDate
                if(color!=null) {
                    notes.color_info = color!!.toString()
                }
                if(imagePath==null){
                    notes.img_path=null
                }else {
                    notes.img_path = imagePath!!
                    Log.i("Arjun","saved image path: "+imagePath!!.toString())
                }
                binding.dataTimeTextBox.text=currentDate
                NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().updateNote(notes)
                notes.title=""
                notes.sub_title=""
                notes.note_text=""
                notes.data_time=""
                findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)
            }
    }


    private fun saveNote() {
        if(binding.titleEditText.text.toString().isEmpty()){
            Toast.makeText(requireContext(),"Enter title",Toast.LENGTH_LONG).show()
        }
        if(binding.subTitleEditText.text.toString().isEmpty()){
            Toast.makeText(requireContext(),"Enter sub title",Toast.LENGTH_LONG).show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            var notes= Notes()
            notes.title=binding.titleEditText.text.toString()
            notes.sub_title=binding.subTitleEditText.text.toString()
            notes.note_text=binding.descriptionEditText.text.toString()
            notes.data_time=currentDate
            if(color!=null) {
                notes.color_info = color!!.toString()
            }
            binding.dataTimeTextBox.text=currentDate
            if(imagePath==null){
                notes.img_path=null
            }else {
                notes.img_path = imagePath!!
                Log.i("Arjun","saved image path: "+imagePath!!.toString())
            }
            NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().insertNote(notes)
            notes.title=""
            notes.sub_title=""
            notes.note_text=""
            notes.data_time=""
            findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)
        }
    }

    private fun deleteNote(id:Int){
        CoroutineScope(Dispatchers.Main).launch {
            NoteDatabase.getNotesDatabase(requireContext()).getNotesDao().deleteNote(id)
             //findNavController().navigate(R.id.action_createNoteFragment_to_homeFragment)
        }
    }

    private fun insertImage(){
        if (EasyPermissions.hasPermissions(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            openGalleryForImage()
        }else{
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }


    }

    private val broadcastReceiver:BroadcastReceiver=object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val colorName = p1!!.getStringExtra("color")


            when(colorName!!){
                "note_color_1"->{
                    Log.d("Arjun","colorChanged")
                    color = resources.getColor(R.color.ColorBlue)
                    binding.colorView.setBackgroundColor(color!!)
                }
                "note_color_2"->{
                    Log.d("Arjun","colorChanged")
                    color=resources.getColor(R.color.ColorYellowNote)
                    binding.colorView.setBackgroundColor(color!!)
                }
                "note_color_3"->{
                    Log.d("Arjun","colorChanged")
                    color=resources.getColor(R.color.ColorPurpleNote)
                    binding.colorView.setBackgroundColor(color!!)
                }
                "note_color_4"->{
                    color=resources.getColor(R.color.ColorGreenNote)
                    binding.colorView.setBackgroundColor(color!!)
                }
                "note_color_5"->{
                    color=resources.getColor(R.color.ColorOrangeNote)
                    binding.colorView.setBackgroundColor(color!!)
                }
                "note_color_6"->{
                    color=resources.getColor(R.color.ColorBlackNote)
                    binding.colorView.setBackgroundColor(color!!)
                }
                "delete"->{
                    deleteNote(noteId)
                }

                "insertImage"->{
                    insertImage()
                }

            }
        }

    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            var imageUri = data?.data
            imagePath=getPathFromUri(imageUri!!)
            binding.imageView.setImageURI(imageUri)
            binding.deleteImageView.isVisible=true
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}