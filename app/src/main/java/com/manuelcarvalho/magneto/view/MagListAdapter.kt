package com.manuelcarvalho.magneto.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.manuelcarvalho.magneto.R
import kotlinx.android.synthetic.main.mag_item.view.*


class MagListAdapter(val magList: ArrayList<Double>) :
    RecyclerView.Adapter<MagListAdapter.MagViewHolder>() {

    fun updateMaglist(newGitJobsList: List<Double>) {
        magList.clear()
        magList.addAll(newGitJobsList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(
            R.layout.mag_item
            , parent, false
        )
        return MagViewHolder(view)
    }

    override fun getItemCount() = magList.size

    override fun onBindViewHolder(holder: MagViewHolder, position: Int) {
        holder.view.textView.text = magList[position].toString()



        holder.view.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment())
        }

    }

    class MagViewHolder(var view: View) : RecyclerView.ViewHolder(view)
}