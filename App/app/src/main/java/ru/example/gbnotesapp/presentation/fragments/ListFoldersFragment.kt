package ru.example.gbnotesapp.presentation.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.presentation.viewmodels.ListFoldersViewModel

@AndroidEntryPoint
class ListFoldersFragment : Fragment() {

    companion object {
        fun newInstance() = ListFoldersFragment()
    }

    private val viewModel: ListFoldersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list_folders, container, false)
    }
}