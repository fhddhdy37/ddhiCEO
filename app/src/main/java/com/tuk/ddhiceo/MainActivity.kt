package com.tuk.ddhiceo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tuk.ddhiceo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.btn1.setOnClickListener {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
        }
        bind.btn2.setOnClickListener {
            val intent = Intent(this, BottomActivity::class.java)
            startActivity(intent)
        }
        bind.btn3.setOnClickListener {
            val intent = Intent(this, CheckBusinessActivity::class.java)
            startActivity(intent)
        }
    }
}