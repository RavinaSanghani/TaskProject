package com.example.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.task.databinding.ActivitySplashTaskBinding
class SplashTaskActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySplashTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashTaskBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, RepositoryListActivity::class.java))
            finishAffinity()
        }, 2000)
    }
}