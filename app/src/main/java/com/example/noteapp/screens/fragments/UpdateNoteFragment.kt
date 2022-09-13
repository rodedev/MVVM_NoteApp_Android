package com.example.noteapp.screens.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentUpdateNoteBinding
import com.example.noteapp.model.Note
import com.example.noteapp.screens.viewmodel.NoteViewModel
import com.example.noteapp.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNoteFragment : Fragment() {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateNoteFragmentArgs by navArgs()
    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var currentNote: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentNote = args.note

        binding.etTitleUpdate.setText(currentNote.noteTitle)
        binding.etBodyUpdate.setText(currentNote.noteBody)

        binding.fbDone.setOnClickListener {

            if (validate()) {
                val note = Note(
                    id = currentNote.id,
                    noteTitle = binding.etTitleUpdate.text.toString().trim(),
                    noteBody = binding.etBodyUpdate.text.toString().trim()
                )
                noteViewModel.updateNote(note)
                findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
            }
            /*else {
                Toast.makeText(context, "Enter a note title please", Toast.LENGTH_LONG).show()
            }*/
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_menu -> {
                deleteNote()
                true
            }
            else -> false
        }

    }

    private fun deleteNote() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                noteViewModel.deleteNote(currentNote)
                findNavController().navigate(
                    R.id.action_updateNoteFragment_to_homeFragment
                )
            }
            setNegativeButton("CANCEL", null)
        }.create().show()
    }

    private fun validate(): Boolean {
        return when {
            binding.etTitleUpdate.text!!.isEmpty() -> binding.etTitleUpdate.run {
                "Se requiere un titulo"
            }
            binding.etBodyUpdate.text!!.isEmpty() -> binding.etBodyUpdate.run {
                "Describa la nota"
            }
            else -> ""
        }.run {
            if (this.isNotEmpty()) {
                requireView().showSnackbar(this)
            }
            this.isEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}