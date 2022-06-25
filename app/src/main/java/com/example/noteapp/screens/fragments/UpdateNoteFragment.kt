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
            val title = binding.etTitleUpdate.text.toString().trim()
            val body = binding.etBodyUpdate.text.toString().trim()

            if (title.isNotEmpty()) {
                val note = Note(currentNote.id, title, body)
                noteViewModel.updateNote(note)
                findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Enter a note title please", Toast.LENGTH_LONG).show()
            }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_menu -> {
                deleteNote()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}