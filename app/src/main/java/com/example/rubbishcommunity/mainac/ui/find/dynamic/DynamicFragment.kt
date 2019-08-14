package com.example.rubbishcommunity.mainac.ui.find.dynamic

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicBinding


class DynamicFragment : BindingFragment<DynamicBinding, DynamicViewModel>(
    DynamicViewModel::class.java, R.layout.frag_dynamic
) {
    override fun initBefore() {


    }

    @SuppressLint("CheckResult")
    override fun initWidget(view: View) {
        binding.vm = viewModel

       // viewModel.refreshing.observe { binding.refreshlayout. = it!! }

        viewModel.init()



        binding.recFind.run {
            layoutManager = LinearLayoutManager(context)
            adapter = DynamicListAdapter(R.layout.cell_dynamic,viewModel.dynamicList.value)

        }



        binding.refreshlayout.setOnRefreshListener {
            when {
                !isNetworkAvailable() -> {
                    showNetErrorSnackBar()
                }
                else -> {
                    viewModel.getDynamicList()
                }
            }
        }




    viewModel.dynamicList.observe {
        binding.recFind.run {
            (adapter as DynamicListAdapter).replaceData(it!!)
        }
    }


}

override fun initData() {

}


}