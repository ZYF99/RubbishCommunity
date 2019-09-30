package com.example.rubbishcommunity.ui.home.message

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.model.Message

class MessageListAdapter(
    layoutResId: Int, @Nullable data: MutableList<Message>?,
    internal var listener: Listener
) : SwipeAdapterInterface, BaseQuickAdapter<Message, BaseViewHolder>(layoutResId, data){
    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }
    
    
    private lateinit var mItemManger :SwipeItemRecyclerMangerImpl
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mItemManger = SwipeItemRecyclerMangerImpl(this)
        return super.onCreateViewHolder(parent, viewType)

    }
    
    override fun convert(helper: BaseViewHolder, item: Message) {

        Glide.with(mContext).load(item.iconUrl).into(helper.getView(R.id.cell_msg_icon) as ImageView)

        helper.setText(R.id.cell_person_name, item.title)
        helper.setText(R.id.cell_person_btn, item.msg)
        helper.setText(R.id.cell_msg_time, item.time)

        //单项点击事件
        setOnItemClickListener { adapter, view, position ->
                mItemManger.closeAllItems()
                listener.onCellClick()
            
        }
    }

    interface Listener {
        fun onCellClick()
        fun onCellLongClick()
    }
}
