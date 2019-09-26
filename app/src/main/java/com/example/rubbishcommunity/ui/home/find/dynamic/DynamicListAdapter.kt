package com.example.rubbishcommunity.ui.home.find.dynamic

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicListCellBinding
import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.ui.adapter.ImagePagerAdapter
import com.example.rubbishcommunity.ui.release.dynamic.FullyGridLayoutManager
import com.example.rubbishcommunity.ui.release.dynamic.MyGridLayoutManager
import com.example.rubbishcommunity.ui.widget.FullScreenDialog
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.pop_gallery.*

class DynamicListAdapter(data: MutableList<Dynamic>?) :
	BaseQuickAdapter<Dynamic, BaseViewHolder>(R.layout.cell_dynamic, data) {
	
	override fun convert(helper: BaseViewHolder, dynamic: Dynamic) {
		val binding: DynamicListCellBinding = DataBindingUtil.bind(helper.itemView)!!
		binding.dynamic = dynamic
		binding.recImg.run {
			layoutManager = MyGridLayoutManager(context, 3)
			adapter = DynamicListGridImageAdapter(
				dynamic.images,
				object : DynamicListGridImageAdapter.OnItemClickListener {
					override fun onItemClick(item: String, v: View) {
						showGallery(context, dynamic.images)
					}
				}
			)
		}
	}
	
	
	private fun showGallery(context: Context, imgList: List<String>) {
		val gallery = FullScreenDialog(context, R.layout.pop_gallery)
		gallery.show()
		gallery.viewpager.adapter = ImagePagerAdapter(context, imgList, gallery.viewpager)
		
		RxView.clicks(gallery.btn_back).doOnNext {
			gallery.dismiss()
		}.subscribe()
		
	}
}