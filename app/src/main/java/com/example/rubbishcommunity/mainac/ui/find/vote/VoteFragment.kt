package com.example.rubbishcommunity.mainac.ui.find.vote

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.VoteBinding
import com.example.rubbishcommunity.mainac.ui.find.dynamic.FindViewModel


class VoteFragment : BindingFragment<VoteBinding, VoteViewModel>(
    VoteViewModel::class.java, R.layout.frag_vote
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
            adapter = VoteListAdapter(R.layout.cell_dynamic,viewModel.dynamicList.value)

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
            (adapter as VoteListAdapter).replaceData(it!!)
        }
    }


}

override fun initData() {

}


}