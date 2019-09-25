package com.example.rubbishcommunity.ui.home.find.vote

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.VoteBinding
import com.example.rubbishcommunity.ui.home.MainActivity


class VoteFragment : BindingFragment<VoteBinding, VoteViewModel>(
    VoteViewModel::class.java, R.layout.frag_vote
) {
    override fun initBefore() {


    }

    @SuppressLint("CheckResult")
    override fun initWidget() {
        binding.vm = viewModel

        //viewModel.refreshing.observe { binding.refreshlayout.isRefreshing = it!! }

        viewModel.init()

        binding.recVote.run {
            layoutManager = LinearLayoutManager(context)
            adapter = VoteListAdapter(R.layout.cell_vote,viewModel.voteList.value)
        }


        binding.refreshLayout.setOnRefreshListener {
            when {
                !isNetworkAvailable() -> {
                    (activity as MainActivity).showNetErrorSnackBar()
                    viewModel.refreshing.postValue(false)
                }
                else -> {
                    viewModel.getVoteList()
                }
            }
        }


    viewModel.voteList.observe {
        binding.recVote.run {
            (adapter as VoteListAdapter).replaceData(it!!)
        }
    }

        viewModel.refreshing.observe { isRefreshing ->
            binding.refreshLayout.run {
                if (!isRefreshing!!) finishRefresh()
            }
        }


}

override fun initData() {

}


}