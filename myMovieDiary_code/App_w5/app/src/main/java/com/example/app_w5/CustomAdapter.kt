package com.example.app_w5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.app_w5.databinding.ListviewItemBinding

class CustomAdapter(context: FourthActivity, private val boxofficeArrayList: ArrayList<BoxofficeClass>) :BaseAdapter(){

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
    lateinit var binding:ListviewItemBinding

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {

       // binding = ListviewItemBinding.inflate(inflater,p2,false)
        binding = ListviewItemBinding.inflate(inflater,p2,false)
        binding.name.text = boxofficeArrayList[position].name
        binding.code.text = boxofficeArrayList[position].code

        return binding.root
    }

    override fun getItem(p0: Int): Any =boxofficeArrayList[p0]

    override fun getItemId(p0: Int): Long =p0.toLong()

    override fun getCount(): Int = boxofficeArrayList.size

}