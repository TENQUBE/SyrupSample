package com.tenqube.sample.syrup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.tenqube.sample.R
import com.tenqube.sample.databinding.FragmentTestListBinding
import com.tenqube.sample.syrup.main.adapter.TestAdapter
import com.tenqube.sample.syrup.main.event.EventListener


class TestListFragment : Fragment(), EventListener {

    private val viewModel = TestListViewModel()

    private lateinit var viewDataBinding: FragmentTestListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentTestListBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewDataBinding.viewmodel = viewModel

        setTestListAdapter()
    }

    private fun setTestListAdapter(){
        viewDataBinding.testList.adapter = TestAdapter(this)
    }

    override fun onItemClicked(view: View, item: String) {
        when (item) {
            getString(R.string.parse) -> {
                view.findNavController().navigate(
                    R.id.action_parse,
                    Bundle().apply { putString(REQUEST_TITLE, item) })
            }

            getString(R.string.bulk_parse) -> {
                view.findNavController().navigate(
                    R.id.action_parseBulk,
                    Bundle().apply { putString(REQUEST_TITLE, item) })

            }

            getString(R.string.query_receipt) -> {
                view.findNavController().navigate(
                    R.id.action_queryReceipt,
                    Bundle().apply { putString(REQUEST_TITLE, item) })
            }

            getString(R.string.start_visual) -> {
                view.findNavController().navigate(
                    R.id.action_startVisual,
                    Bundle().apply { putString(REQUEST_TITLE, item) })

            }

            getString(R.string.start_rcs) -> {
                view.findNavController().navigate(
                        R.id.action_startRcs,
                        Bundle().apply { putString(REQUEST_TITLE, item) })

            }
        }
    }

    companion object {
        const val REQUEST_TITLE = "requestTitle"
        @JvmStatic
        fun newInstance() =
            TestListFragment().apply {

            }
    }
}