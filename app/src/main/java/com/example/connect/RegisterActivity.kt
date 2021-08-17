package com.example.connect

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.connect.databinding.ActivityRegisterBinding
import com.example.connect.databinding.ActivityVerificationBinding
import com.example.connect.models.users
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

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
        val name = binding.registerUsernameTextView.text.toString()
        val email = binding.registerEmailTextView.text.toString()
        var phone = binding.registerNumberTextNumber.text.toString()

//        MaterialAlertDialogBuilder(this)
//            .setTitle("Phone Verification")
//            .setMessage("An OTP will be send on given number")
//            .setNegativeButton("Cancel") { dialog, _ ->
//                Toast.makeText(this, "Operation Cancelled", Toast.LENGTH_SHORT).show()
//                dialog.cancel()
//            }
//            .setPositiveButton("OK") { dialog, _ ->
//                // Respond to positive button press
//                dialog.dismiss()
//            }
//            .show()
//
        //error checking for different fields
        if(name.isEmpty()){
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
        }
        if(phone.isEmpty() || phone.length!=10){
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
        }
        phone = "+91$phone"

        binding.progressBar.visibility=View.VISIBLE
        binding.register.visibility=View.GONE
        Toast.makeText(this, "Connecting to Database", Toast.LENGTH_SHORT).show()
        val ref = FirebaseDatabase.getInstance().getReference("/user");
        val phoneQuery: Query = ref.orderByChild("number").equalTo(phone)
        phoneQuery.addListenerForSingleValueEvent(object :  ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    Toast.makeText(this@RegisterActivity, "Already Registered Try to login", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent);
                }
                else{
                    val intent = Intent(this@RegisterActivity,VerificationActivity::class.java)
                    intent.putExtra("name",name);
                    intent.putExtra("email",email);
                    intent.putExtra("phone",phone);
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent);
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Error Occurred Try Later", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


}