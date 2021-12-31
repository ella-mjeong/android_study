package com.example.myapplication_new

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CompoundButton
import android.widget.ListView
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication_new.databinding.RecyclerviewItemBinding

class CustomAdapter(context: Context, private val myFriendArrayList: ArrayList<MyFriend>):BaseAdapter() {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var binding:RecyclerviewItemBinding

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        binding = RecyclerviewItemBinding.inflate(inflater,p2,false)
        binding.nameItem.text = myFriendArrayList[p0].name
        binding.contentItem.text = myFriendArrayList[p0].contents
        binding.switchUser.isChecked = myFriendArrayList[p0].checked

        binding.switchUser.setOnClickListener{
            val state:Boolean = !myFriendArrayList[p0].checked
            myFriendArrayList[p0].checked = state
        }


        return binding.root
    }

    override fun getItem(p0: Int): Any = myFriendArrayList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getCount(): Int = myFriendArrayList.size

}