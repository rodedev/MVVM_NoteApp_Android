package com.example.noteapp.screens.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentHomeBinding
import com.example.noteapp.model.Note
import com.example.noteapp.screens.adapter.NoteAdapter
import com.example.noteapp.screens.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        setMenuOptions(menuHost)

        setUpRecyclerView()

        binding.fobAddNote.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewNoteFragment())
            //R.id.action_homeFragment_to_newNoteFragment
        }
    }

    private fun setMenuOptions(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
                val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
                mMenuSearch.isSubmitButtonEnabled = false
                mMenuSearch.setOnQueryTextListener(this@HomeFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        noteAdapter.onItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(
                    it
                )
            )
        }

        lifecycle.coroutineScope.launch {
            noteViewModel.getAllNotes().collect {
                noteAdapter.submitList(it)
                visibilityUI(it)
            }
        }

    }

    private fun visibilityUI(note: List<Note>) {
        if (note.isNotEmpty()) {
            binding.cardView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        /*query?.let {
            searchNotes(it)
        }*/
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            searchNotes(it)
        }
        return true
    }

    private fun searchNotes(query: String?) {
        val searchQuery = "%$query%"
        lifecycle.coroutineScope.launch {
            noteViewModel.searchNote(searchQuery).collect() {
                noteAdapter.submitList(it)
            }
        }
    }
}
