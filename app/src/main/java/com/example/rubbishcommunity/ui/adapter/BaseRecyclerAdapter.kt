package com.example.rubbishcommunity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.util.*


abstract class BaseRecyclerAdapter<Bean, Binding : ViewDataBinding>
	(
	private val layoutRes: Int,
	private val onCellClick: (Bean) -> Unit,
	var baseList: List<Bean> = emptyList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	class BaseSimpleViewHolder<Binding : ViewDataBinding>(
		itemView: View
	) : RecyclerView.ViewHolder(itemView) {
		val binding: Binding? by lazy {
			DataBindingUtil.bind<Binding>(itemView)
		}
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): RecyclerView.ViewHolder {
		return BaseSimpleViewHolder<Binding>(
			LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
		)
	}
	
	override fun getItemCount() = baseList.size
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		holder as BaseSimpleViewHolder<Binding>
		holder.binding!!.root.setOnClickListener {
			onCellClick(baseList[position])
		}
		bindData(holder.binding!!, position)
	}
	
	abstract fun bindData(binding: Binding, position: Int)
	
	fun replaceData(newList: List<Bean>) {
		baseList = newList
		notifyDataSetChanged()
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