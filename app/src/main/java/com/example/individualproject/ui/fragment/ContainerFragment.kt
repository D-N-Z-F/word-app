package com.example.individualproject.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentContainerBinding
import com.example.individualproject.databinding.LayoutAlertViewBinding
import com.example.individualproject.databinding.LayoutFilterViewBinding
import com.example.individualproject.ui.adapter.TabAdapter
import com.example.individualproject.ui.viewmodel.ContainerViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ContainerFragment : Fragment() {
    private lateinit var binding: FragmentContainerBinding
    private val viewModel: ContainerViewModel by viewModels { ContainerViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentContainerBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
            On view created, set the Tab Adapter to the ViewPager, also set the
            OnQueryTextListener for the SearchView, and a onClickListener to the
            filter which will open a dialog for filter choices.
        */
        binding.run {
            vpTabs.adapter = TabAdapter(
            this@ContainerFragment, listOf(NewWordFragment(), CompletedWordFragment())
            )
            svWords.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(query: String?): Boolean {
                    query?.let {
                        viewModel.run {
                            sortType.value = "title"
                            sortOrder.value = "ASC"
                            setQueryValue(it)
                        }
                    }
                    return true
                }
            })
            ivFilter.setOnClickListener { showDialog() }
        }
        // This sets up the Tab Layout Names for easier use.
        TabLayoutMediator(binding.tlTabs, binding.vpTabs) { tab, position ->
            when(position) {
                0 -> tab.text = "New Words"
                1 -> tab.text = "Completed Words"
            }
        }.attach()
    }

    private fun showDialog() {
        val dialogBox = AlertDialog.Builder(requireContext()).create()
        val alertView = LayoutFilterViewBinding.inflate(layoutInflater)

        alertView.run {
            viewModel.run {
                sortType.value?.let { mrbTitle.isChecked = it == "title" }
                sortOrder.value?.let { mrbAsc.isChecked = it == "ASC" }
                mrbDate.isChecked = !mrbTitle.isChecked
                mrbDesc.isChecked = !mrbAsc.isChecked
                btnConfirm.setOnClickListener {
                    lifecycleScope.launch {
                        sortType.value = if(mrbTitle.isChecked) "title" else "dateCreated"
                        sortOrder.value = if(mrbAsc.isChecked) "ASC" else "DESC"
                        finish.emit(Unit)
                    }
                    dialogBox.dismiss()
                }
            }
        }
        dialogBox.setView(alertView.root)
        dialogBox.show()
    }
}