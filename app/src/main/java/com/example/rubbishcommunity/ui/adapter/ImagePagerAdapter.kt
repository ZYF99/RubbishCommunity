package com.example.rubbishcommunity.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.utils.getImageFromServer
import com.zhangyf.zoomimageview.ZoomImageView

class ImagePagerAdapter(
	val context: Context,
	private val images: List<String>,
	private val onCellClick: () -> Unit
) : RecyclerView.Adapter<ImagePagerAdapter.ViewPagerViewHolder>() {
	
	class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val zoomImageView: ZoomImageView = itemView.findViewById(R.id.zoom_image)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.item_gallery_image, parent, false)
		return ViewPagerViewHolder(view)
	}
	
	override fun getItemCount(): Int = images.size
	
	override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
		Glide.with(context).load(getImageFromServer(images[position])).into(holder.zoomImageView)
		holder.zoomImageView.onClickAction = {
			onCellClick.invoke()
		}
	}
}

