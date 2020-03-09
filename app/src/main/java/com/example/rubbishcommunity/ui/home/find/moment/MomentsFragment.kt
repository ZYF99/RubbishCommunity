package com.example.rubbishcommunity.ui.home.find.moment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentMomentsBinding
import com.example.rubbishcommunity.ui.adapter.EndlessRecyclerOnScrollListener
import com.example.rubbishcommunity.ui.base.setOnLoadMoreListener
import com.example.rubbishcommunity.ui.container.jumpToMomentDetail
import com.example.rubbishcommunity.ui.widget.UserInfoDialog
import timber.log.Timber

const val CLASSIFY_DYNAMIC = 1
const val CLASSIFY_RECOVERY = 2

class MomentsFragment(private val classify: Int = CLASSIFY_DYNAMIC) :
	BindingFragment<FragmentMomentsBinding, MomentsViewModel>(
		MomentsViewModel::class.java, R.layout.fragment_moments
	) {
	override fun initBefore() {
		binding.vm = viewModel
		viewModel.classify.value = classify
	}
	
	override fun initWidget() {
		
		viewModel.isRefreshing.observeNonNull { isRefreshing ->
			binding.refreshLayout.isRefreshing = isRefreshing
		}
		
		viewModel.isLoadingMore.observeNonNull {
			if (binding.recDynamic.adapter != null)
				(binding.recDynamic.adapter as MomentsListAdapter).onLoadMore = it
		}
		
		viewModel.momentList.observeNonNull {
			(binding.recDynamic.adapter as MomentsListAdapter).replaceData(it)
		}
		
		//下拉刷新监听
		binding.refreshLayout.setOnRefreshListener {
			viewModel.refreshMoments()
		}
		
		//上拉加载
		binding.recDynamic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				if (!recyclerView.canScrollVertically(1)
					&& viewModel.momentList.value?.size ?: 0 > 0)
					if (viewModel.isLoadingMore.value == false) {
						viewModel.loadMoreMoments()
					}
			}
		})
		
		//动态列表
		binding.recDynamic.run {
			layoutManager = LinearLayoutManager(context)
			adapter = MomentsListAdapter(
				{ moment ->
					//开启详情页
					jumpToMomentDetail(context, moment)
				},
				{ publisher ->
					UserInfoDialog(
						context,
						publisher
					) {
						//TODO 点击后跳转主页
					}.show()
				})
		}
	}
	
	override fun initData() {
		viewModel.refreshMoments()
	}
	
	
}