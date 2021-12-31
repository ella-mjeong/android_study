package com.example.app_w5

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.app_w5.databinding.ActivitySecondBinding
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

var addFlag = false

data class MyListClass(
    val title:String, val weather:String, val region:String, val date:String, val movieName:String, val review:String,
    val year:String, val time:String, val genre:String, val company:String, val director:String, val actor:String, val myImg: Bitmap?,
    val num: String)

var myListArrayList = ArrayList<MyListClass>()

class SecondActivity : AppCompatActivity() {

    lateinit var mGlideRequestManager : RequestManager
    lateinit var binding: ActivitySecondBinding

    lateinit var mAdapter: RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (!isLogin) finish()

        mGlideRequestManager = Glide.with(binding.myProfile)

        //리싸이클러뷰 어댑터 생성 및 연결
        mAdapter = RvAdapter(this,myListArrayList)
        binding.mRecyclerView.adapter = mAdapter

        //리싸이클러뷰 매니저 설정
       val lm = LinearLayoutManager(this)
        binding.mRecyclerView.layoutManager = lm
        //item이 추가되거나 삭제될 때 RecyclerView의 크기가 변경될 수도 있고,
        // 그렇게 되면 계층 구조의 다른 View 크기가 변경될 가능성이 있기 때문에 setHasFixedSize(true)로 설정
        // 특히 item이 자주 추가/삭제되면 오류가 날 수도 있기에 setHasFixedSize true를 설정한다.
        binding.mRecyclerView.setHasFixedSize(true)

        UserApiClient.instance.me() { user: User?, throwable: Throwable? ->
            if (user != null) {
                Log.i("log",(user.kakaoAccount!!.profile!!.nickname))
                binding.myName.text = (user.kakaoAccount!!.profile!!.nickname)
                mGlideRequestManager.load(user.kakaoAccount!!.profile!!.thumbnailImageUrl).circleCrop().into(binding.myProfile)
            }
        }

        binding.btnLogOut.setOnClickListener {
            logOut()
        }

        binding.btnDelete.setOnClickListener {
            //deleteItem()
        }


        binding.btnAdd.setOnClickListener {
            var intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        if(addFlag) {
            mAdapter.notifyDataSetChanged()
            addFlag = false
        }
    }

    fun deleteItem(){
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("게시글 삭제")
        dialog.setMessage("몇 번째 글을 삭제하시겠습니까?")
        fun toast_p() {
            Toast.makeText(this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
        }
        var dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    //아이템삭제



                    //순번 재정렬

                    mAdapter.notifyDataSetChanged()
                    toast_p()
                }
            }
        }
        dialog.setPositiveButton("삭제", dialogListener)
        dialog.setNegativeButton("취소", dialogListener)
        dialog.show()
    }


    private fun logOut(){
        var dialog = AlertDialog.Builder(this)
        dialog.setMessage("로그아웃 하시겠습니까?")
        fun toast_p() {
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        }
        var dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    UserApiClient.instance.logout {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                    toast_p()
                    isLogin = false
                    editor.putString("state", isLogin.toString())
                    editor.commit()
                }
            }
        }
        dialog.setPositiveButton("YES", dialogListener)
        dialog.setNegativeButton("NO", dialogListener)
        dialog.show()
    }




}