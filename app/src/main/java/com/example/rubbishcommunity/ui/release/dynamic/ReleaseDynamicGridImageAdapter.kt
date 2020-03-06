package com.example.rubbishcommunity.ui.release.dynamic

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FilterImageListItemBinding
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DateUtils
import com.luck.picture.lib.tools.StringUtils


const val TYPE_CAMERA = 1
const val TYPE_PICTURE = 2

/**
 * 添加图片的RecyclerView适配器
 * */
class ReleaseDynamicGridImageAdapter(
	private val context: Context,
	private val imgList: MutableList<LocalMedia>,
	private val onAddPicClick: () -> Unit,
	private val onGridItemClick: (Int, View) -> Unit,
	private val onGridItemDelClick: (Int) -> Unit
) : BaseRecyclerAdapter<LocalMedia, FilterImageListItemBinding>(
	R.layout.item_filter_grid_image,
	{}
) {
	private val selectMax = 9
	
	override
	fun getItemCount(): Int {
		return if (baseList.size < selectMax) {
			baseList.size + 1
		} else {
			baseList.size
		}
	}
	
	override
	fun getItemViewType(position: Int): Int {
		return if (isShowAddItem(position)) {
			TYPE_CAMERA
		} else {
			TYPE_PICTURE
		}
	}
	
	private fun isShowAddItem(position: Int): Boolean {
		val size = baseList.size
		return position == size
	}
	
	override fun bindData(binding: FilterImageListItemBinding, position: Int) {
		//少于8张，显示继续添加的图标
		if (getItemViewType(position) == TYPE_CAMERA) {
			binding.ivContent.run {
				setImageResource(R.drawable.icon_add_pic)
				setOnClickListener {
					onAddPicClick()
				}
			}
			binding.linDel.visibility = View.INVISIBLE
		} else {
			binding.linDel.run {
				visibility = View.VISIBLE
				setOnClickListener {
					// 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
					// 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
					if (position != RecyclerView.NO_POSITION) {
						replaceData(baseList.apply { toMutableList().removeAt(position) })
						onGridItemDelClick(position)
					}
				}
			}
			val item = baseList[position]
			val mimeType = item.mimeType
			val path: String =
				when {
					item.isCut && !item.isCompressed -> item.cutPath// 裁剪过
					item.isCompressed || item.isCut && item.isCompressed -> item.compressPath// 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
					else -> item.path// 原图
				}
			val pictureType = PictureMimeType.isPictureType(item.pictureType)
			
			val duration = item.duration
			binding.tvDuration.visibility = when (pictureType) {
				PictureConfig.TYPE_VIDEO -> View.VISIBLE
				else -> View.GONE
			}
			
			when (mimeType) {
				PictureMimeType.ofAudio() -> {
					binding.tvDuration.visibility = View.VISIBLE
					val drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio)
					StringUtils.modifyTextViewDrawable(binding.tvDuration, drawable, 0)
				}
				else -> {
					val drawable = ContextCompat.getDrawable(context, R.drawable.video_icon)
					StringUtils.modifyTextViewDrawable(binding.tvDuration, drawable, 0)
				}
			}
			binding.tvDuration.text = DateUtils.timeParse(duration)
			if (mimeType == PictureMimeType.ofAudio()) {
				binding.ivContent.setImageResource(R.drawable.audio_placeholder)
			} else {
				val options = RequestOptions()
					.centerCrop()
					.placeholder(R.color.bar_grey_90)
					.diskCacheStrategy(DiskCacheStrategy.ALL)
				Glide.with(context)
					.load(path)
					.apply(options)
					.into(binding.ivContent)
			}
			//itemView 的点击事件
			binding.root.setOnClickListener { v ->
				onGridItemClick(position, v)
			}
		}
		
	}
	
}




