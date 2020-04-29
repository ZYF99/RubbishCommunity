package com.example.rubbishcommunity.ui.home.homepage

import android.util.Base64
import android.util.Base64.NO_WRAP
import com.example.rubbishcommunity.PersonOutClass
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HomePageBinding
import com.example.rubbishcommunity.ui.container.jumpToNewsDetail
import com.example.rubbishcommunity.ui.container.jumpToSearch
import com.example.rubbishcommunity.ui.home.MainActivity
import com.google.android.material.appbar.AppBarLayout
import java.util.*

class HomePageFragment : BindingFragment<HomePageBinding, HomePageViewModel>(
	HomePageViewModel::class.java, R.layout.fragment_home_page
) {
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	}
	
	override fun onSoftKeyboardClosed() {
	}
	
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		
		val person = PersonOutClass.Person
			.newBuilder()
			.setName("Fenrir")
			.setAge(34)
			.setAge1(46)
			.setAge2(56)
			.setAge3(16)
			.setAge4(56)
			.setAge5(86)
			.setAge6(66)
			.setAge7(56)
			.setAge8(86)
			.setAge9(54)
			.setAge10(66)
			.setAge11(66)
			.build()
		val a = Base64.encodeToString(person.toByteArray(),NO_WRAP)
		print(Arrays.toString(person.toByteArray()))
		
		binding.toolBar.setExpandedTitleColor(resources.getColor(R.color.colorTrans))
		
		binding.banner.setBannerPageClickListener { _, position ->
			if(viewModel.bannerList.value?.isNotEmpty() == true){
				jumpToNewsDetail(
					context!!,
					viewModel.bannerList.value?.get(position)
				)
			}
		}
		
		//监听AppBar滑动隐藏下面的BottomNavigationView
		binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
			(activity as? MainActivity)?.viewModel?.onAppBarOffsetChanged(
				verticalOffset,
				appbarHeight = binding.appbar.height.toFloat() - binding.toolbarHide.height
			)
		})
		
		//搜索栏
		binding.clSearch.setOnClickListener {
			//跳转至搜索界面
			jumpToSearch(context!!)
		}
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull {
			binding.swipe.isRefreshing = it
			binding.root.isEnabled = !it
		}
		
		viewModel.newsList.observeNonNull {
			(binding.recyclerNews.adapter as NewsListAdapter).replaceData(it)
		}
		
		viewModel.bannerList.observeNonNull {
			if (it.isNotEmpty()) {
				binding.banner.run {
					setPages(it.toList()) { BannerViewHolder(it) }
				}
			}
		}
		
		binding.recyclerNews.apply {
			adapter = NewsListAdapter { news ->
				jumpToNewsDetail(context, news)
			}
		}
		
		binding.swipe.setOnRefreshListener {
			viewModel.fetchNews(isRefresh = true)
		}
		
	}
	
	override fun initData() {
		viewModel.fetchNews(isRefresh = true)
	}
	
	override fun onResume() {
		binding.banner.start()
		super.onResume()
	}
	
	override fun onPause() {
		
		binding.banner.pause()
		super.onPause()
	}
	
	
}



