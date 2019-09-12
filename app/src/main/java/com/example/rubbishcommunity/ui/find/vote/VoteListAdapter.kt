package com.example.rubbishcommunity.ui.find.vote


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.model.Vote

class VoteListAdapter(layoutRes:Int, data: MutableList<Vote>?) : BaseQuickAdapter<Vote, BaseViewHolder>(layoutRes,data) {

    override fun convert(helper: BaseViewHolder?, item: Vote?) {
        helper?.setText(R.id.tv_vote_content, item!!.content)
    }


}