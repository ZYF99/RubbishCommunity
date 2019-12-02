package com.example.rubbishcommunity.ui.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.*
import android.media.Image
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import java.io.ByteArrayOutputStream
import android.graphics.ImageFormat


@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
	Glide.with(imageView.context).load(url)
		.placeholder(R.drawable.bg_404)
		.centerCrop()
		.into(imageView)
}

@BindingAdapter("imageUrlWithAddIcon")
fun loadImageWithAddIcon(imageView: ImageView, url: String?) {
	Glide.with(imageView.context).load(url)
		.placeholder(R.drawable.icon_add_pic)
		.centerCrop()
		.into(imageView)
}


@SuppressLint("NewApi")
@BindingAdapter("gender")
fun getGenderDrawable(imageView: ImageView, gender: String?) {
	when (gender) {
		"女" -> {
			Glide.with(imageView.context).load(R.drawable.icon_genderfemale)
				.placeholder(R.mipmap.ic_launcher)
				.dontAnimate()
				.into(imageView)
			
			imageView.imageTintList =
				ColorStateList.valueOf(
					ContextCompat.getColor(
						MyApplication.instance,
						R.color.colorFemale
					)
				)
			
		}
		else -> {
			Glide.with(imageView.context).load(R.drawable.icon_gendermale)
				.placeholder(R.mipmap.ic_launcher)
				.dontAnimate()
				.into(imageView)
			
			imageView.imageTintList =
				ColorStateList.valueOf(
					ContextCompat.getColor(
						MyApplication.instance,
						R.color.colorMale
					)
				)
			
		}
	}
	
	
}

const val COLOR_FormatI420 = 1
const val COLOR_FormatNV21 = 2

private fun isImageFormatSupported(image: Image): Boolean {
	val format = image.format
	when (format) {
		ImageFormat.YUV_420_888, ImageFormat.NV21, ImageFormat.YV12 -> return true
	}
	return false
}

fun getDataFromImage(image: Image, colorFormat: Int): ByteArray {
	if (colorFormat != COLOR_FormatI420 && colorFormat != COLOR_FormatNV21) {
		throw IllegalArgumentException("only support COLOR_FormatI420 " + "and COLOR_FormatNV21")
	}
	if (!isImageFormatSupported(image)) {
		throw RuntimeException("can't convert Image to byte array, format " + image.format)
	}
	val crop = image.cropRect
	val format = image.format
	val width = crop.width()
	val height = crop.height()
	val planes = image.planes
	val data = ByteArray(width * height * ImageFormat.getBitsPerPixel(format) / 8)
	val rowData = ByteArray(planes[0].rowStride)
	//if (VERBOSE) Log.v(TAG, "get data from " + planes.size + " planes")
	var channelOffset = 0
	var outputStride = 1
	for (i in planes.indices) {
		when (i) {
			0 -> {
				channelOffset = 0
				outputStride = 1
			}
			1 -> if (colorFormat == COLOR_FormatI420) {
				channelOffset = width * height
				outputStride = 1
			} else if (colorFormat == COLOR_FormatNV21) {
				channelOffset = width * height + 1
				outputStride = 2
			}
			2 -> if (colorFormat == COLOR_FormatI420) {
				channelOffset = (width.toDouble() * height.toDouble() * 1.25).toInt()
				outputStride = 1
			} else if (colorFormat == COLOR_FormatNV21) {
				channelOffset = width * height
				outputStride = 2
			}
		}
		val buffer = planes[i].buffer
		val rowStride = planes[i].rowStride
		val pixelStride = planes[i].pixelStride
/*		if (VERBOSE) {
			Log.v(TAG, "pixelStride $pixelStride")
			Log.v(TAG, "rowStride $rowStride")
			Log.v(TAG, "width $width")
			Log.v(TAG, "height $height")
			Log.v(TAG, "buffer size " + buffer.remaining())
		}*/
		val shift = if (i == 0) 0 else 1
		val w = width shr shift
		val h = height shr shift
		buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
		for (row in 0 until h) {
			val length: Int
			if (pixelStride == 1 && outputStride == 1) {
				length = w
				buffer.get(data, channelOffset, length)
				channelOffset += length
			} else {
				length = (w - 1) * pixelStride + 1
				buffer.get(rowData, 0, length)
				for (col in 0 until w) {
					data[channelOffset] = rowData[col * pixelStride]
					channelOffset += outputStride
				}
			}
			if (row < h - 1) {
				buffer.position(buffer.position() + rowStride - length)
			}
		}
		/*	if (VERBOSE) Log.v(TAG, "Finished reading data from plane $i")*/
	}
	return data
}

