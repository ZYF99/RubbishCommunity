package com.example.rubbishcommunity.mainac.ui.find.dynamic

import android.widget.GridView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.model.Dynamic

class DynamicListAdapter(layoutRes: Int, data: MutableList<Dynamic>?) :
    BaseQuickAdapter<Dynamic, BaseViewHolder>(layoutRes, data) {

    override fun convert(helper: BaseViewHolder?, item: Dynamic?) {

        helper!!.setText(R.id.tv_dynamic_name, "${item!!.id} ")
        helper!!.setText(R.id.tv_dynamic_content, item.content)

        (helper.getView(R.id.grid_dynamicimg) as GridView).adapter =
            ImageGridAdapter(mContext, item.images)
    }
}