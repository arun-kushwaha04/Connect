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
import com.example.connect.models.users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class RecentMessageRecyclerView(
    private val recentMessageList:ArrayList<message>,
    private val context:Context
): RecyclerView.Adapter<RecentMessageRecyclerView.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val profilePhoto: ImageView = itemView.findViewById(R.id.profile_photo_card_view)
        val userName: TextView = itemView.findViewById(R.id.username_card_view)
        val recentMessageText: TextView = itemView.findViewById(R.id.user_recent_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recent_message_card_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, currPosition: Int) {
        val recentMessage = recentMessageList[currPosition]
        val id = if(recentMessage.toId == FirebaseAuth.getInstance().uid){
            recentMessage.fromId
        }else{
            recentMessage.toId
        }
        val position = holder.adapterPosition
        var user:users
        val ref = FirebaseDatabase.getInstance().getReference("/user/$id")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(users::class.java)!!
                Picasso.get().load(user.profilePhoto).into(holder.profilePhoto)
                holder.userName.text= user.username
                holder.recentMessageText.text = recentMessage.content
            }
            override fun onCancelled(error: DatabaseError) {
                recentMessageList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemChanged(position,recentMessageList.size)
            }
        })
    }

    override fun getItemCount(): Int {
        return recentMessageList.size
    }

}