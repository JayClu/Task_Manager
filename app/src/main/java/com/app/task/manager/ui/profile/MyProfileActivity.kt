package com.app.task.manager.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.task.manager.R
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivityMyProfileBinding
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.models.User
import com.app.task.manager.utils.utils.Constants
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class MyProfileActivity : BaseActivity<ActivityMyProfileBinding>() {

    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL: String = ""
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        initListener()
    }

    private fun initView() {
        setActionBar()
    }

    private fun initData() {
        FireStoreHandler().loadUserData(actionSuccess = {
            val loggedInUser = it.toObject(User::class.java)!!
            setUserDataInUI(loggedInUser)
        }, actionFailure = {

        })
    }

    private fun checkPermissionAndPickImageReadExternal() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Constants.showImageChooser(this)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionAndPickImageMediaImage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Constants.showImageChooser(this)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Constants.showImageChooser(this)
            }
        }

    private fun initListener() {
        binding.myProfileUserImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 33) {
                checkPermissionAndPickImageMediaImage()
            } else {
                checkPermissionAndPickImageReadExternal()
            }
        }

        binding.myProfileUpdateBtn.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadUserImage()
            } else {
                showProgressDialog()
                updateUserProfileData()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            }
        } else {
            Toast.makeText(
                this, "You just denied permission for storage." +
                        "You can allow it from the settings.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //check if the user select a photo from gallery
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            mSelectedImageFileUri = data.data
            // set the new photo to profile image
            try {
                Glide.with(this)
                    .load(mSelectedImageFileUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding.myProfileUserImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setActionBar() {
        setSupportActionBar(binding.toolbarMyProfileActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white)
            actionBar.title = "My Profile"
        }
        binding.toolbarMyProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setUserDataInUI(user: User) {
        mUserDetails = user
        //set user image
        Glide.with(this)
            .load(user.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding.myProfileUserImage)
        //set user details
        binding.myProfileNameEt.setText(user.name)
        binding.myProfileEmailEt.setText(user.email)
        if (user.mobile != 0L) {
            binding.myProfileMobileEt.setText(user.mobile.toString())
        }
    }

    private fun uploadUserImage() {
        showProgressDialog()

        if (mSelectedImageFileUri != null) {
            val sRef: StorageReference = FirebaseStorage.getInstance()
                .reference
                .child(
                    "USER_IMAGE" + System.currentTimeMillis()
                            + "." + Constants.getFileExtension(this, mSelectedImageFileUri)
                )

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl!!.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()

                    updateUserProfileData()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }
        }
    }

    private fun updateUserProfileData() {
        val userHashMap = HashMap<String, Any>()
        var anyChangesMade: Boolean = false

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangesMade = true
        }

        if (binding.myProfileNameEt.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = binding.myProfileNameEt.text.toString()
            anyChangesMade = true
        }

        if (binding.myProfileMobileEt.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = binding.myProfileMobileEt.text.toString().toLong()
            anyChangesMade = true
        }

        if (anyChangesMade)
            FireStoreHandler().updateUserProfileData(
                this,
                userHashMap,
                actionSuccess = {
                    profileUpdateSuccess()
                },
                actionFailure = {
                    hideProgressDialog()
                }
            )
        else {
            Toast.makeText(this, "Nothing as been updated!", Toast.LENGTH_SHORT).show()
            hideProgressDialog()
        }
    }

    private fun profileUpdateSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityMyProfileBinding {
        return ActivityMyProfileBinding.inflate(layoutInflater)
    }
}