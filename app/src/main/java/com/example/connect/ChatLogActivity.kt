package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.connect.databinding.ActivityChatLogBinding
import com.example.connect.models.users

class ChatLogActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<users>("user")!!
        actionBar?.title = user.username



    }
}