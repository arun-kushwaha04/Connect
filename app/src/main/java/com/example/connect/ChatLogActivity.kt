package com.example.connect

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connect.adapter.ChatRecyclerView
import com.example.connect.databinding.ActivityChatLogBinding
import com.example.connect.models.message
import com.example.connect.models.users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
        getChatMessage()

    }

    private fun sendChatMessage(){
        val message = binding.messageTextView.editableText.toString()
        if(message.isNotEmpty()){
            val fromId = FirebaseAuth.getInstance().uid
            val toId = user.uid
            val ref = FirebaseDatabase.getInstance().getReference("/chat-message/$fromId/$toId").push()
            val toref = FirebaseDatabase.getInstance().getReference("/chat-message/$toId/$fromId").push()

            val key = ref.key
            if(key != null){
                val chatMessage = message(
                    key, message,FirebaseAuth.getInstance().uid!!, user.uid!!, System.currentTimeMillis()
                )
                ref.setValue(chatMessage)
                    .addOnSuccessListener {
                        Log.d(TAG, "Message Saved To Database")
                    }
                binding.messageTextView.setText("")
                toref.setValue(chatMessage)
            }
        }
    }
    private fun getChatMessage(){
        val  recyclerView = binding.chatsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val fromId = FirebaseAuth.getInstance().uid
        val toId = user.uid
        val ref=FirebaseDatabase.getInstance().getReference("/chat-message/$fromId/$toId")

        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(message::class.java)
                chatsMessage.add(message!!)
                recyclerView.adapter = ChatRecyclerView(chatsMessage,this@ChatLogActivity)
                recyclerView.scrollToPosition(chatsMessage.size - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}