package com.example.rubbishcommunity.ui.home.find.moment


import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellMomentBinding
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.example.rubbishcommunity.ui.adapter.EmptyRecyclerAdapter
import com.example.rubbishcommunity.ui.adapter.LoadMoreRecyclerAdapter
import com.example.rubbishcommunity.ui.utils.showGallery


class MomentsListAdapter(
	onCellClick: (MomentContent) -> Unit,
	val onHeaderClick: (SimpleProfileResp) -> Unit
) : LoadMoreRecyclerAdapter<MomentContent, CellMomentBinding>(
	R.layout.cell_moment,
	onCellClick
) {
	override fun bindData(binding: CellMomentBinding, position: Int) {
		val moment = baseList[position]
		binding.moment = moment
		
		//宫格图
		binding.recImg.run {
			layoutManager = getMomentsPictureLayoutManager(context, moment.pictures.size)
			adapter = MomentsListGridImageAdapter(moment.pictures) { imgRecPosition ->
				showGallery(
					context,
					moment.pictures,
					imgRecPosition
				)
			}
		}
		
		val ii = SimpleProfileResp(
			avatar = "http://image.upuphub.com/LeoWong@upuphub.com/1583047084264",
			openId = "fdgsfd",
			name = "sdfds",
			signature = "fgfdg",
			country = "dsf",
			province = "dfsdf",
			city = "dsfd",
			age = 10
		)
		
		val likeList =
			listOf(ii, ii, ii, ii, ii, ii, ii, ii)//moment.likeList.map { it.commentator }
		
		//点赞头像列表
		binding.recLike.run {
			val manager = LinearLayoutManager(context)
			manager.orientation = LinearLayoutManager.HORIZONTAL
			layoutManager = manager
			adapter = LikeListAdapter(likeList) {
				//点赞头像的点击事件
			}
			if (itemDecorationCount <= 0) {
				addItemDecoration(object : RecyclerView.ItemDecoration() {
					override fun getItemOffsets(
						outRect: Rect,
						view: View,
						parent: RecyclerView,
						state: RecyclerView.State
					) {
						super.getItemOffsets(outRect, view, parent, state)
						if (parent.getChildAdapterPosition(view) != (likeList.size - 1))
							outRect.right = -6
					}
				})
			}
		}
		val likeSize = likeList.size
		binding.likeString = when {
			likeSize > 10 -> "等共${likeSize}人觉得很赞"
			likeSize in 1..10 -> "共${likeSize}人觉得很赞"
			else -> "没得人点赞它"
		}
		
		binding.cellAuthorPortrait.setOnClickListener {
			onHeaderClick(moment.publisher)
		}
	}
}

fun getMomentsPictureLayoutManager(context: Context, size: Int) =
	GridLayoutManager(
		context,
		when {
			size % 3 == 0 -> 3
			size == 1 -> 1
			size == 2 -> 2
			size == 4 -> 2
			else -> 3
		}
	)

