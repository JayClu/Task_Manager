package com.app.task.manager.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.task.manager.R
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivityMainBinding
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.models.Board
import com.app.task.manager.models.User
import com.app.task.manager.ui.adapter.BoardItemAdapter
import com.app.task.manager.ui.board.CreateBoardActivity
import com.app.task.manager.ui.profile.MyProfileActivity
import com.app.task.manager.ui.sign.LoginActivity
import com.app.task.manager.ui.task.TaskListActivity
import com.app.task.manager.utils.utils.Constants
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : BaseActivity<ActivityMainBinding>(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 21
    }

    private lateinit var mUserName: String
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initData()
        initListener()

    }

    private fun initView() {
        setActionBar()
        binding.navigatorView.setNavigationItemSelectedListener(this)
    }

    private fun initData() {
        mSharedPreferences =
            this.getSharedPreferences(Constants.POJECTMANAGER_PREFERENCES, MODE_PRIVATE)

        val tokenUpdated = mSharedPreferences.getBoolean(Constants.FCM_TOKEN_UPDATED, false)
        if (tokenUpdated) {
            showProgressDialog()
            FireStoreHandler().loadUserData( actionSuccess = {
                val loggedInUser = it.toObject(User::class.java)!!
                updateNavigationUserDetails(loggedInUser, true)
            }, actionFailure = {
                hideProgressDialog()
            })
        } else {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                updateFcmToken(token)
            }
        }

        FireStoreHandler().loadUserData( actionSuccess = {
            val loggedInUser = it.toObject(User::class.java)!!
            updateNavigationUserDetails(loggedInUser, true)
        }, actionFailure = {
            hideProgressDialog()
        })
    }

    private fun initListener() {
        binding.appBarOfMain.createBoardFab.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }
    }

    private fun populateBoardsToUI(boardsList: ArrayList<Board>) {
        hideProgressDialog()

        if (boardsList.size > 0) {
            binding.appBarOfMain.llMainContent.boardsListRv.visibility = View.VISIBLE
            binding.appBarOfMain.llMainContent.noBoardsTv.visibility = View.GONE

            binding.appBarOfMain.llMainContent.boardsListRv.layoutManager =
                LinearLayoutManager(this)
            binding.appBarOfMain.llMainContent.boardsListRv.setHasFixedSize(true)

            val adapter = BoardItemAdapter(this, boardsList)
            binding.appBarOfMain.llMainContent.boardsListRv.adapter = adapter

            adapter.setOnClickListener(object : BoardItemAdapter.OnClickListener {
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentID)
                    startActivity(intent)
                }

                override fun onDelete(position: Int, model: Board) {
                    showProgressDialog()
                    FireStoreHandler().deleteBoard(model, actionSuccess = {
                        getBoardsListHandler()
                    }, actionFailure = {
                        hideProgressDialog()
                    })
                }
            })
        } else {
            binding.appBarOfMain.llMainContent.boardsListRv.visibility = View.GONE
            binding.appBarOfMain.llMainContent.noBoardsTv.visibility = View.VISIBLE
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.appBarOfMain.toolbarMainActivity)
        binding.appBarOfMain.toolbarMainActivity.setNavigationIcon(R.drawable.ic_navigation_manu)
        binding.appBarOfMain.toolbarMainActivity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    override fun onBack() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    // set logged user image and name to the navigator
    private fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
        hideProgressDialog()
        mUserName = user.name

        // add image
        Glide.with(this)
            .load(user.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding.navigatorView.findViewById<ImageView>(R.id.nav_user_image))

        binding.navigatorView.findViewById<TextView>(R.id.user_name_tv).text = user.name

        if (readBoardsList) {
            showProgressDialog()
            getBoardsListHandler()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(this, MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE
                )
            }

            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                mSharedPreferences.edit().clear().apply()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE) {
            loadUserDataHandler()
        } else if (resultCode == RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE) {
            getBoardsListHandler()
        }
    }

    fun tokenUpdateSuccess() {
        hideProgressDialog()
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putBoolean(Constants.FCM_TOKEN_UPDATED, true)
        editor.apply()
        showProgressDialog()

        loadUserDataHandler()
    }


    private fun getBoardsListHandler() {
        FireStoreHandler().getBoardsList(actionSuccess = {
            populateBoardsToUI(it)
        }, actionFailure = {
            hideProgressDialog()
        })
    }


    private fun loadUserDataHandler() {
        FireStoreHandler().loadUserData( actionSuccess = {
            val loggedInUser = it.toObject(User::class.java)!!
            updateNavigationUserDetails(loggedInUser, true)
        }, actionFailure = {
            //hideProgressDialog()
        })
    }

    private fun updateFcmToken(token: String) {
        val userHashMap = HashMap<String, Any>()
        userHashMap[Constants.FCM_TOKEN] = token
        showProgressDialog()
        FireStoreHandler().updateUserProfileData(this, userHashMap,
            actionSuccess = {
                tokenUpdateSuccess()
            }, actionFailure = {
                hideProgressDialog()
            })
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

}