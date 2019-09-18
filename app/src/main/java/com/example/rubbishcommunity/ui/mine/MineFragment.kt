package com.example.rubbishcommunity.ui.mine

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MineBinding
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.ui.guide.LGuideActivity
import com.jakewharton.rxbinding2.view.RxView

class MineFragment : BindingFragment<MineBinding, MineViewModel>(
	MineViewModel::class.java, R.layout.frag_mine
) {
	
	override fun initBefore() {
	
	}
	
	override fun initWidget(view: View) {
		binding.vm = viewModel
		
		viewModel
			.getUserInfo()
			.doOnSuccess {
				viewModel.userInfo.postValue(it.data)
			}.bindLife()
		
		viewModel.userInfo.observe {
			if (it != null) {
				binding.userName = it.name
				binding.content = it.content
				binding.city = it.city
				Glide.with(context).load(it.portrait).crossFade().into(binding.iv)
				Glide.with(context).load(it.portrait).crossFade().into(binding.potrait)
			}
		}
		
		
		binding.collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
		Glide.with(context).load(R.drawable.bg).crossFade().into(binding.iv)
		Glide.with(context).load(R.drawable.bg).crossFade().into(binding.potrait)
		
		
		RxView.clicks(binding.btnLogout)
			.doOnNext {
			
/*				//注销流
				viewModel.getUserInfo().doOnSuccess {
					when (it.meta.code) {
						200 -> {
							//注销成功
							saveLoginState(false)
							startActivity(Intent(context, LGuideActivity::class.java))
							(context as Activity).finish()
						}
						else -> {
							//注销失败
							
						}
					}
				}*/
				
				//模拟注销成功
				//注销成功
				saveLoginState(false)
				startActivity(Intent(context, LGuideActivity::class.java))
				(context as Activity).finish()
				
				
				
			}.bindLife()
	}
	
	override fun initData() {
	
	}


/*    //create pop of jobPicker
    private fun createJobPop() {
        hideKeyboard()
        binding.btnjobdown.setImageResource(R.drawable.icn_chevron_down_black)
        val pop = context?.let { ContractDialog(it) }
        pop?.show()
        //pop click listener
        pop?.setOnClickListener(object : BottomDialogView.OnMyClickListener {
            @SuppressLint("SetTextI18n")
            override fun onFinishClick() {
                binding.tvWork.text = pop.job
            }
        })

    }*/
	
	
}