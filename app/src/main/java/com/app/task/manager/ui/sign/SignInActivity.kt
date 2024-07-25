package com.app.task.manager.ui.sign

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.task.manager.R
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.ui.main.MainActivity
import com.app.task.manager.ui.theme.Color1
import com.app.task.manager.ui.theme.Color2
import com.app.task.manager.ui.theme.TaskManagerAppTheme
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {

    private lateinit var authFirebase: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFirebase = FirebaseAuth.getInstance()
        setContent {
            TaskManagerAppTheme {
                MainScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreen() {

        var showDialog by remember { mutableStateOf(false) }

        var email by remember {
            mutableStateOf("")
        }

        var password by remember {
            mutableStateOf("")
        }

        val backgroundPainter = painterResource(id = R.drawable.ic_background)

        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = backgroundPainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Box(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = "SIGN IN",
                            style = TextStyle(
                                color = Color(0xFF0A80F5),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = "Enter your email and password to sign in.",
                        fontFamily = FontFamily(Font(R.font.poppins_regular_400, FontWeight.Bold, FontStyle.Normal)),
                        style = TextStyle(
                            color = Color(0xFF7A8089), // Replace with your color resource
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                    )


                    Card(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 25.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp),
                                value = email,
                                onValueChange = {
                                    email = it
                                },
                                textStyle = TextStyle(
                                    color = Color(0xFF000000), // Replace with your color resource
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular_400, FontWeight.Normal, FontStyle.Normal)),
                                    fontWeight = FontWeight.Normal
                                ),
                                label = { Text("Email") }
                            )

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp),
                                value = password,
                                onValueChange = {
                                    password = it
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                textStyle = TextStyle(
                                    color = Color(0xFF000000), // Replace with your color resource
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular_400, FontWeight.Normal, FontStyle.Normal)),
                                    fontWeight = FontWeight.Normal
                                ),
                                label = { Text("Password") }
                            )

                            Box(
                                modifier = with(Modifier) {
                                    padding(start = 20.dp, end = 20.dp, top = 15.dp)
                                    fillMaxWidth()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color1,
                                                    Color2
                                                )
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            if (TextUtils.isEmpty(email)) {
                                                makeText(
                                                    baseContext,
                                                    "Please enter an email address",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else if (TextUtils.isEmpty(password)) {
                                                makeText(
                                                    baseContext,
                                                    "Please enter a password",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                showDialog = true
                                                authFirebase
                                                    .signInWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(this@SignInActivity) { task ->
                                                        if (task.isSuccessful) {
                                                            FireStoreHandler().loadUserData(
                                                                actionSuccess = {
                                                                    showDialog = false
                                                                    val intent = Intent(
                                                                        this@SignInActivity,
                                                                        MainActivity::class.java
                                                                    ).apply {
                                                                        flags =
                                                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                    }
                                                                    startActivity(intent)
                                                                    finish()
                                                                }, actionFailure = {
                                                                    showDialog = false
                                                                })
                                                        } else {
                                                            makeText(
                                                                baseContext,
                                                                "Authentication failed.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                            }
                                        }
                                }
                            ) {
                                Text(
                                    text = "SIGN IN",
                                    fontFamily = FontFamily(Font(R.font.poppins_regular_400, FontWeight.Bold, FontStyle.Normal)),
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(top = 12.dp, bottom = 12.dp)
                                )
                            }
                        }
                    }
                }

                if (showDialog) {
                    LoadingDialog()
                }
            }
        }
    }
}