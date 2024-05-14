package com.example.individualproject.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// The Tab Adapter, over here we can have multiple fragments inside a single ViewPager.
class TabAdapter(
    fragment: Fragment, private val tabs: List<Fragment>
): FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int) = tabs[position]

    override fun getItemCount() = tabs.size

}