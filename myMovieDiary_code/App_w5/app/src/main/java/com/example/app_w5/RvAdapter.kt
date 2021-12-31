package com.example.app_w5

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RvAdapter(val context: Context, val myMovieList: ArrayList<MyListClass>) :
    RecyclerView.Adapter<RvAdapter.Holder>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val rYear = itemView?.findViewById<TextView>(R.id.listYear)
        val rMonth = itemView?.findViewById<TextView>(R.id.listMonth)
        val rDay = itemView?.findViewById<TextView>(R.id.listDay)
        val rTitle = itemView?.findViewById<TextView>(R.id.listTitle)
        val rWeather = itemView?.findViewById<TextView>(R.id.listWeather)
        val rRegion = itemView?.findViewById<TextView>(R.id.listRegion)
        val rMovieName = itemView?.findViewById<TextView>(R.id.listMovieName)
        val rReview = itemView?.findViewById<TextView>(R.id.listReview)
        val rNum = itemView?.findViewById<TextView>(R.id.txtNum)
        val rMyImg = itemView?.findViewById<ImageView>(R.id.listImg)
        val rWeatherImg = itemView?.findViewById<ImageView>(R.id.listWeatherImg)


        @SuppressLint("ResourceType")
        fun bind (myList: MyListClass, context: Context) {
            /* myImage setImageBitmap에 들어갈 이미지를 찾고,
            이미지가 없는 경우 기본 이미지를 표시한다.*/
            if (myList.myImg != null) {
                rMyImg?.setImageBitmap(myList.myImg)
            } else {
                rMyImg?.setImageResource(R.drawable.gallary)
            }


            if (myList.weather == "Clouds")
                rWeatherImg?.setImageResource(R.drawable.cloud)
            if (myList.weather == "Mist")
                rWeatherImg?.setImageResource(R.drawable.cloudy)
            if(myList.weather == "Haze")
                rWeatherImg?.setImageResource(R.drawable.cloudy)
            if (myList.weather == "Rain")
                rWeatherImg?.setImageResource(R.drawable.rainy)
            if(myList.weather == "Clear")
                rWeatherImg?.setImageResource(R.drawable.sunny)

            /* 나머지 TextView와 String 데이터를 연결한다. */
            rYear?.text = date?.substring(0,4)
            rMonth?.text = date?.substring(4,6)
            rDay?.text = date?.substring(6)
            rTitle?.text = myList.title
            rWeather?.text = myList.weather
            rRegion?.text = myList.region
            rMovieName?.text = myList.movieName
            rReview?.text = myList.review

            rNum?.text = (myList.num.toInt() + 1).toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return myMovieList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(myMovieList[position], context)
    }


}