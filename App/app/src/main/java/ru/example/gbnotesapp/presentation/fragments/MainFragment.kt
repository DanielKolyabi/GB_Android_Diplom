package ru.example.gbnotesapp.presentation.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.databinding.FragmentMainBinding
import ru.example.gbnotesapp.presentation.ViewModelFactory
import ru.example.gbnotesapp.presentation.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreateNewNote.setOnClickListener{
            findNavController().navigate(R.id.action_MainFragment_to_CreateNoteFragment)
        }

        binding.buttonNavigationToFolders.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_ListFoldersFragment)
        }


    }






    companion object {
        fun newInstance() = MainFragment()
    }
}