package com.example.rubbishcommunity.ui.home.find.moment


import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellMomentBinding
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.model.api.moments.COMMENT_LIKE
import com.example.rubbishcommunity.model.api.moments.MomentComment
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.persistence.getLocalOpenId
import com.example.rubbishcommunity.persistence.getUserSimpleProfile
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.example.rubbishcommunity.ui.utils.showGallery


class MomentsListAdapter(
	onCellClick: (MomentContent) -> Unit,
	val onHeaderClick: (SimpleProfileResp) -> Unit,
	val onLikeClick: (MomentContent, Int) -> Unit,
	val onTransClick: (MomentContent) -> Unit
) : BaseRecyclerAdapter<MomentContent, CellMomentBinding>(
	R.layout.cell_moment,
	onCellClick,
	hasLoadMore = true
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
		
		
		val likeList = moment.likeList?.map { it.commentator } ?: emptyList()
		
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
						if (
							parent.getChildAdapterPosition(view) != (likeList.size - 1)
							&& parent.getChildAdapterPosition(view) != (0)
						)
							outRect.right = -6
					}
				})
			}
		}
		val likeSize = likeList.size
		binding.likeString = when {
			likeSize > 10 -> "等共${likeSize}人觉得很赞"
			likeSize in 1..10 -> "共${likeSize}人觉得很赞"
			else -> ""
		}
		
		binding.cellAuthorPortrait.setOnClickListener { onHeaderClick(moment.publisher) }
		binding.btnLike.setOnClickListener { onLikeClick(moment, position) }
		binding.btnTrans.setOnClickListener { onTransClick(moment) }
		
	}
	
	//点赞或取消点赞
	@RequiresApi(Build.VERSION_CODES.N)
	fun like(commentId: Long, momentPosition: Int) {
		val moment = baseList[momentPosition]
		val changedMoment =
			moment.copy(momentCommentList = moment.momentCommentList.toMutableList().apply {
				when {
					moment.isLikedByMe -> removeIf {
						it.commentator.openId == getLocalOpenId() && it.commentType == COMMENT_LIKE
					}
					else -> add(
						MomentComment(
							commentId,
							COMMENT_LIKE,
							commentator = getUserSimpleProfile(),
							commentDate = 0.toLong(),
							commentReplyList = emptyList()
						)
					)
				}
			})
		changeData(
			changedMoment,
			momentPosition
		)
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

