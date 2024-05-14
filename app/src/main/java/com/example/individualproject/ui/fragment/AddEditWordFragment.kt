package com.example.individualproject.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentAddEditWordBinding
import com.example.individualproject.ui.viewmodel.AddEditWordViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddEditWordFragment : Fragment() {
    private lateinit var binding: FragmentAddEditWordBinding
    private val viewModel: AddEditWordViewModel by viewModels { AddEditWordViewModel.Factory }
    private val args: AddEditWordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditWordBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
            On view created, check if user wants to edit, if yes, then setup the word details,
            then observe the user's inputs, on submit, update/add the word accordingly.
        */
        lifecycleScope.launch {
            viewModel.run {
                if(args.type == "Edit") {
                    getWordById(args.id)
                    word.observe(viewLifecycleOwner) { setWord() }
                }
                showSnackbar.observe(viewLifecycleOwner) {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
                finish.collect { findNavController().popBackStack() }
            }
        }
        binding.viewModel = viewModel
        binding.run {
            lifecycleOwner = viewLifecycleOwner
            mbAddEdit.text = args.type
            tvAddEdit.text = getString(R.string.add_edit_details, args.type)
            mbAddEdit.setOnClickListener {
                viewModel?.run { if(args.type == "Edit") updateWord() else addWord() }
            }
        }
    }
}