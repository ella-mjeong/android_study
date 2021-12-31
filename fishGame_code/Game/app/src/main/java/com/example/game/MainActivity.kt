package com.example.game

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.game.databinding.ActivityMainBinding

var w = 0
var h = 0
var touched_x: Float = 1400f
var touched_y: Float = 650f

var score: Int = 0

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        // 풀스크린모드
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)
        //
        val display: Display = windowManager.defaultDisplay
        w = display.width
        h = display.height

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, GameMainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHow.setOnClickListener {
            val intent = Intent(this, HowActivity::class.java)
            startActivity(intent)
        }

        binding.btnExit.setOnClickListener{
            //System.exit(0)
            finish()
        }

    }
}