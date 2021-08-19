package com.example.connect

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connect.adapter.ChatRecyclerView
import com.example.connect.databinding.ActivityChatLogBinding
import com.example.connect.models.message
import com.example.connect.models.users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatLogActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatLogBinding
    private lateinit var chatsMessage : ArrayList<message>
    private lateinit var user:users
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<users>("user")!!
        supportActionBar?.title = user.username

        binding.sendMessage.setOnClickListener(){
            sendChatMessage()
        }

        chatsMessage = arrayListOf()
        val recyclerView = binding.chatsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


        recyclerView.adapter = ChatRecyclerView(chatsMessage,this)

    }

    private fun sendChatMessage(){
        val message = binding.messageTextView.editableText.toString()
        if(message.isNotEmpty()){
            val ref = FirebaseDatabase.getInstance().getReference("/message").push()
            val chatMessage = message(
                ref.key!!, message,FirebaseAuth.getInstance().uid!!, user.uid!!, System.currentTimeMillis()
            )
            ref.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Message Saved To Database")
                }
            binding.messageTextView.setText("")
        }
    }
}