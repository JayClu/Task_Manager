package com.app.task.manager.ui.sign

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
import androidx.compose.material3.CardColors
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
import com.app.task.manager.models.User
import com.app.task.manager.ui.theme.Color1
import com.app.task.manager.ui.theme.Color2
import com.app.task.manager.ui.theme.TaskManagerAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {

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

        var name by remember {
            mutableStateOf("")
        }

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
                        text = "Enter your name, email and password to register with us.",
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
                                value = name,
                                onValueChange = {
                                    name = it
                                },
                                textStyle = TextStyle(
                                    color = Color(0xFF000000), // Replace with your color resource
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular_400, FontWeight.Normal, FontStyle.Normal)),
                                    fontWeight = FontWeight.Normal
                                ),
                                label = { Text("Name") }
                            )

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
                                            if (TextUtils.isEmpty(name)) {
                                                makeText(
                                                    baseContext,
                                                    "Please enter a name",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else if (TextUtils.isEmpty(email)) {
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
                                            } else if (password.length < 6) {
                                                makeText(
                                                    baseContext,
                                                    "Password should be at least 6 characters",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                showDialog = true
                                                authFirebase
                                                    .createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            val firebaseUser: FirebaseUser =
                                                                task.result!!.user!!
                                                            val registeredEmail =
                                                                firebaseUser.email!!
                                                            val user = User(
                                                                firebaseUser.uid,
                                                                name,
                                                                registeredEmail
                                                            )
                                                            FireStoreHandler().registerUser(user) {
                                                                makeText(
                                                                    baseContext,
                                                                    "You have successfully registered",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                showDialog = false
                                                                authFirebase.signOut()
                                                                finish()
                                                            }
                                                        } else {
                                                            makeText(
                                                                baseContext,
                                                                "Registration Failed. Try Again",
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
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular_400, FontWeight.Bold, FontStyle.Normal)),
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