package com.example.rubbishcommunity.ui.search.cameraSearch


import android.graphics.*
import android.util.Base64
import android.util.Base64.NO_WRAP
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.util.concurrent.TimeUnit
import android.media.Image
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory


class ThingsAnalyzer(
	val onImageLoad: (String) -> Unit
) : ImageAnalysis.Analyzer {
	private var lastAnalyzedTimestamp = 0L
	
	
	override fun analyze(image: ImageProxy, rotationDegrees: Int) {
		val currentTimestamp = System.currentTimeMillis()
		// Calculate the average luma no more often than every second
		if (currentTimestamp - lastAnalyzedTimestamp >=
			TimeUnit.SECONDS.toMillis(5)
		) {
			val byteArray = image.image?.toByteArray()
			
			val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
			
			//val imgStr = "data:image/jpeg;base64," + Base64.encodeToString(byteArray, NO_WRAP)
			val imgStr = Base64.encodeToString(byteArray, NO_WRAP)
			onImageLoad(imgStr)
			
			//Log.d("!!","$imgStr")
			lastAnalyzedTimestamp = currentTimestamp
		}
	}
}

fun Image.toByteArray(): ByteArray {
	val yBuffer = planes[0].buffer // Y
	val uBuffer = planes[1].buffer // U
	val vBuffer = planes[2].buffer // V
	
	val ySize = yBuffer.remaining()
	val uSize = uBuffer.remaining()
	val vSize = vBuffer.remaining()
	
	val nv21 = ByteArray(ySize + uSize + vSize)
	
	//U and V are swapped
	yBuffer.get(nv21, 0, ySize)
	vBuffer.get(nv21, ySize, vSize)
	uBuffer.get(nv21, ySize + vSize, uSize)
	
	val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
	val out = ByteArrayOutputStream()
	yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
	return out.toByteArray()
}



