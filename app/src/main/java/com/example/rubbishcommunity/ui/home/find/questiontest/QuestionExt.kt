package com.example.rubbishcommunity.ui.home.find.questiontest


import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.rubbishcommunity.ui.widget.DragSeekBar

fun DragSeekBar.initWithBounce() {
	isEnabled = true
	progress = 0
	thumb?.alpha = 255
	setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
		override fun onProgressChanged(
			seekBar: SeekBar,
			progress: Int,
			fromUser: Boolean
		) {
		}
		
		override fun onStartTrackingTouch(seekBar: SeekBar) {}
		override fun onStopTrackingTouch(seekBar: SeekBar) {
			if (seekBar.progress != seekBar.max) seekBar.progress = 0
			else onProgressMax?.invoke(seekBar as? DragSeekBar)
			
		}
	})
	
}