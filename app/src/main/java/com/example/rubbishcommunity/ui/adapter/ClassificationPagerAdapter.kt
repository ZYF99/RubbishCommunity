package com.example.rubbishcommunity.ui.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchKeyConclusionBinding
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

class ClassificationPagerAdapter(
	private val context: Context,
	private val list: List<SearchKeyConclusion>
) : PagerAdapter() {
	
	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		val root =
			LayoutInflater.from(context)
				.inflate(R.layout.cell_page_classification, container, false)
		container.addView(root)
		val binding: SearchKeyConclusionBinding? = DataBindingUtil.bind(root)
		binding?.searchKeyConclusion = list[position]
		RxView.clicks(root).throttleFirst(1, TimeUnit.SECONDS).doOnNext {
			startClickAnimation(root)
		}.subscribe()
		return root
	}
	
	override fun isViewFromObject(view: View, `object`: Any): Boolean {
		return view == `object`
	}
	
	override fun getCount(): Int {
		return list.size
	}
	
	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		container.removeView(`object` as View)
	}
	
	private fun startClickAnimation(root: View) {
		val animatorSet = AnimatorSet()
		val scaleY = ObjectAnimator.ofFloat(root, "scaleY", 1.toFloat(), 1.1.toFloat())
		val scaleX = ObjectAnimator.ofFloat(root, "scaleX", 1.toFloat(), 1.1.toFloat())
		val scaleXNew = ObjectAnimator.ofFloat(root, "scaleX", 1.1.toFloat(), 1.toFloat())
		val scaleYNew = ObjectAnimator.ofFloat(root, "scaleY", 1.1.toFloat(), 1.toFloat())
		animatorSet.duration = 300
		animatorSet.playTogether(scaleX, scaleY)
		animatorSet.playTogether(scaleXNew, scaleYNew)
		animatorSet.play(scaleYNew).after(scaleX)
		animatorSet.start()
		
	}
}