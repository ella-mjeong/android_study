package com.example.myapplication_v3

import android.R
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication_v3.databinding.ActivitySecondBinding
import java.io.ByteArrayOutputStream


class SecondActivity : AppCompatActivity() {

    var dataNum = 0
    var loginState = ""
    var num = ""
    var img = ""
    var arrayList = ArrayList<String>()
    lateinit var binding: ActivitySecondBinding
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySecondBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        binding.txtId.text=intent.getStringExtra("myId")

        loginState = intent.getStringExtra("state")
        pref = getSharedPreferences(dataNum.toString(),Activity.MODE_PRIVATE)
        editor = pref.edit()
        if(loginState == "login"){
            num = pref.getString("tot",null).toString()
            img = pref.getString("img",null).toString()

            if(num != null){
                dataNum = num.toInt()
            }
            if(img != null){
                val bitmap = stringToBitmap(img)
                binding.imageView.setImageBitmap(bitmap)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        var cnt = 0
        while(cnt < dataNum){
            pref.getString(cnt.toString(),"None")?.let { arrayList.add(it) }
            cnt++
        }
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1,arrayList)
        binding.listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.imageView.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,1)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            editor.putString(dataNum.toString(),binding.edtData.text.toString())
            editor.commit()
            arrayList.add(binding.edtData.text.toString())
            val adapter = ArrayAdapter(this, R.layout.simple_list_item_1,arrayList)
            binding.listView.adapter = adapter
            dataNum++
            editor.remove("tot").commit()
            editor.putString("tot",dataNum.toString())
            editor.commit()
            binding.edtData.setText("")
        }
        binding.btnDelete.setOnClickListener {
            var cnt = 0
            while(cnt < dataNum){
                arrayList.removeAt(0)
                editor.remove(cnt.toString()).commit()
                cnt++
            }
            dataNum=0
            val adapter = ArrayAdapter(this, R.layout.simple_list_item_1,arrayList)
            binding.listView.adapter = adapter
        }
    }

    override fun onPause() {
        super.onPause()
        var cnt = 0
        while(cnt < dataNum){
            arrayList.removeAt(0)
            cnt++
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                var currentImageUrl: Uri? = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    img = bitmapToString(bitmap)
                    editor.putString("img", img)
                    editor.commit()
                    binding.imageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun bitmapToString(bitmap: Bitmap):String{
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun stringToBitmap(encodedString: String):Bitmap{
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }

}