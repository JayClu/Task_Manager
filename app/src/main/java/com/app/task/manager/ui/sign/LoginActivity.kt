package com.app.task.manager.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivityLoginBinding
import com.app.task.manager.utils.ex.openActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

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
        binding.signUpBtn.setOnClickListener {
            openActivity(SignUpActivity::class.java)
        }

        binding.signInBtn.setOnClickListener {
            openActivity(SignInActivity::class.java)
        }
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

}