package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connect.adapter.NewChatsRecyclerView
import com.example.connect.databinding.ActivityNewChatBinding
import com.example.connect.models.users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class newChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNewChatBinding
    private lateinit var userList:ArrayList<users>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currUid = FirebaseAuth.getInstance().uid
        userList = arrayListOf();
        val recyclerView = binding.recyclerViewUsers
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val ref = FirebaseDatabase.getInstance().getReference("/user")
        Toast.makeText(this, "Fetching Users From Database", Toast.LENGTH_SHORT).show()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(users::class.java)
                        if (user != null) {
                            if(user.uid != currUid){
                                userList.add(user);
                            }
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                    recyclerView.adapter = NewChatsRecyclerView(userList,this@newChatActivity)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("NewChatsActivity",error.message)
            }
        })
    }
}