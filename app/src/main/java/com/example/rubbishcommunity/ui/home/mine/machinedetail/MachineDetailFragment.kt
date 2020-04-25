package com.example.rubbishcommunity.ui.home.mine.machinedetail

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentMachineDetailBinding
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.utils.globalGson


class MachineDetailFragment : BindingFragment<FragmentMachineDetailBinding, MachineDetailViewModel>(
	MachineDetailViewModel::class.java, R.layout.fragment_machine_detail
) {
	override fun initBefore() {
		binding.vm = viewModel
			viewModel.machineInfo.value =
			globalGson.getAdapter(FetchMachineInfoResultModel.MachineHeathInfo::class.java)
				.fromJson(activity?.intent?.getStringExtra("machineInfo"))
	}
	
	override fun initWidget() {
		
		viewModel.searchList.observeNonNull {
			(binding.recSearchHistory.adapter as SearchHistoryAdapter).replaceData(it)
		}
		
		binding.recSearchHistory.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = SearchHistoryAdapter {
			
			}
		}
		
		
	}
	
	override fun initData() {
		viewModel.fetchMachineHealthInfo()
		viewModel.fetchSearchHistory()
	}
}
