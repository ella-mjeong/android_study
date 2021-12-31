package com.example.app_w5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.app_w5.databinding.ActivityFourthBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//var movieNumList = ArrayList<String>()
//var movieNameList = ArrayList<String>()

var movieCnt:String = ""
var date:String?=null

data class BoxofficeClass(val name:String, val code:String)
class FourthActivity : AppCompatActivity() {

    companion object{
        var BaseUrl = "http://kobis.or.kr/"
        var key = "89e26fec28e201f0b4228c1308c61f60"
    }

    var boxofficeArrayList = ArrayList<BoxofficeClass>()
    private lateinit var customAdapter: CustomAdapter

    lateinit var binding: ActivityFourthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFourthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        date = intent.getStringExtra("date")
        binding.todayDate.text = date

        supportActionBar?.hide()

        customAdapter = CustomAdapter(this,boxofficeArrayList)
//        binding.btnMovieList.setOnClickListener {
//            myBoxOffice(date!!)
//        }

    }

    override fun onResume() {
        super.onResume()
        date?.let { myBoxOffice(it) }
    }

    fun myBoxOffice(date:String){
//        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(BoxOfficeService::class.java)
        val call = service.getBoxOfficeData(key,date.toString())
        call?.enqueue(object : Callback<BoxOfficeResponse> {
            override fun onFailure(call: Call<BoxOfficeResponse>, t: Throwable) {
                Log.d("MainActivity", "result :" + t.message)
            }

            override fun onResponse(
                call: Call<BoxOfficeResponse>,
                response: Response<BoxOfficeResponse>
            ) {
                if(response.code() == 200){
                    val boxOfficeResponse = response.body()
                    Log.d("MainActivity", "result: " + boxOfficeResponse.toString())
                    val listSize = boxOfficeResponse!!.boxOfficeResult?.dailyBoxOfficeList!!.size
                    Log.d("MainActivity", "size: " + listSize)
                    for(i in 0 until listSize.toInt()){
                        boxOfficeResponse!!.boxOfficeResult?.dailyBoxOfficeList?.get(i)?.movieNm?.let {
                            boxOfficeResponse!!.boxOfficeResult?.dailyBoxOfficeList?.get(i)?.movieCd?.let { it1 ->
                                BoxofficeClass(
                                    it, it1
                                )
                            }
                        }?.let { boxofficeArrayList.add(it) }
                    }
                    binding.movieList.adapter = customAdapter
                    customAdapter.notifyDataSetChanged()

                }
            }
        })
//        customAdapter = CustomAdapter(this,boxofficeArrayList)
//        customAdapter.notifyDataSetChanged()
//        binding.movieList.adapter = customAdapter

        //Toast.makeText(this,"영화리스트를 불러왔습니다.", Toast.LENGTH_SHORT).show()

        // 리스트 클릭 이벤트 - 리스트뷰의 각 줄이 눌리는 시점의 이벤트
        binding.movieList.setOnItemClickListener { adapterView, view, i, l ->
            // 눌린 위치에 해당하는 목록이 어떤 목록인지 가져오기
            movieCnt = boxofficeArrayList[i].code
            // 선택된 목록정보를 가져왔으면 이제 화면 이동
            //val myintent = Intent(this, ThirdActivity::class.java)
//            // 정보를 담아주기
           // myintent.putExtra("movieCd", movieCnt)
            chooseFlag = true
            Log.d("MainActivity", "choose: " + movieCnt)
            // 화면 전환
           // startActivity(myintent)
            finish()
        }
    }
}


interface BoxOfficeService{

    @GET("kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?")
    fun getBoxOfficeData(
        @Query("key") key: String,
        @Query("targetDt") date: String) :
            Call<BoxOfficeResponse>

}
class BoxOfficeResponse(){
    @SerializedName("boxOfficeResult") var boxOfficeResult: BoxOfficeResult? = null
}

class BoxOfficeResult{
    @SerializedName("boxofficeType")
    var boxofficeType: String? = null
    @SerializedName("showRange")
    var showRange: String? = null
    @SerializedName("dailyBoxOfficeList")
    var dailyBoxOfficeList = ArrayList<DailyBoxOfficeList>()
}

class DailyBoxOfficeList{
    @SerializedName("rnum")
    var rnum: String? = null
    @SerializedName("rank")
    var rank: String? = null
    @SerializedName("movieCd")
    var movieCd: String? = null
    @SerializedName("movieNm")
    var movieNm: String? = null

}
