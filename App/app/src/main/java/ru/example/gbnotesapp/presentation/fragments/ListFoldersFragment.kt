package ru.example.gbnotesapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.databinding.FragmentCreateNoteBinding
import ru.example.gbnotesapp.databinding.FragmentListFoldersBinding
import ru.example.gbnotesapp.presentation.ViewModelFactory
import ru.example.gbnotesapp.presentation.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ListFoldersFragment : Fragment() {

    private var _binding: FragmentListFoldersBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var listFoldersViewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { listFoldersViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListFoldersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickButtonBack()


    }


    private fun clickButtonBack() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_ListFoldersFragment_to_MainFragment)
        }
    }






    companion object {

    }
}
