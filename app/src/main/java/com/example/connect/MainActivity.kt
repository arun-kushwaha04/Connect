package com.example.connect

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connect.adapter.RecentMessageRecyclerView
import com.example.connect.databinding.ActivityMainBinding
import com.example.connect.models.message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recentMessageList: ArrayList<message>
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserIsLoggedIn()
        recentMessageList= arrayListOf()
        recyclerView = binding.recentMessageRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        getRecentMessages()

    }
    private fun checkUserIsLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.newChatActivity ->{
                val intent = Intent(this,newChatActivity::class.java)
                startActivity(intent)
            }
            R.id.signOut ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun getRecentMessages(){
        val userUid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-message/$userUid")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val recentMessage = snapshot.getValue(message::class.java)
                if (recentMessage != null) {
                    Log.d(TAG,recentMessage.toString())
                    recentMessageList.add(recentMessage)
                }
                recyclerView.adapter = RecentMessageRecyclerView(recentMessageList,this@MainActivity)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val changedRecentMessage = snapshot.getValue(message::class.java)
                if (changedRecentMessage != null) {
                    for (message in recentMessageList) {
                        message.id?.let { Log.d(TAG, it) }
                        if(snapshot.key == message.fromId || snapshot.key == message.toId){
                            recentMessageList.remove(message)
                            recentMessageList.add(0,changedRecentMessage)
                            Log.d(TAG,recentMessageList.toString())
                            break
                        }
                    }
                    recyclerView.adapter = RecentMessageRecyclerView(recentMessageList,this@MainActivity)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}