package ru.example.gbnotesapp.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.databinding.FragmentCreateNoteBinding
import ru.example.gbnotesapp.presentation.ViewModelFactory
import ru.example.gbnotesapp.presentation.viewmodels.CreateNoteViewModel
import ru.example.gbnotesapp.presentation.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var createNoteViewModelFactory: ViewModelFactory
    private val viewModel: CreateNoteViewModel by viewModels { createNoteViewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickButtonBack()
        clickButtonDone()

        binding.editTextTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Ничего не делать
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onTitleChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Ничего не делать
            }
        })

        binding.editTextContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Ничего не делать
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onContentChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Ничего не делать
            }
        })
    }


    private fun clickButtonBack() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_createNoteFragment_to_MainFragment)
        }
    }

    private fun clickButtonDone() {
        binding.buttonDone.setOnClickListener {
            lifecycleScope.launch {
                viewModel.onSaveNote()
            }
            findNavController().navigate(R.id.action_createNoteFragment_to_MainFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
    }
}
