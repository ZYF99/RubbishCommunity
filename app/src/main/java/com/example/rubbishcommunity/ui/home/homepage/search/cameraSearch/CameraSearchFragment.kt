package com.example.rubbishcommunity.ui.home.homepage.search.cameraSearch

import android.graphics.Matrix
import android.view.Surface
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CameraSearchBinding
import java.util.concurrent.Executors
import android.content.Intent
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.utils.checkCameraPermission


class CameraSearchFragment : BindingFragment<CameraSearchBinding, CameraSearchViewModel>(
	CameraSearchViewModel::class.java, R.layout.fragment_camera_search
) {
	
	private val executor = Executors.newSingleThreadExecutor()
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	}
	
	override fun onSoftKeyboardClosed() {
	}
	
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		
		viewModel.thingList.observeNonNull {
			(binding.recThing.adapter as ThingListAdapter).replaceData(it)
		}
		
		//获取相机权限
		checkCameraPermission().doOnNext {
			binding.viewFinder.post { startCamera() }
		}.bindLife()
		
		
		
		binding.viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
			updateTransform()
		}
		
		binding.recThing.run {
			layoutManager = LinearLayoutManager(context)
			adapter = ThingListAdapter(
				viewModel.thingList.value!!
			) { position ->
				//返回结果给Search界面
				val intent = Intent(context!!, ContainerActivity::class.java)
				intent.putExtra("searchKey", viewModel.thingList.value!![position].keyword)
				activity!!.setResult(1, intent)
				activity!!.finish()
			}
		}
		
	}
	
	override fun initData() {
	
	}
	
	
	private fun startCamera() {
		
		// Create configuration object for the viewfinder use case
		val previewConfig = PreviewConfig.Builder().build()
		
		// Build the viewfinder use case
		val preview = Preview(previewConfig)
		
		val imageCaptureConfig = ImageCaptureConfig.Builder()
			.apply {
				// We don't set a resolution for image capture; instead, we
				// select a capture mode which will infer the appropriate
				// resolution based on aspect ration and requested mode
				setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
			}.build()
		
		val imageCapture = ImageCapture(imageCaptureConfig)
		// Setup image analysis pipeline that computes average pixel luminance
		val analyzerConfig = ImageAnalysisConfig.Builder().apply {
			/*			setTargetResolution(Size(1280, 720))*/
			// 旋转
			setImageReaderMode(
				ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
			)
		}.build()
		
		
		// Build the image analysis use case and instantiate our analyzer
		val analyzerUseCase = ImageAnalysis(analyzerConfig).apply {
			setAnalyzer(executor, ThingsAnalyzer {
				viewModel.identifyThingName(it)
			})
		}
		
		
		// Every time the viewfinder is updated, recompute layout
		preview.setOnPreviewOutputUpdateListener {
			// To update the SurfaceTexture, we have to remove it and re-add it
			val parent = binding.viewFinder.parent as ViewGroup
			parent.removeView(binding.viewFinder)
			parent.addView(binding.viewFinder, 0)
			binding.viewFinder.surfaceTexture = it.surfaceTexture
			updateTransform()
		}
		
		CameraX.bindToLifecycle(this, preview, imageCapture, analyzerUseCase)
	}
	
	private fun updateTransform() {
		val matrix = Matrix()
		
		// Compute the center of the view finder
		val centerX = binding.viewFinder.width / 2f
		val centerY = binding.viewFinder.height / 2f
		
		// Correct preview output to account for display rotation
		val rotationDegrees = when (binding.viewFinder.display.rotation) {
			Surface.ROTATION_0 -> 0
			Surface.ROTATION_90 -> 90
			Surface.ROTATION_180 -> 180
			Surface.ROTATION_270 -> 270
			else -> return
		}
		matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
		
		// Finally, apply transformations to our TextureView
		binding.viewFinder.setTransform(matrix)
	}
	
	
}

