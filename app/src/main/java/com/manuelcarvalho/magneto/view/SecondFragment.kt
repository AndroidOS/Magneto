package com.manuelcarvalho.magneto.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.manuelcarvalho.magneto.R
import com.manuelcarvalho.magneto.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_second.*


private const val TAG = "SecondFragment"
class SecondFragment : Fragment() {

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this)[ListViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        observeViewModel()

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )

        // set manual X bounds
//        graph.viewport.isYAxisBoundsManual = true
//        graph.viewport.setMinY(-150.0)
//        graph.viewport.setMaxY(150.0)
//
//        graph.viewport.isXAxisBoundsManual = true
//        graph.viewport.setMinX(4.0)
//        graph.viewport.setMaxX(150.0)

        // enable scaling and scrolling
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
        graph.addSeries(series)
    }

    private fun observeViewModel() {

        viewModel.readings.observe(this, Observer { readings ->
            readings?.let {

                generateData(it)
                Toast.makeText(activity, "ViewModel canged", Toast.LENGTH_SHORT).show()


            }
        })
    }

    private fun generateData(readings: List<Double>) {
        var readingsArray = Array<DataPoint>(readings.size) { DataPoint(0.0, 1.0) }
        var index = 0
        for (r in readings) {
            Log.d(TAG, " ${r}")
            readingsArray[index] = DataPoint(index.toDouble(), r - 20564)
            index++
        }
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(readingsArray)
        graph.addSeries(series)

    }
}
