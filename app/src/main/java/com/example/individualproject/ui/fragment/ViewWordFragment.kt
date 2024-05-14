package com.example.individualproject.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentViewWordBinding
import com.example.individualproject.databinding.LayoutAlertViewBinding
import com.example.individualproject.ui.viewmodel.ViewWordViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewWordFragment : Fragment() {
    private lateinit var binding: FragmentViewWordBinding
    private val viewModel: ViewWordViewModel by viewModels { ViewWordViewModel.Factory }
    private val args: ViewWordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewWordBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
            On View Created, get the Word details based on the ID and set up the details,
            and the functionality for Complete, Update, and Delete button.
        */
        lifecycleScope.launch {
            viewModel.run {
                getWordById(args.id)
                word.observe(viewLifecycleOwner) {
                    binding.run {
                        tvTitle.text = it.title
                        tvDef.text = it.definition
                        tvSyn.text = it.synonyms.ifBlank { "N/A" }
                        tvDetails.text = it.details.ifBlank { "N/A" }
                        if(it.isCompleted) { btnComplete.isInvisible = true }
                    }
                }
                showSnackbar.observe(viewLifecycleOwner) {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
                finish.collect {
                    findNavController().popBackStack(
                        findNavController().graph.startDestinationId, false
                    )
                }
            }
        }
        binding.run {
            lifecycleOwner = viewLifecycleOwner
            btnComplete.setOnClickListener { showDialog("Complete") }
            btnDelete.setOnClickListener { showDialog("Delete") }
            btnUpdate.setOnClickListener {
                findNavController().navigate(
                    ViewWordFragmentDirections.actionViewToEdit("Edit", args.id)
                )
            }
        }
    }

    private fun showDialog(type: String) {
        val dialogBox = AlertDialog.Builder(requireContext()).create()
        val alertView = LayoutAlertViewBinding.inflate(layoutInflater)

        alertView.run {
            tvHeader.text = getString(R.string.header, type)
            btnNegative.setOnClickListener { dialogBox.dismiss() }
            btnPositive.setOnClickListener {
                if(type == "Delete") viewModel.deleteWord()
                else if(type == "Complete") viewModel.completeWord()
                dialogBox.dismiss()
            }
        }
        dialogBox.setView(alertView.root)
        dialogBox.show()
    }
}