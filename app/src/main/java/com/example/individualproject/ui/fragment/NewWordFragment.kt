package com.example.individualproject.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.individualproject.data.model.Word
import com.example.individualproject.databinding.FragmentNewWordBinding
import com.example.individualproject.ui.adapter.WordAdapter
import com.example.individualproject.ui.viewmodel.ContainerViewModel
import kotlinx.coroutines.launch

class NewWordFragment : Fragment() {
    private lateinit var binding: FragmentNewWordBinding
    private lateinit var adapter: WordAdapter
    private val viewModel: ContainerViewModel by viewModels(
        // Instead of making a new viewModel, reuse the viewModel of the parentFragment.
        ownerProducer = { requireParentFragment() }
    ) { ContainerViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewWordBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
            On view created, set up the adapter, and observe for any search queries or
            any filters.
        */
        setupAdapter()
        lifecycleScope.launch {
            viewModel.run {
                query.observe(viewLifecycleOwner) {
                    lifecycleScope.launch {
                        viewModel.getAllNewWords().collect {
                            adapter.setWords(it)
                            binding.tvNoNewWords.isInvisible = adapter.itemCount != 0
                        }
                    }
                }
                finish.collect {
                    val words = viewModel.sortWords(adapter.getWords())
                    adapter.setWords(words)
                }
            }
        }
        binding.mbAdd.setOnClickListener {
            findNavController().navigate(
                ContainerFragmentDirections.actionContainerToAdd("Add", 0)
            )
        }
    }

    private fun setupAdapter() {
        adapter = WordAdapter(emptyList())
        adapter.listener = object: WordAdapter.Listener {
            override fun onClick(word: Word) {
                findNavController().navigate(
                    ContainerFragmentDirections.actionContainerToView(word.id!!)
                )
            }
        }
        binding.run {
            rvNewWords.adapter = adapter
            rvNewWords.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}