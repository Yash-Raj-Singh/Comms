package com.example.comms.dao

import com.example.comms.models.postModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDAO {
    val db = FirebaseFirestore.getInstance()
    val postcollection = db.collection("posts")
    val auth = Firebase.auth

    fun addpost(text: String){
        val currentUserId = auth.currentUser!!.uid
        GlobalScope.launch {
            val userDao = UserDAO()
            val user = userDao.getUserID(currentUserId).await().toObject(com.example.comms.models.User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = postModel(text, user, currentTime)
            postcollection.document().set(post)
        }
    }
}