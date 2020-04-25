package com.example.rubbishcommunity.ui.home.mine
import com.example.rubbishcommunity.databinding.BannerCellMachineBinding

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class MachineBannerAdapter(
	onCellClick: (FetchMachineInfoResultModel.MachineHeathInfo) -> Unit
) :
	BaseRecyclerAdapter<FetchMachineInfoResultModel.MachineHeathInfo, BannerCellMachineBinding>(
		R.layout.banner_cell_machine,
		onCellClick
	) {
	override fun bindData(binding: BannerCellMachineBinding, position: Int) {
		binding.machine = baseList[position]
		binding.imgCircle.setBackgroundResource(
			if (baseList[position].healthInfoResult.cpuUsageRate > 60) R.drawable.bg_circle_stroke_warning
			else R.drawable.bg_circle_stroke_normal
		)
	}
	
}