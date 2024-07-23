package com.app.task.manager.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.task.manager.R

class LabelColorListItemAdapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private val mSelectedColor: String,
) : RecyclerView.Adapter<LabelColorListItemAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_label_color, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.view_main.setBackgroundColor(Color.parseColor(item))
        if (item == mSelectedColor) {
            holder.selected_color_iv.visibility = View.VISIBLE
        } else {
            holder.selected_color_iv.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onClick(position, item)
            }
        }

    }


    interface OnItemClickListener {
        fun onClick(position: Int, color: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view_main: View = itemView.findViewById(R.id.view_main)
        val selected_color_iv: ImageView = itemView.findViewById(R.id.selected_color_iv)
    }
}