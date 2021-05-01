package com.example.comms.dao

import com.example.comms.models.postModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
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

    fun getpostbyID(postID: String): Task<DocumentSnapshot>{
        return postcollection.document(postID).get()
    }

    fun updateLike(postID: String){
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getpostbyID(postID).await().toObject(postModel::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked){
                post.likedBy.remove(currentUserId)
            }
            else{
                post.likedBy.add(currentUserId)
            }
            postcollection.document(postID).set(post)
        }
    }
}