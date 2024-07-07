package com.example.cmpt362project.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmpt362project.R
import com.example.cmpt362project.adapters.MemberItemAdapter
import com.example.cmpt362project.models.User

abstract class MembersListDialog(
    context: Context,
    private var list: ArrayList<User>,
    private val title: String = "",
) : Dialog(context) {

    private var adapter: MemberItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)
        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

    private fun setUpRecyclerView(view: View) {
        view.findViewById<TextView>(R.id.dialog_title_tv).text = title

        if (list.size > 0) {
            view.findViewById<RecyclerView>(R.id.dialog_list_rv).layoutManager =
                LinearLayoutManager(context)
            adapter = MemberItemAdapter(context, list)
            view.findViewById<RecyclerView>(R.id.dialog_list_rv).adapter = adapter
            adapter!!.setOnClickListener(object : MemberItemAdapter.OnClickListener {
                override fun onClick(position: Int, user: User, action: String) {
                    dismiss()
                    onItemSelected(user, action)
                }
            })
        }
    }

    protected abstract fun onItemSelected(user: User, color: String)
}