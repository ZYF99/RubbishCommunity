package com.example.rubbishcommunity.ui.home.mine.editinfo


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.EditInfoFragmentBinding
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.utils.showSingleAlbum
import com.example.rubbishcommunity.ui.widget.DatePopView
import com.example.rubbishcommunity.ui.widget.EditNameDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import java.text.Format
import java.text.SimpleDateFormat


class EditInfoFragment : BindingFragment<EditInfoFragmentBinding, EditInfoViewModel>(
	EditInfoViewModel::class.java, R.layout.fragment_edit_info
) {
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		
		//返回按钮
		binding.toolbar.toolbar.setNavigationOnClickListener {
			activity!!.finish()
		}
		
		//修改头像按钮
		binding.btnAvatar.setOnClickListener {
			showSingleAlbum()
		}
		
		//修改昵称按钮
		binding.btnName.setOnClickListener {
			EditNameDialog(
				context!!,
				viewModel.name.value!!
			){
				//修改完成按钮
				viewModel.name.value = it
				viewModel.editName()
			}.show()
		}
		
		//修改性别按钮
		binding.btnGender.setOnClickListener {
		
		}
		
		//修改生日按钮
		binding.btnBirthday.setOnClickListener {
			DatePopView(context!!,"1998年10月10日"){year,month,day ->
			
			}.show()
		}
		
		//修改签名按钮
		binding.btnSignature.setOnClickListener {
		
		}
		
		//修改职业按钮
		binding.btnProfession.setOnClickListener {
		
		}
		
		//修改分类宣言按钮
		binding.btnAboutMe.setOnClickListener {
		
		}
		
	}
	
	override fun initData() {
		viewModel.initData()
	}
	
	
	//选图后的回调
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		val images: List<LocalMedia>
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
				images = PictureSelector.obtainMultipleResult(data)
				viewModel.avatar.value = images[0].cutPath
			}
		}
	}
	
	
}