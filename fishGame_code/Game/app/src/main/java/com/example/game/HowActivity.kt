package com.example.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.game.databinding.ActivityHowBinding
import com.example.game.databinding.ActivityMainBinding

class HowActivity : AppCompatActivity() {

        lateinit var binding: ActivityHowBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHowBinding.inflate(layoutInflater)
        val view = binding.root
        // 풀스크린모드
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}