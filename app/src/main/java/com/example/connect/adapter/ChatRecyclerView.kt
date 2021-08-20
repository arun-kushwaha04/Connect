package com.example.connect.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.connect.R
import com.example.connect.models.message
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatRecyclerView(
    private val message: ArrayList<message>,
    val context: Context
): RecyclerView.Adapter<ChatRecyclerView.MyViewHolder>(){
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        @SuppressLint("SimpleDateFormat")
        private fun convertDate(timeInMilliseconds: Long, dateFormat: String?): String? {
            val dateObject = Date(timeInMilliseconds)
            val dateFormatter = SimpleDateFormat(dateFormat)
            return dateFormatter.format(dateObject)
        }
        fun bindView(message: message) {
            val messageContent:TextView = itemView.findViewById(R.id.message_content)
            val messageTime:TextView = itemView.findViewById(R.id.message_time)
            messageContent.text = message.content
            messageTime.text = convertDate(message.timestamp,"HH:MM")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(message[position].fromId == FirebaseAuth.getInstance().uid) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        Log.d(TAG,viewType.toString())
        val itemView:View = if(viewType == 0) {
            LayoutInflater.from(parent.context).inflate(R.layout.from_message, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.to_message, parent, false)
        }
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(message[position])
    }

    override fun getItemCount(): Int {
        return message.size;
    }

}