package com.example.rubbishcommunity.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.databinding.ItemFooterProgressbarBinding
import java.lang.Exception
import java.util.*
import kotlin.properties.Delegates

abstract class LoadMoreRecyclerAdapter<Bean, Binding : ViewDataBinding>
	(
	private val layoutRes: Int,
	private val onCellClick: (Bean) -> Unit,
	private val loadMoreEnabled: Boolean = true,
	var baseList: List<Bean> = emptyList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	private var headerView: View? = null
	private var headerContentMap: Map<String, String>? = null
	var onLoadMore by Delegates.observable(
		true,
		onChange = { _, _, _ ->
			notifyItemChanged(itemCount - 1)
		})
	
	abstract class BaseSimpleViewHolder<BaseBinding : ViewDataBinding>(
		itemView: View
	) : RecyclerView.ViewHolder(itemView) {
		val binding: BaseBinding? by lazy {
			DataBindingUtil.bind<BaseBinding>(itemView)
		}
		
		open fun onLoadMore(onLoadMore: Boolean) {}
		
		open fun onHeaderInit(contentMap: Map<String, String>) {}
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): RecyclerView.ViewHolder {
		
		return when (viewType) {
			ITEM_TYPE_HEADER -> {
				HeaderVH<ViewDataBinding>(
					headerView!!
				)
			}
			
			ITEM_TYPE_CONTENT -> ContentVH<Binding>(
				LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
			)
			
			ITEM_TYPE_LOAD_MORE -> {
				ProgressBarVH(
					ItemFooterProgressbarBinding.inflate(
						LayoutInflater.from(parent.context), parent, false
					)
				)
			}
			else -> throw RuntimeException("no such ViewType")
		}
		
	}
	
	override fun getItemCount(): Int {
		return when {
			headerView != null -> when {
				loadMoreEnabled -> baseList.size + 1 + FOOTER_SIZE
				else -> baseList.size + 1
			}
			else -> when {
				loadMoreEnabled -> baseList.size + FOOTER_SIZE
				else -> baseList.size
			}
		}
	}
	
	override fun getItemViewType(position: Int): Int {
		return if (headerView != null) {
			if (loadMoreEnabled) {
				when {
					position == 0 -> ITEM_TYPE_HEADER
					position >= baseList.size + 1 -> ITEM_TYPE_LOAD_MORE
					else -> ITEM_TYPE_CONTENT
				}
			} else {
				when (position) {
					0 -> ITEM_TYPE_HEADER
					else -> ITEM_TYPE_CONTENT
				}
			}
		} else {
			if (loadMoreEnabled) {
				if (position >= baseList.size) ITEM_TYPE_LOAD_MORE
				else ITEM_TYPE_CONTENT
			} else {
				ITEM_TYPE_CONTENT
			}
		}
	}
	
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		holder as BaseSimpleViewHolder<Binding>
		when {
			getItemViewType(position) == ITEM_TYPE_CONTENT -> {
				bindData(holder.binding!!, position)
				holder.binding!!.root.setOnClickListener {
					onCellClick(baseList[position])
				}
			}
			getItemViewType(position) == ITEM_TYPE_HEADER -> holder.onHeaderInit(
				headerContentMap ?: mapOf()
			)
			else -> {if(loadMoreEnabled)holder.onLoadMore(onLoadMore)}
		}
	}
	
	fun setHeaderView(view: View, headerContentMap: Map<String, String>) {
		this.headerContentMap = headerContentMap
		headerView = view
		notifyItemInserted(0)
	}
	
	class HeaderVH<HeaderBinding : ViewDataBinding>(view: View) :
		BaseSimpleViewHolder<HeaderBinding>(view) {
		override fun onHeaderInit(contentMap: Map<String, String>) {
		
		}
	}
	
	class ContentVH<ContentBinding : ViewDataBinding>(view: View) :
		BaseSimpleViewHolder<ContentBinding>(view)
	
	
	class ProgressBarVH(progressBinding: ItemFooterProgressbarBinding) :
		BaseSimpleViewHolder<ItemFooterProgressbarBinding>(progressBinding.root) {
		
		override fun onLoadMore(onLoadMore: Boolean) {
			binding?.footerProgressBar?.visibility = if (onLoadMore) View.VISIBLE else View.GONE
		}
		
	}
	
	
	abstract fun bindData(binding: Binding, position: Int)
	
	fun replaceData(newList: List<Bean>) {
		baseList = newList
		notifyDataSetChanged()
	}
	
	fun changeData(data:Bean,position:Int){
		baseList = baseList.toMutableList().apply { set(position,data) }
		notifyItemChanged(position)
	}
	
	fun onItemMove(fromPosition: Int, toPosition: Int) {
		//交换位置
		if (toPosition >= 0 && toPosition < baseList.size) {
			try {
				Collections.swap(baseList, fromPosition, toPosition)
				notifyItemMoved(fromPosition, toPosition)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}
	
	companion object {
		private const val ITEM_TYPE_HEADER = 0
		private const val ITEM_TYPE_CONTENT = 1
		private const val ITEM_TYPE_LOAD_MORE = 2
		private const val FOOTER_SIZE = 1
	}
}