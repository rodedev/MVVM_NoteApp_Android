package com.example.noteapp.screens.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentNewNoteBinding
import com.example.noteapp.model.Note
import com.example.noteapp.screens.viewmodel.NoteViewModel
import com.example.noteapp.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.new_note_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.save_menu -> {
                        saveNote()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveNote() {

        if (validate()) {
            val note = Note(
                id = 0,
                noteTitle = binding.etTitle.text.toString().trim(),
                noteBody = binding.etBody.text.toString().trim()
            )
            noteViewModel.addNote(note)
            findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
        }
    }

    private fun validate(): Boolean {
        return when {
            binding.etTitle.text!!.isEmpty() -> binding.etTitle.run {
                requestFocus()
                "Se requiere un titulo"
            }
            binding.etBody.text!!.isEmpty() -> binding.etBody.run {
                requestFocus()
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