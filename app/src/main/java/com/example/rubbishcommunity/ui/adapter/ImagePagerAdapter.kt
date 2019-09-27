package com.example.rubbishcommunity.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide

class ImagePagerAdapter(val context:Context, private val imgs :List<String>, private val pager:ViewPager) : PagerAdapter() {

    private val imgViews = mutableListOf<ImageView>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount() = imgs.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        if(imgs.isNotEmpty()){
            val item = imgs[position]
            //存列表
            imgViews.add(imageView)
            //放在pager中
            pager.addView(imageView)
            Glide.with(context).load(item).centerInside().into(imageView)
        }

        return imageView

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
/*        if(imgViews.isNotEmpty())
        pager.removeView(imgViews[position])*/
    }


}