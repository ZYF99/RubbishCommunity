package com.example.rubbishcommunity.mainac.ui.find


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *
 *
 * @author Zhangyf
 * @date  2019/8/14 20:41
 * @version 1.0
 */

class FindPagerAdapter(fragmentManager: FragmentManager, private val fragList:List<Pair<Fragment,String>>) :FragmentPagerAdapter(fragmentManager){
    override fun getItem(position: Int): Fragment {
        return fragList[position].first
    }

    override fun getCount(): Int {
        return fragList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragList[position].second
    }

}