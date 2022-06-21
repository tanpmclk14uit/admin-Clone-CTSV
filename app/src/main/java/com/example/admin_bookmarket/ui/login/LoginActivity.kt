package com.example.admin_bookmarket.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_bookmarket.EditProfileActivity
import com.example.admin_bookmarket.MainActivity
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.data.FullBookList
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.model.Information
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //init view
    private lateinit var loginEmailLayout: TextInputLayout
    private lateinit var loginPasswordLayout: TextInputLayout
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginSignUp: TextView
    private lateinit var loginButton: Button
    private val TAG = "LOGIN"


    //init database reference
    private val db = Firebase.firestore
    private val dbSalerAccountsReference = db.collection("salerAccount")
    private val dbAccountsReference = db.collection("accounts")
    private lateinit var auth: FirebaseAuth
    private lateinit var loadDialog: LoadDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        loginEmailLayout = findViewById(R.id.LoginEmailLayout)
        loginEmail = findViewById(R.id.LoginEmail)
        loginPasswordLayout = findViewById(R.id.LoginPasswordLayout)
        loginPassword = findViewById(R.id.LoginPassword)
        loginButton = findViewById(R.id.loginButton)
        loginPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                loginPasswordLayout.error = null
            }
        }
        loginButton.setOnClickListener {
            onButtonLoginClick()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            loginEmail.setText(Firebase.auth.currentUser!!.email.toString())
        }

    }

    private fun onButtonLoginClick() {
        if (isValidEmail() && isValidPassword()) {
            loadDialog = LoadDialog(this)
            loadDialog.startLoading()
            dbSalerAccountsReference.get().addOnSuccessListener { result ->
                var emailExist: Boolean = false
                for (document in result) {
                    if (document.id == loginEmail.text.toString()) {
                        emailExist = true
                    }
                }
                if (emailExist) {
                    val email = loginEmail.text.toString()
                    val password = loginPassword.text.toString()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                FullBookList.getInstance()
                                AppUtil.currentAccount.email = email
                                FirebaseFirestore.getInstance().collection("salerAccount")
                                    .document(email).get().addOnSuccessListener {
                                        val mapToInformation = it.data!!["information"] as Map<*,*>
                                        loadDialog.dismissDialog()
                                        val recentInformation: Information = Information(
                                            fullName = mapToInformation["fullName"].toString(),
                                            introduction = mapToInformation["introduction"].toString(),
                                            webSite = mapToInformation["webSite"].toString(),
                                            phoneNumber = mapToInformation["phoneNumber"].toString(),
                                            address = mapToInformation["address"].toString(),
                                        )
                                        startActivity(Intent(baseContext, MainActivity::class.java))
                                        AppUtil.currentInformation = recentInformation
                                        finish()
                                }
                            } else {
                                loginPasswordLayout.error = task.exception!!.message.toString()
                                loginPassword.clearFocus()
                                loadDialog.dismissDialog()
                            }
                        }
                } else {
                    Toast.makeText(this, "Sai tài khoản đăng nhập!", Toast.LENGTH_LONG).show()
                    loginPassword.clearFocus()
                    loadDialog.dismissDialog()
                }
            }
        }
    }


    private fun isEmailRightFormat(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    private fun isValidPassword(): Boolean {

        return if (loginPassword.text.isEmpty()) {
            loginPasswordLayout.error = "Password can not empty"
            loginPassword.clearFocus()
            false
        } else {
            if (loginPassword.text.count() < 8) {
                loginPasswordLayout.error = "Password must have more than 8 character"
                loginPassword.clearFocus()
                false
            } else {
                loginPasswordLayout.error = null
                true
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return if (loginEmail.text.isEmpty()) {
            loginEmailLayout.error = "Email can not empty"
            false
        } else {
            if (isEmailRightFormat(loginEmail.text.toString().trim())) {
                loginEmailLayout.error = null
                true
            } else {
                loginEmailLayout.error = "Invalid email"
                false
            }
        }
    }
}