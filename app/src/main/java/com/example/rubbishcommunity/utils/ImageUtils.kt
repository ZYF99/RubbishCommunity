package com.example.rubbishcommunity.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R

@BindingAdapter("imageUrl")
fun loadImage(imageView:ImageView, url:String?){
	Glide.with(imageView.context).load(url)
		.placeholder(R.drawable.bg_404)
		.centerCrop()
		.dontAnimate()
		.into(imageView)
}

@SuppressLint("NewApi")
@BindingAdapter("gender")
fun getGenderDrawable(imageView: ImageView, gender: String?) {
	when (gender) {
		"女" -> {
			Glide.with(imageView.context).load(R.drawable.icon_genderfemale)
				.placeholder(R.mipmap.ic_launcher)
				.dontAnimate()
				.into(imageView)
			
			imageView.imageTintList =
				ColorStateList.valueOf(
					ContextCompat.getColor(
						MyApplication.instance,
						R.color.colorMale
					)
				)
			
		}
		else -> {
			Glide.with(imageView.context).load(R.drawable.icon_gendermale)
				.placeholder(R.mipmap.ic_launcher)
				.dontAnimate()
				.into(imageView)
			
			imageView.imageTintList =
				ColorStateList.valueOf(
					ContextCompat.getColor(
						MyApplication.instance,
						R.color.colorFemale
					)
				)
			
		}
	}
	
	
}