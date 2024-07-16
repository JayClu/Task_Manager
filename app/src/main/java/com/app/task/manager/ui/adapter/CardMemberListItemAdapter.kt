package com.app.task.manager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.task.manager.R
import com.app.task.manager.models.SelectedMembers
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

open class CardMemberListItemAdapter(
    private val context: Context,
    private var list: ArrayList<SelectedMembers>,
    private val assignedMembers: Boolean,
) : RecyclerView.Adapter<CardMemberListItemAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.item_card_selected_member,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]


        if (position == list.size - 1 && assignedMembers) {
            holder.add_member_iv.visibility = View.VISIBLE
            holder.selected_member_image_iv.visibility = View.GONE
        } else {
            holder.add_member_iv.visibility = View.GONE
            holder.selected_member_image_iv.visibility = View.VISIBLE

            //set users image
            Glide
                .with(context)
                .load(model.image)
                .fitCenter()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.selected_member_image_iv)
        }

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick()
            }
        }

    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val add_member_iv: CircleImageView = itemView.findViewById(R.id.add_member_iv)
        val selected_member_image_iv: CircleImageView =
            itemView.findViewById(R.id.selected_member_image_iv)
    }
}