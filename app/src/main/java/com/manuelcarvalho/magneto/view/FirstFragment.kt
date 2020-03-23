package com.manuelcarvalho.magneto.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.manuelcarvalho.magneto.R
import com.manuelcarvalho.magneto.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val magListAdapter = MagListAdapter(arrayListOf())
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = magListAdapter
        }

        viewModel = activity?.run {
            ViewModelProviders.of(this)[ListViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.refresh()

        refreshLayout.setOnRefreshListener {
            loadingView.visibility = View.VISIBLE
            viewModel.refresh()
            refreshLayout.isRefreshing = false
        }
        observeViewModel()


    }

    fun observeViewModel() {

        viewModel.readings.observe(this, Observer { readings ->
            readings?.let {
                recyclerView.visibility = View.VISIBLE
                magListAdapter.updateMaglist(readings)
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE

            }
        })

    }
}
