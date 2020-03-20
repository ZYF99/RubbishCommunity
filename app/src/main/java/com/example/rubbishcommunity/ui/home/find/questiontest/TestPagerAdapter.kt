package com.example.rubbishcommunity.ui.home.find.questiontest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellTestBinding
import com.example.rubbishcommunity.model.TestCard
import com.example.rubbishcommunity.ui.widget.DragSeekBar
import com.github.chengang.library.TickView


class TestPagerAdapter(
	var pagerList: List<TestCard>,
	var onAnswerCorrect: ((Int) -> Unit)? = null,
	var onAnswerError: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.cell_test, parent, false)
		return TestPagerViewHolder(view)
	}
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val item = pagerList[position]
		(holder as TestPagerViewHolder).pagerBinding?.testCard = item
		initWithProgressBounce(holder)
		fun onProgressWithAnswerSortId(
			chooseSortId: Int,
			seekBar: DragSeekBar?,
			tickView: TickView?
		) = onProgressMax(position, chooseSortId, seekBar, tickView, answerSortId = item.sortId)
		holder.pagerBinding?.dragSort1?.onProgressMax =
			{ onProgressWithAnswerSortId(1, it, holder.pagerBinding?.tickView1) }
		holder.pagerBinding?.dragSort2?.onProgressMax =
			{ onProgressWithAnswerSortId(2, it, holder.pagerBinding?.tickView2) }
		holder.pagerBinding?.dragSort3?.onProgressMax =
			{ onProgressWithAnswerSortId(3, it, holder.pagerBinding?.tickView3) }
		holder.pagerBinding?.dragSort4?.onProgressMax =
			{ onProgressWithAnswerSortId(4, it, holder.pagerBinding?.tickView4) }
	}
	
	override fun getItemCount(): Int {
		return pagerList.size
	}
	
	class TestPagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val pagerBinding by lazy {
			DataBindingUtil.bind<CellTestBinding>(view)
		}
	}
	
	fun replaceData(newList: List<TestCard>) {
		pagerList = newList
		notifyDataSetChanged()
	}
	
	private fun onProgressMax(
		position: Int,
		chooseSortId: Int,
		seekBar: DragSeekBar?,
		tickView: TickView?,
		answerSortId: Int
	) {
		if (chooseSortId == answerSortId) {//答对题目的回调
			seekBar?.isEnabled = false
			seekBar?.thumb?.alpha = 0
			tickView?.visibility = View.VISIBLE
			tickView?.isChecked = true
			onAnswerCorrect?.invoke(position)
		} else {//答错
			seekBar?.progress = 0
			onAnswerError?.invoke(position)
		}
	}
	
	private fun initWithProgressBounce(holder: TestPagerViewHolder) {
		holder.pagerBinding?.apply {
			tickView1.visibility = View.GONE
			tickView1.isChecked = false
		}?.dragSort1?.initWithBounce()
		holder.pagerBinding?.apply {
			tickView2.visibility = View.GONE
			tickView2.isChecked = false
		}?.dragSort2?.initWithBounce()
		holder.pagerBinding?.apply {
			tickView3.visibility = View.GONE
			tickView3.isChecked = false
		}?.dragSort3?.initWithBounce()
		holder.pagerBinding?.apply {
			tickView4.visibility = View.GONE
			tickView4.isChecked = false
		}?.dragSort4?.initWithBounce()
	}
}