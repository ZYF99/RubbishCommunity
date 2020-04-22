package com.example.rubbishcommunity.ui.home.mine.editinfo

import android.app.Activity
import android.content.Intent
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.EditInfoFragmentBinding
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.container.jumpToLogin
import com.example.rubbishcommunity.ui.container.jumpToPassword
import com.example.rubbishcommunity.ui.utils.showAvatarAlbum
import com.example.rubbishcommunity.ui.widget.DatePopView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia

class EditInfoFragment : BindingFragment<EditInfoFragmentBinding, EditInfoViewModel>(
	EditInfoViewModel::class.java, R.layout.fragment_edit_info
) {
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		
		//修改头像按钮
		binding.btnAvatar.setOnClickListener {
			showAvatarAlbum()
		}
		
		//修改昵称按钮
		binding.btnName.setOnClickListener {
			showEditDialog(
				"昵称",
				viewModel.name.value!!,
				12
			) {
				viewModel.name.value = it
				viewModel.editName()
			}
		}
		
		//修改性别按钮
		binding.btnGender.setOnClickListener {
			EditGenderDialog(context, viewModel.gender.value!!) {
				viewModel.gender.value = it
				viewModel.editGender()
			}.show()
		}
		
		//修改生日按钮
		binding.btnBirthday.setOnClickListener {
			DatePopView(context!!, viewModel.birthday.value!!) { year, month, day, birthLong ->
				viewModel.birthday.value = birthLong
				viewModel.editBirthDay()
			}.show()
		}
		
		//修改签名按钮
		binding.btnSignature.setOnClickListener {
			showEditDialog(
				"个人签名",
				viewModel.signature.value!!,
				40
			) {
				viewModel.signature.value = it
				viewModel.editSignature()
			}
		}
		
		//修改职业按钮
		binding.btnProfession.setOnClickListener {
			showEditDialog(
				"职业",
				viewModel.profession.value!!,
				20
			) {
				viewModel.profession.value = it
				viewModel.editProfession()
			}
		}
		
		//修改分类宣言按钮
		binding.btnAboutMe.setOnClickListener {
			showEditDialog(
				"分类宣言",
				viewModel.aboutMe.value!!,
				40
			) {
				viewModel.aboutMe.value = it
				viewModel.editAboutMe()
			}
		}
		
		//修改密码按钮
		binding.btnEditPassword.setOnClickListener { jumpToPassword(context!!) }
		
		//注销按钮
		binding.btnLogout.setOnClickListener { logout() }
		
	}
	
	override fun initData() {
		viewModel.initData()
	}
	
	//弹出修改文字按钮
	private fun showEditDialog(
		title: String,
		oldText: String,
		maxLength: Int,
		onFinish: (String) -> Unit
	) {
		EditTextDialog(
			context!!,
			title,
			oldText,
			maxLength
		) {
			onFinish(it)
		}.show()
	}
	
	private fun logout() {
		viewModel.logout()
			.doOnSuccess {
				//注销成功
				jumpToLogin(context!!)
			}
			.bindLife()
	}
	
	//选图后的回调
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		val images: List<LocalMedia>
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
				images = PictureSelector.obtainMultipleResult(data)
				viewModel.avatar.value = images[0].cutPath
				viewModel.editAvatar()
			}
		}
	}
	
}