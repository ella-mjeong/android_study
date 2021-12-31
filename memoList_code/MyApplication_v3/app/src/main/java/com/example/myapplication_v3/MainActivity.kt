package com.example.myapplication_v3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication_v3.databinding.ActivityMainBinding
import android.content.SharedPreferences
import android.util.Log
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    var spFlag = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref:SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)

        pref = getSharedPreferences("mine",Activity.MODE_PRIVATE)
        editor = pref.edit()
    }

    override fun onResume() {
        super.onResume()
        binding.btnLogin.setOnClickListener {
            var id = binding.edtId.text.toString()
            var pw = binding.edtPw.text.toString()

            if(!Pattern.matches("^(?=.*[a-zA-Z]+)(?=.*[0-9]+).{4,8}$",pw)) {
                Toast.makeText(this, "비밀번호는 영문자와 숫자를 포함한 4~8자형식이어야합니다.", Toast.LENGTH_SHORT).show()
                binding.edtPw.setText("")
                return@setOnClickListener
            }
            if(spFlag == 0) {
                editor.putString("saveId", id)
                editor.putString("savePw", pw)
                editor.commit()
                spFlag = 1
                var intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("myId", id)
                intent.putExtra("state","none");
                startActivity(intent)
            }
            else if(id!=pref.getString("saveId","None") || pw != pref.getString("savePw","None")){
                Log.v("*********", "not correct" + "ID is " + pref.getString("saveId","None") + " PW is " + pref.getString("savePw","None") )
                binding.edtId.setText("")
                binding.edtPw.setText("")
                var intentThird = Intent(this, ThirdActivity::class.java)
                startActivity(intentThird)
            }
            else{
                var intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("myId", id)
                intent.putExtra("myPw", pw)
                intent.putExtra("state","login");
                startActivity(intent)
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