package com.example.comms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.comms.dao.PostDAO
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {
    private lateinit var postDao: PostDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        postDao = PostDAO()
        postButton.setOnClickListener{
            val input = postInput.text.toString().trim()
            if(input.isNotEmpty()){
                postDao.addpost(input)
                finish()
            }
        }
    }
}