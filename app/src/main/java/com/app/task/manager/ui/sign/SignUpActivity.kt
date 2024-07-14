package com.app.task.manager.ui.sign

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivitySignUpBinding
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

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
        binding.signUpPageBtn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = binding.nameEt.text.toString().trim { it <= ' ' } //remove spaces
        val email = binding.emailEt.text.toString().trim { it <= ' ' }
        val password = binding.passwordEt.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            showProgressDialog()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        FireStoreHandler().registerUser(user) {
                            userRegisteredSuccess()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Registration Failed. Try Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun userRegisteredSuccess() {
        Toast.makeText(
            this,
            "You have successfully registered",
            Toast.LENGTH_SHORT
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }

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

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }
}