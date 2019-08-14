package com.example.rubbishcommunity.mainac.ui.find.dynamic

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.R

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/7/31 21:26
 */
class ImageGridAdapter(internal var context: Context, private val urlList: List<String>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return urlList.size
    }

    override fun getItem(position: Int): Any {
        return urlList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.griditem_image, null)
            holder = ViewHolder()
            holder.img = convertView!!.findViewById<ImageView>(R.id.gird_img)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        Glide.with(context).load(urlList[position]).crossFade().into(holder.img!!)

        return convertView
    }

    internal inner class ViewHolder {
        var img: ImageView? = null
    }


}