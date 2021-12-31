package com.example.game

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.game.databinding.ActivityPlayAgainBinding

class PlayAgainActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayAgainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityPlayAgainBinding.inflate(layoutInflater)

        binding.btnPlayAgain.setOnClickListener {
            val intent = Intent(this, GameMainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}