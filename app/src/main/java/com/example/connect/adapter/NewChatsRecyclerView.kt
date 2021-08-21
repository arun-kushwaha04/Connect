package com.example.connect.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.connect.ChatLogActivity
import com.example.connect.R
import com.example.connect.models.users
import com.squareup.picasso.Picasso

class NewChatsRecyclerView (
    private val users: ArrayList<users>,
    val context: Context
        ):RecyclerView.Adapter<NewChatsRecyclerView.MyViewHolder>(){
            class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
                private val image: ImageView = itemView.findViewById(R.id.profile_photo_card_view)
                private val username: TextView = itemView.findViewById(R.id.username_card_view)
                private val phone:TextView = itemView.findViewById(R.id.user_recent_message)
                private val email:TextView = itemView.findViewById(R.id.user_email)
                val cardView:ConstraintLayout = itemView.findViewById(R.id.user_card_view)
                fun bindView(userData: users) {
                    Picasso.get().load(userData.profilePhoto).into(image)
                    username.text = userData.username
                    phone.text= userData.number
                    email.text=userData.email
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_card_view,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(users[position])
        holder.cardView.setOnClickListener{
            val userData = users[position]
            val intent = Intent(it.context,ChatLogActivity::class.java)
            intent.putExtra("user",userData)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}