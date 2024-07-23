package com.app.task.manager.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.task.manager.R
import com.app.task.manager.models.User
import com.app.task.manager.utils.utils.Constants
import com.bumptech.glide.Glide

open class MemberItemAdapter(private val context: Context, private var list: ArrayList<User>) :
    RecyclerView.Adapter<MemberItemAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_member, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        if (holder is ViewHolder) {
            val regularFont: Typeface =
                Typeface.createFromAsset(holder.itemView.getContext().assets, "Raleway-Regular.ttf")
            val boldFont: Typeface =
                Typeface.createFromAsset(holder.itemView.getContext().assets, "Raleway-Bold.ttf")
            holder.member_name_tv.typeface = boldFont
            holder.member_email_tv.typeface = regularFont
            holder.member_name_tv.text = model.name
            holder.member_email_tv.text = model.email
            Glide
                .with(context)
                .load(model.image)
                .fitCenter()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.member_image_iv)

            if (model.selected) {
                holder.selected_member_iv.visibility = View.VISIBLE
            } else {
                holder.selected_member_iv.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    if (model.selected) {
                        onClickListener!!.onClick(position, model, Constants.UN_SELECT)
                    } else {
                        onClickListener!!.onClick(position, model, Constants.SELECT)
                    }
                }
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selected_member_iv: ImageView = itemView.findViewById(R.id.selected_member_iv)
        val member_image_iv: ImageView = itemView.findViewById(R.id.selected_member_iv)
        val member_name_tv: TextView = itemView.findViewById(R.id.member_name_tv)
        val member_email_tv: TextView = itemView.findViewById(R.id.member_email_tv)
    }
}