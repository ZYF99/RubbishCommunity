package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchKeyConclusionBinding
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit


class SearchListDialog(context: Context,
					   private val list:MutableList<SearchKeyConclusion>,
					   private val onAddDialogClickListener: OnSearchListDialogClickListener) :
	BasePopWindow(context){
	lateinit var view: View
	private lateinit var recycler: RecyclerView

	override fun getViewId(inflater: LayoutInflater): View {
		view = inflater.inflate(R.layout.dialog_searchlist, null)
		recycler = view.findViewById(R.id.list)
		return view
	}

	override fun initWindow() {
		super.initWindow()
		recycler.run {
			layoutManager = LinearLayoutManager(context)
			adapter = SearchListAdapter(context,list,object:SearchListAdapter.OnClickListener{
				override fun onCellClick(position: Int) {
				}
			})
		}
	}
	

	override fun dismiss() {
		super.dismiss()

	}

	//抛给view层的监听接口
	interface OnSearchListDialogClickListener {
		//列表单项选中
		fun onItemClick()
	}
}


class SearchListAdapter
	(
	private val mContext: Context,
	private val mDataset: MutableList<SearchKeyConclusion>,
	private val cellClickListener: OnClickListener
) : RecyclerView.Adapter<SearchListAdapter.SimpleViewHolder>() {
	interface OnClickListener {
		fun onCellClick(position: Int)
	}
	
	
	lateinit var binding: SearchKeyConclusionBinding
	
	class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_search_list, parent, false)
		return SimpleViewHolder(view)
	}
	
	override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
		binding = DataBindingUtil.bind(viewHolder.itemView)!!
		binding.searchKeyConclusion = mDataset[position]
		
		RxView.clicks(binding.cell).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			cellClickListener.onCellClick(position)
		}.subscribe()
		
	}
	
	override fun getItemCount(): Int {
		return mDataset.size
	}
	
}

