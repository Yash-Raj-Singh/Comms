package com.example.comms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.i
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comms.dao.PostDAO
import com.example.comms.models.postModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener{
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                //Toast.makeText(this, "Signout clicked", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle("EXIT")
                //set message for alert dialog
                builder.setMessage("You want to Sign-Out ?")
                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    FirebaseAuth.getInstance().signOut()
                    finish()
                    val intent = Intent(this, SignIn_Activity::class.java)
                    startActivity(intent)
                }
                //performing negative action
                builder.setNegativeButton("No"){dialogInterface, which ->
                    dialogInterface.cancel()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
//                alertDialog.setCancelable(false)
                alertDialog.show()
            true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }





    private fun setupRecyclerView() {
        postDao = PostDAO()
        val postscollection = postDao.postcollection
        val query = postscollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<postModel>().setQuery(query,postModel::class.java).build()

        adapter = PostAdapter(recyclerViewOption, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postID: String) {
        postDao.updateLike(postID)
    }
}