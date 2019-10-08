package com.example.rubbishcommunity.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.R

@BindingAdapter("imageUrl")
fun loadImage(imageView:ImageView, url:String?){
	Glide.with(imageView.context).load(url)
		.placeholder(R.drawable.bg_404)
		.centerCrop()
		.dontAnimate()
		.into(imageView)
}
