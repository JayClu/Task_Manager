package com.example.cmpt362project.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board

open class BoardItemAdapter (private val context:Context, private var list : ArrayList<Board>)
    :RecyclerView.Adapter<BoardItemAdapter.ViewHolder>(){

    private var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_board, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

            val regularFont: Typeface = Typeface.createFromAsset(context.assets, "Raleway-Regular.ttf")
            val boldFont: Typeface = Typeface.createFromAsset(context.assets, "Raleway-Bold.ttf")

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.item_board_iv)
            //set fonts
            holder.item_board_name_tv.typeface = boldFont
            holder.item_board_created_by_tv.typeface = regularFont
            //set text
            holder.item_board_name_tv.text = model.name
            holder.item_board_created_by_tv.text = "Created by: " + model.createdBy

            holder.itemAll.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }
    }

    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener

    }

    interface OnClickListener{
        fun onClick(position : Int, model : Board)
    }

    class ViewHolder(view : View): RecyclerView.ViewHolder(view){
        val itemAll: LinearLayout = itemView.findViewById(R.id.itemAll)
        val item_board_iv: ImageView = itemView.findViewById(R.id.item_board_iv)
        val item_board_name_tv: TextView = itemView.findViewById(R.id.item_board_name_tv)
        val item_board_created_by_tv: TextView = itemView.findViewById(R.id.item_board_created_by_tv)
    }
}