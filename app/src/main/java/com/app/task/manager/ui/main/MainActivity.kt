package com.app.task.manager.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

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

    }

    private fun initData() {

    }

    private fun initListener() {

    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

}