package com.app.task.manager.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.app.task.manager.models.Board
import com.app.task.manager.models.User
import com.app.task.manager.utils.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

class FireStoreHandler {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(userInfo: User, action: () -> Unit) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                action.invoke() //show toast to the user
            }
    }

    fun getBoardDetails(
        documentID: String,
        actionSuccess: (board: Board) -> Unit,
        actionFailure: () -> Unit
    ) {
        mFireStore.collection(Constants.BOARDS)
            .document(documentID)
            .get()
            .addOnSuccessListener { document ->
                Log.e("GetBoardList", document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentID = document.id
                actionSuccess.invoke(board)
            }.addOnFailureListener {
                actionFailure.invoke()
            }
    }

    fun createBoard(
        boardInfo: Board,
        actionSuccess: () -> Unit,
        actionFailure: () -> Unit
    ) {
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(boardInfo, SetOptions.merge())
            .addOnSuccessListener {
                actionSuccess.invoke()
            }.addOnFailureListener {
                actionFailure.invoke()
            }
    }

    fun addUpdateTaskList(
        activity: Activity,
        board: Board,
        actionSuccess: () -> Unit,
        actionFailure: () -> Unit
    ) {

        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentID)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "TaskList updated successfully.")
                actionSuccess.invoke()
            }
            .addOnFailureListener { e ->
                actionFailure.invoke()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    fun updateUserProfileData(
        activity: Activity,
        userHashMap: HashMap<String, Any>,
        actionSuccess: () -> Unit,
        actionFailure: () -> Unit
    ) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                actionSuccess.invoke()
            }.addOnFailureListener {
                actionFailure.invoke()
                Toast.makeText(activity, "Failed updating profile!", Toast.LENGTH_SHORT).show()
            }
    }

    fun getBoardsList(
        actionSuccess: (listData: ArrayList<Board>) -> Unit,
        actionFailure: () -> Unit
    ) {
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.e("GetBoardList", document.documents.toString())
                val boardsList: ArrayList<Board> = ArrayList()
                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.documentID = i.id
                    boardsList.add(board);
                }
                actionSuccess.invoke(boardsList)
            }.addOnFailureListener {
                actionFailure.invoke()
            }
    }

    fun loadUserData(
        actionSuccess: (document: DocumentSnapshot) -> Unit,
        actionFailure: () -> Unit
    ) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                actionSuccess.invoke(document)
            }.addOnFailureListener {
                actionFailure.invoke()
            }
    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getAssignedMembersListDetails(
        activity: Activity,
        assignedTo: ArrayList<String>,
        actionSuccess: (usersList: ArrayList<User>) -> Unit,
        actionFailure: () -> Unit
    ) {
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                val usersList: ArrayList<User> = ArrayList()

                for (i in document.documents) {
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }

                actionSuccess.invoke(usersList)
            }
            .addOnFailureListener { e ->
                actionFailure.invoke()
                Log.e(activity.javaClass.simpleName, "Error while creating a List of members", e)
            }
    }

    fun getMemberDetails(
        activity: Activity,
        email: String,
        actionSuccess: (doc: QuerySnapshot) -> Unit,
        actionFailure: () -> Unit
    ) {
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener { document ->
                actionSuccess.invoke(document)
            }
            .addOnFailureListener {
                actionFailure.invoke()
                Log.e(activity.javaClass.simpleName, "Error while getting user details")
            }
    }

    fun assignMemberToBoard(
        activity: Activity, board: Board, user: User,
        actionSuccess: (doc: User) -> Unit,
        actionFailure: () -> Unit
    ) {
        val assignedToHashMap = HashMap<String, Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentID)
            .update(assignedToHashMap)
            .addOnSuccessListener {
                actionSuccess.invoke(user)
            }
            .addOnFailureListener { e ->
                actionFailure.invoke()
                Log.e(activity.javaClass.simpleName, "Error while creating a board", e)
            }
    }
}