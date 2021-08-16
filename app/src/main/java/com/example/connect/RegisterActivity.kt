package com.example.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.connect.databinding.ActivityRegisterBinding
import com.example.connect.databinding.ActivityVerificationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.TextViewToLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java);
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        binding.circleViewProfilePhotoRegister.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, 100);
        }
        
        
        binding.register.setOnClickListener(){
            registerUsersToFirebase();
        }
    }

    private fun registerUsersToFirebase() {
        val name = binding.editTextNameRegister.toString()
        val email = binding.editTextEmailRegister.toString()
        val password = binding.editTextPasswordText.toString()
        val confirmPassword = binding.editTextConfirmPasswordRegister.toString()
        val phone = binding.editTextNumberRegister.toString()

        MaterialAlertDialogBuilder(this)
            .setTitle("Phone Verification")
            .setMessage("An OTP will be send on given number")
            .setNegativeButton("Cancel") { dialog, _ ->
                Toast.makeText(this, "Operation Cancelled", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setPositiveButton("OK") { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
        
        //error checking for different fields
        if(name.isEmpty()){
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
        }
        if(phone.isEmpty() || phone.length!=10){
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
        }
//        if(password.isEmpty()){
//            Toast.makeText(this,"Enter a Password",Toast.LENGTH_SHORT).show()
//        }
//        if(password != confirmPassword){
//            Toast.makeText(this, "Password and Confirm Password Should Match", Toast.LENGTH_SHORT).show()
//        }


    }
}