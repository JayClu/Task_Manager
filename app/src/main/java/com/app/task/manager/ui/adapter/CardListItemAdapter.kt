package com.app.task.manager.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.task.manager.R
import com.app.task.manager.models.Card
import com.app.task.manager.models.SelectedMembers
import com.app.task.manager.ui.task.TaskListActivity

open class CardListItemAdapter(private val context: Context, private var list: ArrayList<Card>) :
    RecyclerView.Adapter<CardListItemAdapter.ViewHolder>() {

    private var onClickListener: CardListItemAdapter.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val model = list[position]


        val regularFont: Typeface = Typeface.createFromAsset(context.assets, "Raleway-Regular.ttf")
        holder.card_name_tv.typeface = regularFont
        holder.card_name_tv.text = model.name

        if (model.labelColor.isNotEmpty()) {
            holder.view_label_color.visibility = View.VISIBLE
            holder.view_label_color.setBackgroundColor(
                Color.parseColor(model.labelColor)
            )
        } else {
            holder.view_label_color.visibility = View.GONE
        }

        //set the assigned members under the card name as circle images
        if ((context as TaskListActivity).mAssignedMembersDetailList.size > 0) {

            val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()
            val assignedMembers = context.mAssignedMembersDetailList

            for (i in assignedMembers.indices) {
                for (j in model.assignedTo) {
                    if (assignedMembers[i].id == j) {
                        val selectedMember =
                            SelectedMembers(assignedMembers[i].id, assignedMembers[i].image)
                        selectedMembersList.add(selectedMember)
                    }
                }
            }

            if (selectedMembersList.size > 0) {
                //if the only member in the list is the creator - do not show the recycler view
                if (selectedMembersList.size == 1
                    && selectedMembersList[0].id == model.createdBy
                ) {
                    holder.card_selected_members_list_rv.visibility = View.GONE
                } else { //add all the assigned members to the recycler view under the card name create and set the adapter
                    holder.card_selected_members_list_rv.visibility = View.VISIBLE
                    holder.card_selected_members_list_rv.layoutManager =
                        GridLayoutManager(context, 4)
                    val adapter = CardMemberListItemAdapter(context, selectedMembersList, false)
                    holder.card_selected_members_list_rv.adapter = adapter
                    adapter.setOnClickListener(object : CardMemberListItemAdapter.OnClickListener {
                        override fun onClick() {
                            if (onClickListener != null) {
                                onClickListener!!.onClick(position)
                            }
                        }
                    })
                }
            } else {
                holder.card_selected_members_list_rv.visibility = View.GONE
            }
        }

        holder.itemAll.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position)
            }
        }

    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener

    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemAll: CardView = itemView.findViewById(R.id.itemAll)
        val card_name_tv: TextView = itemView.findViewById(R.id.card_name_tv)
        val view_label_color: View = itemView.findViewById(R.id.view_label_color)
        val card_selected_members_list_rv: RecyclerView =
            itemView.findViewById(R.id.card_selected_members_list_rv)
    }
}