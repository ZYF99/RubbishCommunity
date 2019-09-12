package com.example.rubbishcommunity.ui.message

import android.widget.ImageView
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.model.Message

class MessageListAdapter(
    layoutResId: Int, @Nullable data: MutableList<Message>?,
    listener: MessageListAdapter.Listener
) : BaseQuickAdapter<Message, BaseViewHolder>(layoutResId, data) {

    internal var listener: Listener

    init {
        this.listener = listener
    }

    override fun convert(helper: BaseViewHolder, item: Message) {

        Glide.with(mContext).load(item.iconUrl).into(helper.getView(R.id.cell_msg_icon) as ImageView)

        helper.setText(R.id.cell_person_name, item.title)
        helper.setText(R.id.cell_person_btn, item.msg)
        helper.setText(R.id.cell_msg_time, item.time)

        //单项点击事件
        setOnItemClickListener { adapter, view, position -> listener.onCellClick() }
    }

    interface Listener {
        fun onCellClick()
        fun onCellLongClick()
    }
}
