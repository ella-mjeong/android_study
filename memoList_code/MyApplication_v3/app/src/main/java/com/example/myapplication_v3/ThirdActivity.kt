package com.example.myapplication_v3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication_v3.databinding.ActivityThirdBinding

class ThirdActivity : Activity() {
    lateinit var binding: ActivityThirdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityThirdBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        binding.okBtn.setOnClickListener {
            finish()
        }
    }
    override fun onStop(){
        super.onStop()
    }
}