package com.example.app_w5

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.app_w5.databinding.ActivityMainBinding
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

var isLogin: Boolean = false

lateinit var pref: SharedPreferences
lateinit var editor: SharedPreferences.Editor

class MainActivity : AppCompatActivity() {

    var TAG = "MainActivity"

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        pref = getSharedPreferences("state", Activity.MODE_PRIVATE)
        editor = pref.edit()

        val tmp = pref.getString("state",null).toString()
        if(tmp == "true") isLogin = true
        else if(tmp == "false") isLogin = false


        var keyHash = Utility.getKeyHash(this)
        Log.d("KEY_HASH", keyHash)

        // 로그인 조합 예제
        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(this,"아이디와 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "로그인 실패", error)
            }
            else if (token != null) {
                Toast.makeText(this,"로그인 되었습니다",Toast.LENGTH_SHORT).show()
                isLogin = true
                editor.putString("state", isLogin.toString())
                editor.commit()
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
            }
            updateKakaoLoginUi()
            checkLogin()
        }

        binding.btnKakaoLogin.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            LoginClient.instance.run {
                if (isKakaoTalkLoginAvailable(this@MainActivity)) {
                    loginWithKakaoAccount(this@MainActivity, callback = callback)
                } else loginWithKakaoAccount(this@MainActivity, callback = callback)
            }
            updateKakaoLoginUi()
        }

        binding.btnLogout.setOnClickListener {
            logOut()
            isLogin = false
            editor.putString("state", isLogin.toString())
            editor.commit()
            updateKakaoLoginUi()
        }

        binding.profile.setOnClickListener {
//            var intent = Intent(this, SecondActivity::class.java)
//            startActivity(intent)
            if (isLogin)
                startActivity(Intent(this, SecondActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"**********************************************************************")
        updateKakaoLoginUi()
        Log.i(TAG,"------------------------------------------------------------------------")
        checkLogin()
    }

    fun updateKakaoLoginUi(){
        UserApiClient.instance.me() { user: User?, throwable: Throwable? ->
            if (user != null) { //로그인이 된 상태인지 아닌지 확인가능
                Log.i(TAG, "invoke: id = " + user.id)
                Log.i(TAG, "invoke: email = " + user.kakaoAccount!!.email)
                Log.i(TAG, "invoke: gender = " + user.kakaoAccount!!.gender)
                Log.i(TAG, "invoke: age = " + user.kakaoAccount!!.ageRange)

                binding.nickname.text = (user.kakaoAccount!!.profile!!.nickname)
                binding.nickname.textSize = 20f
                binding.btnKakaoLogin.visibility = View.GONE //로그인버튼은 화면에서 사라지게
                binding.btnLogout.visibility = View.VISIBLE //로그아웃버튼은 보이게
                binding.profile.visibility = View.VISIBLE
                Glide.with(binding.profile).load(user.kakaoAccount!!.profile!!.thumbnailImageUrl).circleCrop().into(binding.profile)

            }
            else {
                binding.nickname.text="MY MOVIE\nDIARY"
                binding.nickname.textSize = 45f
                binding.profile.setImageResource(R.drawable.app_icon)
                binding.btnKakaoLogin.visibility = View.VISIBLE //로그인버튼은 보이게
                binding.btnLogout.visibility = View.GONE //로그아웃버튼은 화면에서 사라지게

            }
        }
    }

    private fun logOut(){
        var dialog = AlertDialog.Builder(this)
        dialog.setMessage("로그아웃 하시겠습니까?")
        fun toast_p() {
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        }
        var dialogListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        UserApiClient.instance.logout {
                            updateKakaoLoginUi()
                        }
                        toast_p()
                        isLogin = false
                        editor.putString("state", isLogin.toString())
                        editor.commit()
                    }
                }
            }
        }
        dialog.setPositiveButton("YES", dialogListener)
        dialog.setNegativeButton("NO", dialogListener)
        dialog.show()
    }

    fun checkLogin(){
        if (isLogin){
            Log.i(TAG,"로그인 확인됨")
            //startActivity(Intent(this, SecondActivity::class.java))
        }
    }

}