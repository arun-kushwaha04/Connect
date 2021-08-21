package com.example.connect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connect.R
import com.example.connect.models.message
import com.squareup.picasso.Picasso

class RecentMessageRecyclerView(
    private val recentMessage:ArrayList<message>,
    private val context:Context
): RecyclerView.Adapter<RecentMessageRecyclerView.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val profilePhoto: ImageView = itemView.findViewById(R.id.profile_photo_card_view)
        val userName: TextView = itemView.findViewById(R.id.username_card_view)
        val recentMessageText: TextView = itemView.findViewById(R.id.user_recent_message)

        fun bindView(recentMessage:message){
//            Picasso.get().load(recentMessage.)
            recentMessageText.text = recentMessage.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recent_message_card_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(recentMessage[position])
    }

    override fun getItemCount(): Int {
        return recentMessage.size
    }
}