package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connect.adapter.ToChatRecylerView
import com.example.connect.databinding.ActivityChatLogBinding
import com.example.connect.models.message
import com.example.connect.models.users
import com.google.firebase.auth.FirebaseAuth

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

        chatsMessage = arrayListOf()
        val recyclerView = binding.chatsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        for(i in 1..5){
            chatsMessage.add( createChat("Hey I am using connect"))
        }
        recyclerView.adapter = ToChatRecylerView(chatsMessage,this)

    }
    private fun createChat(chat: String): message {
        return message(
            "tttt", chat,
            FirebaseAuth.getInstance().uid!!, user.uid!!, System.currentTimeMillis()
        )
    }
}