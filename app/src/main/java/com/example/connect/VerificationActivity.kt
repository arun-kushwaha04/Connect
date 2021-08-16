package com.example.connect

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.connect.databinding.ActivityVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val phone = intent.getStringExtra("phone")!!
        binding.phone.text = phone

        sendOtp(phone)


    }

    private fun sendOtp(phone : String){

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                binding.progressActivity.visibility = View.GONE
                binding.otp.visibility = View.VISIBLE
                binding.verifyOtp.visibility = View.VISIBLE
                binding.resendOtp.visibility=View.GONE
                binding.otpEntered.setText(credential.smsCode);
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(ContentValues.TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@VerificationActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
                binding.verifyOtp.setOnClickListener(){
                    val userEnteredVerificationId = binding.otpEntered.text.toString()
                    val credential = PhoneAuthProvider.getCredential(verificationId, userEnteredVerificationId)
                    signInWithPhoneAuthCredential(credential)
                }
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String){
                binding.progressActivity.visibility = View.GONE
                binding.otp.visibility = View.GONE
                binding.verifyOtp.visibility = View.GONE
                binding.resendOtp.visibility=View.VISIBLE
                binding.resendOtp.setOnClickListener(){
                    val options = PhoneAuthOptions.newBuilder(Firebase.auth)
                        .setPhoneNumber(phone)
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this@VerificationActivity)
                        .setCallbacks(callbacks)
                        .setForceResendingToken(resendToken)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
            }
        }
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phone)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        binding.progressActivity.visibility = View.GONE
        binding.verifyOtp.visibility = View.GONE
        binding.verifyingOtpEntered.visibility=View.VISIBLE
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }else {
                    Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        binding.verifyOtp.visibility = View.VISIBLE
                        binding.verifyingOtpEntered.visibility=View.GONE
                    }
                    // Update UI
                }
            }
    }



}