package com.example.rubbishcommunity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ItemFooterProgressbarBinding
import java.util.*
import kotlin.properties.Delegates

abstract class BaseRecyclerAdapter<Bean, Binding : ViewDataBinding>
	(
	private val layoutRes: Int,
	private val onCellClick: (Bean) -> Unit,
	val hasLoadMore:Boolean = false,
	var baseList: List<Bean> = emptyList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	var headerView: View? = null
	
	var onLoadMore by Delegates.observable(
		false,
		onChange = { _, _, _ ->
			notifyItemChanged(itemCount - 1)
		})
	
	companion object {
		private const val ITEM_TYPE_HEADER = 0
		private const val ITEM_TYPE_CONTENT = 1
		private const val ITEM_TYPE_LOAD_MORE = 2
		private const val FOOTER_SIZE = 1
	}
	
	inner class ContentViewHolder(
		itemView: View
	) : RecyclerView.ViewHolder(itemView) {
		val binding: Binding? by lazy {
			DataBindingUtil.bind<Binding>(itemView)
		}
	}
	
	inner class HeaderViewHolder(
		itemView: View
	) : RecyclerView.ViewHolder(itemView)
	
	
	inner class FooterViewHolder(
		itemView: View
	) : RecyclerView.ViewHolder(itemView) {
		fun onLoadMore(onLoadMore: Boolean) {
			itemView.visibility = if (onLoadMore) View.VISIBLE else View.GONE
		}
	}

/*	class BaseSimpleViewHolder<Binding : ViewDataBinding>(
		itemView: View
	) : RecyclerView.ViewHolder(itemView) {
		val binding: Binding? by lazy {
			DataBindingUtil.bind<Binding>(itemView)
		}
	}*/
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): RecyclerView.ViewHolder {
		
		return when (viewType) {
			ITEM_TYPE_CONTENT -> ContentViewHolder(
				LayoutInflater.from(parent.context).inflate(
					layoutRes,
					parent,
					false
				)
			)
			ITEM_TYPE_HEADER -> if (headerView != null) HeaderViewHolder(headerView!!)
			else throw RuntimeException("no headerView")
			ITEM_TYPE_LOAD_MORE -> FooterViewHolder(
				ItemFooterProgressbarBinding.inflate(
					LayoutInflater.from(parent.context), parent, false
				).root
			)
			else -> throw RuntimeException("no such ViewType")
		}
		
	}
	
	override fun getItemViewType(position: Int): Int {
		return if (headerView != null) {
			when {
				position == 0 -> ITEM_TYPE_HEADER
				position >= baseList.size + 1 -> ITEM_TYPE_LOAD_MORE
				else -> ITEM_TYPE_CONTENT
			}
		} else {
			if (position >= baseList.size) ITEM_TYPE_LOAD_MORE
			else ITEM_TYPE_CONTENT
		}
	}
	
	override fun getItemCount() =
		if (headerView == null) {
			baseList.size + if(hasLoadMore)FOOTER_SIZE else 0
		}
		else {
			baseList.size + 1 + if(hasLoadMore)FOOTER_SIZE else 0
		}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (getItemViewType(position)) {
			ITEM_TYPE_CONTENT -> {
				val pos = if (headerView != null) position - 1 else position
				holder as BaseRecyclerAdapter<*, *>.ContentViewHolder
				holder.binding?.root?.setOnClickListener {
					onCellClick(baseList[pos])
				}
				bindData(
					holder.binding as Binding,
					pos
				)
			}
			ITEM_TYPE_HEADER -> {
			}
			ITEM_TYPE_LOAD_MORE -> (holder as BaseRecyclerAdapter<*, *>.FooterViewHolder).onLoadMore(
				onLoadMore
			)
		}
		
	}
	
	abstract fun bindData(binding: Binding, position: Int)
	
	open fun replaceData(newList: List<Bean>) {
		if (baseList.isEmpty()) {
			baseList = newList
			notifyDataSetChanged()
		} else {
			if (newList.isNotEmpty()) {
				val diffResult =
					DiffUtil.calculateDiff(
						SingleBeanDiffCallBack(
							baseList,
							newList
						), true
					)
				baseList = newList
				diffResult.dispatchUpdatesTo(this)
			}
		}


/*		baseList = newList
		notifyDataSetChanged()*/
	}
	
	fun changeData(data: Bean, position: Int) {
		baseList = baseList.toMutableList().apply { set(position, data) }
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
}

class SingleBeanDiffCallBack<Bean>(
	val oldDatas: List<Bean>,
	val newDatas: List<Bean>
) : DiffUtil.Callback() {
	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return true
	}
	
	override fun getOldListSize(): Int {
		return oldDatas.size
	}
	
	override fun getNewListSize(): Int {
		return newDatas.size
	}
	
	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		val oldData = oldDatas[oldItemPosition]
		val newData = newDatas[newItemPosition]
		return oldData == newData
	}
}

const val ITEM_SWIPE_VERTICAL = 0
const val ITEM_SWIPE_HORIZONTAL = 1
const val ITEM_SWIPE_FREE = 2

fun RecyclerView.attachItemSwipe(decoration: Int, onSwipeStart: () -> Unit, onSwiped: () -> Unit) {
	
	//互换位置
	ItemTouchHelper(object : ItemTouchHelper.Callback() {
		override fun getMovementFlags(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder
		): Int = when (decoration) {
			ITEM_SWIPE_VERTICAL -> makeMovementFlags(
				ItemTouchHelper.UP or ItemTouchHelper.DOWN,
				ItemTouchHelper.UP or ItemTouchHelper.DOWN
			)
			ITEM_SWIPE_HORIZONTAL -> makeMovementFlags(
				ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
				ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
			)
			ITEM_SWIPE_FREE -> makeMovementFlags(
				ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN,
				ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN
			)
			else -> makeMovementFlags(
				ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN,
				ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN
			)
		}
		
		override fun isLongPressDragEnabled(): Boolean = true
		
		
		override fun onMoved(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			fromPos: Int,
			target: RecyclerView.ViewHolder,
			toPos: Int,
			x: Int,
			y: Int
		) {
		}
		
		
		override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
			if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
				onSwipeStart() //自定义的开启拖拽时的回调方法(这里可以替换为 refreshLayout.enabled = false）
			} else {
				onSwiped() //自定义的拖拽结束时的回调方法(这里可以替换为 refreshLayout.enabled = true）
			}
			super.onSelectedChanged(viewHolder, actionState)
		}
		
		override fun onMove(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			target: RecyclerView.ViewHolder
		): Boolean {
			(adapter as BaseRecyclerAdapter<*, *>).onItemMove(
				viewHolder.adapterPosition, target.adapterPosition
			)
			return true
		}
		
		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
		
		
	}).attachToRecyclerView(this)
	
	
}