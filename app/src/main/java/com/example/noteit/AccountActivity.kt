package com.example.noteit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.Toast



class AccountActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordText: EditText
    private lateinit var createAccountButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loginButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        emailEditText = findViewById(R.id.email_edit)
        passwordEditText = findViewById(R.id.password_edit)
        confirmPasswordText = findViewById(R.id.confirm_password_edit)
        createAccountButton = findViewById(R.id.button)
        progressBar = findViewById(R.id.progress_bar)
        loginButton = findViewById(R.id.login_button)

        createAccountButton.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            // Create a new user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        Log.d("MainActivity", "createUserWithEmail:success")

                        // Send verification email
                        sendVerificationEmail()

                        Toast.makeText(this, "Registration successful. Verification email sent.", Toast.LENGTH_SHORT).show()
                    } else {
                        // If registration fails, display a message to the user.
                        Log.w("MainActivity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Registration failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MainActivity", "Email verification sent.")
                    finish()
                } else {
                    Log.w("MainActivity", "Failed to send email verification.", task.exception)
                }
            }
    }
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

}