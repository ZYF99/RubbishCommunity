package com.example.rubbishcommunity.ui

import android.content.Context
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.adapter.ImagePagerAdapter
import com.example.rubbishcommunity.ui.widget.FullScreenDialog
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.pop_gallery.*

class CommonWidgetUtil {

}

fun showGallery(context: Context, imgList: List<String>, currentPosition: Int) {
	val gallery = FullScreenDialog(context, R.layout.pop_gallery)
	gallery.show()
	gallery.viewpager.adapter = ImagePagerAdapter(context, imgList, gallery.viewpager)
	gallery.viewpager.currentItem = currentPosition
	RxView.clicks(gallery.btn_back).doOnNext {
		gallery.dismiss()
	}.subscribe()
}