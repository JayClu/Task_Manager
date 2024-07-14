package com.app.task.manager.ui.sign

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivitySignInBinding
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.ui.main.MainActivity
import com.app.task.manager.utils.ex.openActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();
        initView()
        initData()
        initListener()

    }

    private fun initView() {

    }

    private fun initData() {

    }

    private fun initListener() {
        binding.signInPageBtn.setOnClickListener {
            signInUser()
        }
    }

    private fun signInUser() {
        val email = binding.signInEmailEt.text.toString().trim { it <= ' ' }
        val password = binding.signInPasswordEt.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            showProgressDialog()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        FireStoreHandler().loadUserData(
                            actionSuccess = {
                                signInSuccess()
                            }, actionFailure = {
                                hideProgressDialog()
                            })
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun signInSuccess() {
        hideProgressDialog()
        openActivity(MainActivity::class.java, true)
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email address")
                false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }

            else -> {
                true
            }
        }
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySignInBinding {
        return ActivitySignInBinding.inflate(layoutInflater)
    }
}