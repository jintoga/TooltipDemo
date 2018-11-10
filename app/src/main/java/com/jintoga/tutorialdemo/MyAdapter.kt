package com.jintoga.tutorialdemo

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_data.view.*

class MyAdapter
    : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private val data = ArrayList<String>()

    init {
        for (i in 0..45) {
            data.add("item $i")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_data))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        fun bind(txt: String) = with(itemView) {
            textView1.text = txt
            textView2.text = txt
            textView3.text = txt
            textView4.text = txt
            textView5.text = txt
        }
    }
}