package com.example.audioplayer

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagesAdapter(
    fragment: Fragment,
    val fragmentList: MutableList<Fragment>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

}