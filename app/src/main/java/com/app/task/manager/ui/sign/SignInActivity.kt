package com.app.task.manager.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity<ActivitySignInBinding>() {

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

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySignInBinding {
        return ActivitySignInBinding.inflate(layoutInflater)
    }
}