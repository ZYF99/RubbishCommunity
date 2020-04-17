package com.example.rubbishcommunity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import java.util.*

abstract class HeaderRecyclerAdapter<Bean, Binding : ViewDataBinding, HeaderBinding : ViewDataBinding>
	(
	private val layoutRes: Int,
	private val onCellClick: (Bean) -> Unit,
	var baseList: List<Bean> = emptyList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	private var headerView: View? = null
	
	companion object {
		private const val ITEM_TYPE_HEADER = 0
		private const val ITEM_TYPE_CONTENT = 1
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
	) : RecyclerView.ViewHolder(itemView) {
		val binding: HeaderBinding? by lazy {
			DataBindingUtil.bind<HeaderBinding>(itemView)
		}
	}
	
/*	inner class FooterViewHolder(
		itemView: View
	) : RecyclerView.ViewHolder(itemView) {
		val binding: ItemFooterProgressbarBinding? by lazy {
			DataBindingUtil.bind<ItemFooterProgressbarBinding>(itemView)
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
			ITEM_TYPE_HEADER -> HeaderViewHolder(
				LayoutInflater.from(parent.context).inflate(
					R.layout.header_mine,
					parent,
					false
				)
			)
/*			ITEM_TYPE_LOAD_MORE -> FooterViewHolder(
				ItemFooterProgressbarBinding.inflate(
					LayoutInflater.from(parent.context), parent, false
				).root
			)*/
			else -> throw RuntimeException("no such ViewType")
		}

	}
	
	override fun getItemCount() = baseList.size
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (getItemViewType(position)) {
			ITEM_TYPE_CONTENT -> {
				holder as HeaderRecyclerAdapter<*, *, *>.ContentViewHolder
				holder.binding?.root?.setOnClickListener {
					onCellClick(baseList[position])
				}
				bindData(holder.binding as Binding?, position)
			}
			ITEM_TYPE_HEADER -> onHeaderInit()
/*			ITEM_TYPE_LOAD_MORE -> onLoadMore()*/
		}
	}
	
	override fun getItemViewType(position: Int): Int {
		return when {
			position == 0  -> ITEM_TYPE_HEADER
			position>0 && position<baseList.size -> ITEM_TYPE_CONTENT
			else -> ITEM_TYPE_HEADER
			/*else -> ITEM_TYPE_LOAD_MORE*/
		}
	}
	
	
	abstract fun bindData(binding: Binding?, position: Int)
	abstract fun onHeaderInit()
	
	
	open fun replaceData(newList: List<Bean>) {
		if (newList.isNotEmpty()) {
			val diffResult = DiffUtil.calculateDiff(SingleBeanDiffCallBack(baseList, newList), true)
			baseList = newList
			diffResult.dispatchUpdatesTo(this)
		}
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

/*class SingleBeanDiffCallBack<Bean>(
	val oldDatas:List<Bean>,
	val newDatas:List<Bean>
): DiffUtil.Callback() {
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
	
}*/


/*
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
	
	
}*/
