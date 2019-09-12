package com.example.rubbishcommunity.ui.homepage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HotWordItemBinding

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/7/28 21:10
 */
class TagGridAdapter(val context: Context, private val wordList: List<String>) : BaseAdapter() {

    lateinit var binding: HotWordItemBinding


    override fun getCount(): Int {
        return wordList.size
    }

    override fun getItem(position: Int): Any {
        return wordList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.griditem_hottag, parent, false)
        binding.tvWord.text = wordList[position]

        return binding.root
    }


}

