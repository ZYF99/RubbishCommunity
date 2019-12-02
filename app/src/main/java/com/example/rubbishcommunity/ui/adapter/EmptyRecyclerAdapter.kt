package com.example.rubbishcommunity.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import java.util.*

/**
 * viewType--分别为item以及空view
 */
private const val VIEW_TYPE_ITEM = 1
private const val VIEW_TYPE_EMPTY = 0


abstract class EmptyRecyclerAdapter<Bean, Binding : ViewDataBinding>
constructor(
	private val layoutRes: Int,
	private val onCellClick: (Int) -> Unit
) : BaseRecyclerAdapter<Bean, Binding>(
	layoutRes,
	onCellClick
) {
	
	class BaseSimpleViewHolder<Binding : ViewDataBinding>(
		itemView: View
	) : RecyclerView.ViewHolder(itemView) {
		val binding: Binding? by lazy {
			DataBindingUtil.bind<Binding>(itemView)
		}
	}
	
	class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): RecyclerView.ViewHolder {
		return if (viewType == VIEW_TYPE_ITEM) {
			BaseSimpleViewHolder<Binding>(
				LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
			)
		} else {
			EmptyViewHolder(
				LayoutInflater.from(parent.context).inflate(
					R.layout.layout_empty,
					parent,
					false
				)
			)
		}
	}
	
	override fun getItemCount(): Int {
		//同时这里也需要添加判断，如果mData.size()为0的话，只引入一个布局，就是emptyView
		// 那么，这个recyclerView的itemCount为1
		if (baseList.size == 0) {
			return 1
		}
		//如果不为0，按正常的流程跑
		return baseList.size
		
	}
	
	override fun getItemViewType(position: Int): Int {
		//如果集合的长度为0时，使用emptyView的布局
		if (baseList.size == 0) {
			return VIEW_TYPE_EMPTY
		}
		//如果有数据，则使用ITEM的布局
		return VIEW_TYPE_ITEM
	}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is BaseSimpleViewHolder<*>) {
			val rootHolder = holder as BaseSimpleViewHolder<Binding>
			rootHolder.binding!!.root.setOnClickListener {
				onCellClick(position)
			}
			bindData(rootHolder.binding!!, position)
		}
	}
	
	
}
