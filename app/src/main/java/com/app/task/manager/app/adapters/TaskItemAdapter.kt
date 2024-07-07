package com.example.cmpt362project.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.activities.TaskListActivity
import com.example.cmpt362project.models.Task
import java.util.*
import kotlin.collections.ArrayList

open class TaskItemAdapter(private val context: Context, private var list : ArrayList<Task>)
    : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(){

    private var mPositionDraggedFrom = -1
    private var mPositionDraggedTo = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        val layoutParams = LinearLayout.LayoutParams((parent.width * 0.7).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins((15.toDP()).toPX(), 0, (40.toDP()).toPX(), 0)
        view.layoutParams = layoutParams
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val model = list[position]

        val regularFont: Typeface = Typeface.createFromAsset(context.assets, "Raleway-Regular.ttf")
        val boldFont: Typeface = Typeface.createFromAsset(context.assets, "Raleway-Bold.ttf")

        holder.add_task_list_tv.typeface = boldFont
        holder.task_list_name_et.typeface = regularFont
        holder.task_list_title_tv.typeface = regularFont
        holder.edit_task_list_name_et.typeface = regularFont
        holder.card_name_et.typeface = regularFont
        holder.add_card_tv.typeface = boldFont

        if(position == list.size-1){
            holder.add_task_list_tv.visibility = View.VISIBLE
            holder.task_item_ll.visibility = View.GONE
        }else{
            holder.add_task_list_tv.visibility = View.GONE
            holder.task_item_ll.visibility = View.VISIBLE
        }

        holder.task_list_title_tv.text = model.title
        holder.add_task_list_tv.setOnClickListener {
            holder.add_task_list_tv.visibility = View.GONE
            holder.add_task_list_name_cv.visibility = View.VISIBLE
        }

        holder.close_list_name_ib.setOnClickListener {
            holder.add_task_list_tv.visibility = View.VISIBLE
            holder.add_task_list_name_cv.visibility = View.GONE
        }

        holder.done_list_name_ib.setOnClickListener {
            val listName = holder.task_list_name_et.text.toString()

            if(listName.isNotEmpty()){
                if(context is TaskListActivity){
                    context.createTaskList(listName)
                }
            }else{
                Toast.makeText(context,"Please enter a list name", Toast.LENGTH_SHORT).show()
            }
        }

        holder.edit_list_name_ib.setOnClickListener {
            holder.edit_task_list_name_et.setText(model.title)
            holder.title_view_ll.visibility = View.GONE
            holder.edit_task_list_name_cv.visibility = View.VISIBLE
        }

        holder.close_editable_view_ib.setOnClickListener {
            holder.title_view_ll.visibility = View.VISIBLE
            holder.edit_task_list_name_cv.visibility = View.GONE
        }

        holder.done_edit_list_name_ib.setOnClickListener {
            val listName = holder.edit_task_list_name_et.text.toString()
            if(listName.isNotEmpty()){
                if(context is TaskListActivity){
                    context.updateTaskList(position,listName, model)
                }
            }else{
                Toast.makeText(context,"Please enter a list name", Toast.LENGTH_SHORT).show()
            }
        }

        holder.delete_list_ib.setOnClickListener {
            alertDialogForDeleteList(position, model.title)
        }

        holder.add_card_tv.setOnClickListener {
            holder.add_card_tv.visibility = View.GONE
            holder.add_card_cv.visibility = View.VISIBLE
        }

        holder.close_card_name_ib.setOnClickListener {
            holder.add_card_tv.visibility = View.VISIBLE
            holder.add_card_cv.visibility = View.GONE
        }

        holder.done_card_name_ib.setOnClickListener {
            val cardName = holder.card_name_et.text.toString()
            if(cardName.isNotEmpty()){
                if(context is TaskListActivity){
                   context.addCardToTask(position,cardName)
                }
            }else{
                Toast.makeText(context,"Please enter a card name", Toast.LENGTH_SHORT).show()
            }
        }

        holder.card_list_rv.layoutManager = LinearLayoutManager(context)
        holder.card_list_rv.setHasFixedSize(true)

        val adapter = CardListItemAdapter(context, model.cards)
        holder.card_list_rv.adapter = adapter

        adapter.setOnClickListener(object :
            CardListItemAdapter.OnClickListener {
            override fun onClick(cardPosition: Int) {

                if (context is TaskListActivity) {
                    context.cardDetails(position, cardPosition)
                }
            }
        })

        val dividerItemDecoration = DividerItemDecoration(context,
        DividerItemDecoration.VERTICAL)

        holder.card_list_rv.addItemDecoration(dividerItemDecoration)

        val helper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0){

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val draggedPosition = viewHolder.adapterPosition
                    val targetPosition = target.adapterPosition

                    if(mPositionDraggedFrom == -1){
                        mPositionDraggedFrom = draggedPosition
                    }

                    mPositionDraggedTo = targetPosition
                    //using swap function to store the dragging cards changes in the list of cards in database
                    Collections.swap(list[position].cards, draggedPosition, targetPosition)
                   //notify the adapter about the change
                    adapter.notifyItemMoved(draggedPosition,targetPosition)
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }

                //this function runs when the drag and drop done
                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    //if any change was done update the cards list task
                    if(mPositionDraggedFrom != -1 && mPositionDraggedTo != -1 && mPositionDraggedFrom != mPositionDraggedTo){
                        (context as TaskListActivity).updateCardsInTaskList(position,list[position].cards)
                    }
                    // reset values
                    mPositionDraggedFrom = -1
                    mPositionDraggedTo = -1
                }
            }
        )

        helper.attachToRecyclerView(holder.card_list_rv)
    }

    private fun alertDialogForDeleteList(position : Int, title : String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert!")
        builder.setMessage("Are you sure you want to delete $title?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") {
            dialog, which ->
            dialog.dismiss()

            if(context is TaskListActivity){
                context.deleteTaskList(position)
            }
        }

        builder.setNegativeButton("No") {
                dialog, which ->
                dialog.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun Int.toDP(): Int = (this/Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPX(): Int = (this*Resources.getSystem().displayMetrics.density).toInt()

    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
        val add_task_list_tv: TextView = itemView.findViewById(R.id.add_task_list_tv)
        val task_list_title_tv: TextView = itemView.findViewById(R.id.task_list_title_tv)
        val add_card_tv: TextView = itemView.findViewById(R.id.add_card_tv)

        val task_list_name_et: EditText = itemView.findViewById(R.id.task_list_name_et)
        val edit_task_list_name_et: EditText = itemView.findViewById(R.id.edit_task_list_name_et)
        val card_name_et: EditText = itemView.findViewById(R.id.card_name_et)

        val task_item_ll: LinearLayout = itemView.findViewById(R.id.task_item_ll)
        val title_view_ll: LinearLayout = itemView.findViewById(R.id.title_view_ll)

        val add_task_list_name_cv: CardView = itemView.findViewById(R.id.add_task_list_name_cv)
        val edit_task_list_name_cv: CardView = itemView.findViewById(R.id.edit_task_list_name_cv)
        val add_card_cv: CardView = itemView.findViewById(R.id.add_card_cv)

        val edit_list_name_ib: ImageButton = itemView.findViewById(R.id.edit_list_name_ib)
        val done_list_name_ib: ImageButton = itemView.findViewById(R.id.done_list_name_ib)
        val delete_list_ib: ImageButton = itemView.findViewById(R.id.delete_list_ib)
        val close_editable_view_ib: ImageButton = itemView.findViewById(R.id.close_editable_view_ib)
        val close_list_name_ib: ImageButton = itemView.findViewById(R.id.close_list_name_ib)
        val done_edit_list_name_ib: ImageButton = itemView.findViewById(R.id.done_edit_list_name_ib)
        val close_card_name_ib: ImageButton = itemView.findViewById(R.id.close_card_name_ib)
        val done_card_name_ib: ImageButton = itemView.findViewById(R.id.done_card_name_ib)

        val card_list_rv: RecyclerView = itemView.findViewById(R.id.card_list_rv)

    }

}