package com.example.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.connect.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toRegisterActivity = binding.toRegisterActivity

        toRegisterActivity.setOnClickListener(){
            val intent = Intent(this,RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener(){
            var phone = binding.loginNumberTextField.text.toString()
            phone="+91$phone"

            val ref = FirebaseDatabase.getInstance().getReference("/user");
            val phoneQuery: Query = ref.orderByChild("number").equalTo(phone)
            phoneQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val intent = Intent(this@LoginActivity,VerificationActivity::class.java)
                        intent.putExtra("phone",phone);
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "Phone Number Not Registered Try to Register", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent);
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Error Occurred Try Later", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

}