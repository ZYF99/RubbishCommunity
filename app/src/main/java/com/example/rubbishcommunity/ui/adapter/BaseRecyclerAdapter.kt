package com.example.rubbishcommunity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<Bean, Binding : ViewDataBinding>
constructor(
	private val layoutRes: Int,
	private val onCellClick: (Int) -> Unit
) : RecyclerView.Adapter<BaseRecyclerAdapter.BaseSimpleViewHolder<Binding>>() {
	
	abstract val baseList: MutableList<Bean>
	
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
	): BaseSimpleViewHolder<Binding> {
		return BaseSimpleViewHolder(
			LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
		)
	}
	
	override fun getItemCount() = baseList.size
	
	
	override fun onBindViewHolder(holder: BaseSimpleViewHolder<Binding>, position: Int) {
		bindData(holder.binding!!, position)
		holder.binding!!.root.setOnClickListener {
			onCellClick(position)
		}
	}
	
	abstract fun bindData(binding: Binding, position: Int)
	
	fun replaceData(newList: MutableList<Bean>) {
		baseList.run {
			clear()
			notifyDataSetChanged()
			addAll(newList)
			notifyDataSetChanged()
		}
		
	}
	
	fun addData(data: Bean) {
		baseList.add(data)
		
		notifyItemInserted(baseList.size - 1)
	}
	
	fun removeData(position: Int) {
		baseList.removeAt(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, baseList.size)
	}
	
}