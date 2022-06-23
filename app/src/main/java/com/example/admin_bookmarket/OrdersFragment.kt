package com.example.admin_bookmarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.ViewModel.OrderViewModel
import com.example.admin_bookmarket.data.adapter.OrderAdapter
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrdersFragment : Fragment(), RecyclerViewClickListener {
    private lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrderViewModel by activityViewModels()
    private lateinit var orderListAdapter: OrderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var showList: MutableList<Order> = ArrayList()
    private var listStatus: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        orderListAdapter =
            OrderAdapter(mutableListOf(), this.requireContext(), this, viewModel)
        getOrders(orderListAdapter)
        binding.ordersList.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        setHasOptionsMenu(true)
        binding.option.setOnClickListener {
            startActivity(Intent(binding.root.context, StatisticActivity::class.java))
        }

        setUpMessage()
        return binding.root
    }

    private fun setUpMessage() {
        if (showList.size == 0) {
            binding.noItemToShow.visibility = View.VISIBLE
        } else {
            binding.noItemToShow.visibility = View.GONE
        }
    }

    private fun getOrders(orderAdapter: OrderAdapter) {
        viewModel.getAllOrder().observe(this.viewLifecycleOwner) { changes ->
            showList = changes
            AppUtils.oderList = changes
            if (listStatus != "") {
                orderAdapter.addOrder(filterList(showList, listStatus))
            } else {
                orderAdapter.addOrder(showList)
                Log.d("hihi", showList.size.toString())
            }
            setUpMessage()

        }
    }

    private fun filterList(orderList: MutableList<Order>, condition: String): MutableList<Order> {
        val filterOrderList: MutableList<Order> = ArrayList()
        for (order in orderList) {
            if (order.status == condition) {
                filterOrderList.add(order)
            }
        }
        return filterOrderList
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        //super.onCreateOptionsMenu(menu, inflater)
    }

    override fun recyclerViewListClicked(v: View?, id: String) {

        startActivity(Intent(binding.root.context, OrderDetail::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.waiting -> listStatus = Constants.OrderStatus.WAITING.toString()
            R.id.confirmed -> listStatus = Constants.OrderStatus.CONFIRMED.toString()
            R.id.delivering -> listStatus = Constants.OrderStatus.PRINTED.toString()
            R.id.complete -> listStatus = Constants.OrderStatus.COMPLETE.toString()
            R.id.cancel -> listStatus = Constants.OrderStatus.CANCEL.toString()
        }
        if (listStatus != "") {
            val filterList = filterList(showList, listStatus)
            if (filterList.isEmpty()) {
                binding.noItemToShow.visibility = View.VISIBLE
            } else {
                binding.noItemToShow.visibility = View.GONE
            }
            orderListAdapter.addOrder(filterList)

        }
        return super.onOptionsItemSelected(item)
    }


}