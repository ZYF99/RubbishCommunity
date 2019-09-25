package com.example.rubbishcommunity.ui.release.dynamic


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.rubbishcommunity.R
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DateUtils
import com.luck.picture.lib.tools.StringUtils

const val TYPE_CAMERA = 1
const val TYPE_PICTURE = 2

class GridImageAdapter(
	private val context: Context,
	private val list: MutableList<LocalMedia>,
	private val mOnItemClickListener: OnItemClickListener,
	private val mOnAddPicClickListener: OnAddPicClickListener
) : RecyclerView.Adapter<GridImageAdapter.ViewHolder>() {
	
	private val mInflater: LayoutInflater = LayoutInflater.from(context)
	private var selectMax = 9
	
	override
	fun getItemCount(): Int {
		return if (list.size < selectMax) {
			list.size + 1
		} else {
			list.size
		}
	}
	
	interface OnAddPicClickListener {
		fun onAddPicClick()
	}
	
	interface OnItemClickListener {
		fun onItemClick(position: Int, v: View)
	}
	
	fun setSelectMax(selectMax: Int) {
		this.selectMax = selectMax
	}
	
	
	
	fun replaceDates(newList: MutableList<LocalMedia>) {
		list.clear()
		list.addAll(newList)
		notifyDataSetChanged()
	}
	
	
	inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
		val ivContent: ImageView = view.findViewById(R.id.iv_content)
		val linDel: LinearLayout = view.findViewById(R.id.lin_del)
		val tvDuration: TextView = view.findViewById(R.id.tv_duration)
		
	}
	
	override
	fun getItemViewType(position: Int): Int {
		return if (isShowAddItem(position)) {
			TYPE_CAMERA
		} else {
			TYPE_PICTURE
		}
	}
	
	/**
	 * 创建ViewHolder
	 */
	override
	fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
		val view = mInflater.inflate(R.layout.item_filter_image, viewGroup, false)
		return ViewHolder(view)
	}
	
	private fun isShowAddItem(position: Int): Boolean {
		val size = list.size
		return position == size
	}
	
	/**
	 * 设置值
	 */
	override
	fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
		//val binding: FilterImageListItemBinding = DataBindingUtil.bind(viewHolder.itemView)!!
		//少于8张，显示继续添加的图标
		if (getItemViewType(position) == TYPE_CAMERA) {
			viewHolder.ivContent.run {
				setImageResource(R.drawable.icon_add_pic)
				setOnClickListener { mOnAddPicClickListener.onAddPicClick() }
			}
			viewHolder.linDel.visibility = View.INVISIBLE
		} else {
			viewHolder.linDel.run {
				visibility = View.VISIBLE
				setOnClickListener {
					// 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
					// 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
					if (position != RecyclerView.NO_POSITION) {
						list.removeAt(position)
						notifyItemRemoved(position)
						notifyItemRangeChanged(position, list.size)
					}
				}
			}
			val item = list[position]
			val mimeType = item.mimeType
			val path: String =
				when {
					item.isCut && !item.isCompressed -> item.cutPath// 裁剪过
					item.isCompressed || item.isCut && item.isCompressed -> item.compressPath// 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
					else -> item.path// 原图
				}
			val pictureType = PictureMimeType.isPictureType(item.pictureType)
			
			val duration = item.duration
			viewHolder.tvDuration.visibility = when (pictureType) {
				PictureConfig.TYPE_VIDEO -> View.VISIBLE
				else -> View.GONE
			}
			
			when (mimeType) {
				PictureMimeType.ofAudio() -> {
					viewHolder.tvDuration.visibility = View.VISIBLE
					val drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio)
					StringUtils.modifyTextViewDrawable(viewHolder.tvDuration, drawable, 0)
				}
				else -> {
					val drawable = ContextCompat.getDrawable(context, R.drawable.video_icon)
					StringUtils.modifyTextViewDrawable(viewHolder.tvDuration, drawable, 0)
				}
			}

			
			viewHolder.tvDuration.text = DateUtils.timeParse(duration)
			if (mimeType == PictureMimeType.ofAudio()) {
				viewHolder.ivContent.setImageResource(R.drawable.audio_placeholder)
			} else {
				val options = RequestOptions()
					.centerCrop()
					.placeholder(R.color.bar_grey_90)
					.diskCacheStrategy(DiskCacheStrategy.ALL)
				Glide.with(context)
					.load(path)
					.apply(options)
					.into(viewHolder.ivContent)
			}
			//itemView 的点击事件
			viewHolder.itemView.setOnClickListener { v ->
				mOnItemClickListener.onItemClick(position, v)
			}
		}
	}
	
	
}






