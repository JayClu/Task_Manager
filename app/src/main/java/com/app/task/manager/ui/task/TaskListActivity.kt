package com.app.task.manager.ui.task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.task.manager.R
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivityTaskListBinding
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.models.Board
import com.app.task.manager.models.Card
import com.app.task.manager.models.Task
import com.app.task.manager.models.User
import com.app.task.manager.ui.adapter.TaskItemAdapter
import com.app.task.manager.ui.card.CardDetailsActivity
import com.app.task.manager.ui.member.MembersActivity
import com.app.task.manager.utils.utils.Constants

class TaskListActivity : BaseActivity<ActivityTaskListBinding>() {

    private lateinit var mBoardDetails: Board
    private lateinit var mBoardDocumentID: String
    lateinit var mAssignedMembersDetailList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            mBoardDocumentID = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
        }

        showProgressDialog()
        FireStoreHandler().getBoardDetails(mBoardDocumentID, actionSuccess = {
            boardDetails(it)
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    private fun boardDetails(board : Board){
        mBoardDetails = board
        hideProgressDialog()
        setActionBar()

        showProgressDialog()
        FireStoreHandler().getAssignedMembersListDetails(
            this,
            mBoardDetails.assignedTo,
            actionSuccess = {
                boardMembersDetailList(it)
            }, actionFailure = {
                hideProgressDialog()
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_members -> {
                val intent = Intent(this, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == MEMBERS_REQUEST_CODE
            || requestCode == CARD_DETAILS_REQUEST_CODE
        ) {
            showProgressDialog()
            FireStoreHandler().getBoardDetails(mBoardDocumentID, actionSuccess = {
                boardDetails(it)
            }, actionFailure = {
                hideProgressDialog()
            })
        } else {
            Log.e("Canceled", " Cancelled")
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.taskListActivityToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white)
            actionBar.title = mBoardDetails.name
        }
        binding.taskListActivityToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()
        showProgressDialog()
        FireStoreHandler().getBoardDetails(mBoardDetails.documentID, actionSuccess = {
            boardDetails(it)
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    fun createTaskList(taskName: String) {
        val task = Task(taskName, FireStoreHandler().getCurrentUserId())
        mBoardDetails.taskList.add(0, task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)// Remove the last position as we have added the item manually for adding the TaskList.

        showProgressDialog()
        FireStoreHandler().addUpdateTaskList(this, mBoardDetails, actionSuccess = {
            addUpdateTaskListSuccess()
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    fun updateTaskList(position: Int, listName: String, model: Task) {
        val task = Task(listName, model.createdBy)
        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog()
        FireStoreHandler().addUpdateTaskList(this, mBoardDetails, actionSuccess = {
            addUpdateTaskListSuccess()
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    fun deleteTaskList(position: Int) {
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog()
        FireStoreHandler().addUpdateTaskList(this, mBoardDetails, actionSuccess = {
            addUpdateTaskListSuccess()
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    fun addCardToTask(position: Int, cardName: String) {
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        val currentUserID = FireStoreHandler().getCurrentUserId()
        cardAssignedUsersList.add(currentUserID)
        val card = Card(cardName, currentUserID, cardAssignedUsersList)

        val cardList = mBoardDetails.taskList[position].cards
        cardList.add(card) //add the card to cads list in the task list

        val task = Task(
            mBoardDetails.taskList[position].title,
            mBoardDetails.taskList[position].createdBy,
            cardList
        )

        mBoardDetails.taskList[position] = task

        showProgressDialog()
        FireStoreHandler().addUpdateTaskList(this, mBoardDetails, actionSuccess = {
            addUpdateTaskListSuccess()
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    fun cardDetails(taskListPosition: Int, cardPosition: Int) {
        val intent = Intent(this@TaskListActivity, CardDetailsActivity::class.java)
        intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
        intent.putExtra(Constants.TASK_LIST_ITEM_POSITION, taskListPosition)
        intent.putExtra(Constants.CARD_LIST_ITEM_POSITION, cardPosition)
        intent.putExtra(Constants.BOARD_MEMBERS_LIST, mAssignedMembersDetailList)
        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE)
    }

    private fun boardMembersDetailList(list: ArrayList<User>) {
        mAssignedMembersDetailList = list
        hideProgressDialog()

        val addTaskList = Task("Add List")
        mBoardDetails.taskList.add(addTaskList)

        binding.taskListRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.taskListRv.setHasFixedSize(true)

        val adapter = TaskItemAdapter(this, mBoardDetails.taskList)
        binding.taskListRv.adapter = adapter

    }

    fun updateCardsInTaskList(taskListPosition: Int, cards: ArrayList<Card>) {
        //remove the 'add card' card
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        mBoardDetails.taskList[taskListPosition].cards = cards
        //show progress dialog and update the database with the new order of list
        showProgressDialog()
        FireStoreHandler().addUpdateTaskList(this, mBoardDetails, actionSuccess = {
            addUpdateTaskListSuccess()
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    companion object {
        const val MEMBERS_REQUEST_CODE: Int = 13
        const val CARD_DETAILS_REQUEST_CODE: Int = 14
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityTaskListBinding {
        return ActivityTaskListBinding.inflate(layoutInflater)
    }
}