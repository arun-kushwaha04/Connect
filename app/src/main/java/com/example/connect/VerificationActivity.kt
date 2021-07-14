package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.connect.databinding.ActivityVerificationBinding

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater);
        setContentView(binding.root)
    }
}