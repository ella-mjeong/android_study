package com.example.myapplication_new

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication_new.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    var spFlag = 0
    private lateinit var binding: ActivityLoginBinding

    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLoginBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        supportActionBar?.hide()

        pref = getSharedPreferences("mine", Activity.MODE_PRIVATE)
        editor = pref.edit()

        binding.btnLogin.setOnClickListener {
            var id = binding.edtId.text.toString()
            var pw = binding.edtPw.text.toString()

            editor.putString("saveId", id)
            editor.putString("savePw", pw)
            editor.commit()
            spFlag = 1


            if ((id == "user@naver.com") && (pw == "1q2w"))
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                var loginfailalert = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                loginfailalert.setMessage("카카오계정 또는 비밀번호를 다시 확인해주세요.")
                loginfailalert.setPositiveButton("확인",null)
                loginfailalert.show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.edtId.setText("")
        binding.edtPw.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        spFlag=0
        editor.remove("saveId").commit()
        editor.remove("savePw").commit()
    }

}