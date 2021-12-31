package com.example.app_w5

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.app_w5.databinding.ActivityThirdBinding
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.ByteArrayOutputStream

var chooseFlag = false

class ThirdActivity : AppCompatActivity() {

//    private val URL = "http://api.openweathermap.org/data/2.5/weather?q=Seoul&appid="
//    private val API_KEY = "dc0ccd65c3660cc9e81087398c71edaa"

    companion object{
        var BaseUrl = "http://api.openweathermap.org/"
        var AppId = "dc0ccd65c3660cc9e81087398c71edaa"
        var lat = "37.445293"
        var lon = "126.785823"

        var movieBaseUrl = "http://kobis.or.kr/"
        var key = "aa83b3aa93050f7b895aab234cc176cb"
    }

    var imgData:Bitmap ?= null

    lateinit var binding: ActivityThirdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnWeather.setOnClickListener {
            getWeather()
            Toast.makeText(this,"날씨를 불러왔습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.editTextDate.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?){
                //텍스트를 입력 후
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 전
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 중
                binding.btnMovie.isEnabled = binding.editTextDate.length() >= 8
                binding.btnMovie.setOnClickListener {
                    var intent = Intent(applicationContext, FourthActivity::class.java)
                    intent.putExtra("date",binding.editTextDate.text.toString())
                    Log.d("MainActivity", "date: " + binding.editTextDate.text.toString())
                    startActivity(intent)
                }
            }
        })

        binding.btnSave.setOnClickListener {
            //데이터 저장
            if(imgData == null){
                myListArrayList.add( MyListClass(binding.title.text.toString(),binding.infoWeather.text.toString(), binding.region.text.toString(),
                    binding.editTextDate.text.toString(), binding.movieName.text.toString(), binding.review.text.toString(), binding.year.text.toString(),
                    binding.time.text.toString(), binding.genre.text.toString(), binding.company.text.toString(), binding.director.text.toString(),
                    binding.actor.text.toString(), null, myListArrayList.size.toString()))
            }
            else{
                myListArrayList.add( MyListClass(binding.title.text.toString(),binding.infoWeather.text.toString(), binding.region.text.toString(),
                    binding.editTextDate.text.toString(), binding.movieName.text.toString(), binding.review.text.toString(), binding.year.text.toString(),
                    binding.time.text.toString(), binding.genre.text.toString(), binding.company.text.toString(), binding.director.text.toString(),
                    binding.actor.text.toString(), imgData, myListArrayList.size.toString()))
            }
//            imgData?.let { it1 ->
//                MyListClass(binding.title.text.toString(),binding.infoWeather.text.toString(), binding.region.text.toString(),
//                        binding.editTextDate.text.toString(), binding.movieName.text.toString(), binding.review.text.toString(), binding.year.text.toString(),
//                        binding.time.text.toString(), binding.genre.text.toString(), binding.company.text.toString(), binding.director.text.toString(),
//                        binding.actor.text.toString(), it1)
//            }?.let { it2 -> myListArrayList.add(it2) }
            addFlag = true
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
        }
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
        }
        binding.myImg.setOnClickListener {
            val intentImg = Intent(Intent.ACTION_GET_CONTENT)
            intentImg.setType("image/*")
            startActivityForResult(intentImg,1)
        }
    }

    override fun onResume() {
        super.onResume()
        if(chooseFlag) {
            getMovie()
            chooseFlag = false
        }
    }

    fun getMovie(){
        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
                .baseUrl(movieBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(MymovieService::class.java)
        val call = service.getBoxOfficeData(key,movieCnt)
        call.enqueue(object : Callback<Mymovie> {
            override fun onFailure(call: Call<Mymovie>, t: Throwable) {
                Log.d("MainActivity", "result :" + t.message)
            }
            override fun onResponse(
                    call: Call<Mymovie>,
                    response: Response<Mymovie>
            ) {
                if(response.code() == 200){
                    val mymovieResponse = response.body()
                    Log.d("MainActivity", "result: " + mymovieResponse.toString())

                    binding.movieName.text = mymovieResponse!!.movieInfoResult?.movieInfo?.movieNm
                    binding.time.text = mymovieResponse!!.movieInfoResult?.movieInfo?.showTm
                    binding.year.text = mymovieResponse!!.movieInfoResult?.movieInfo?.openDt
                    binding.company.text = mymovieResponse!!.movieInfoResult?.movieInfo?.companys?.get(0)?.companyNm
                    binding.genre.text = mymovieResponse!!.movieInfoResult?.movieInfo?.genres?.get(0)?.genreNm
                    binding.director.text = mymovieResponse!!.movieInfoResult?.movieInfo?.directors?.get(0)?.peopleNm
                    intent.putExtra("date",binding.editTextDate.text.toString())

                    val listSize = mymovieResponse!!.movieInfoResult?.movieInfo?.actors!!.size
                    Log.d("MainActivity", "size: " + listSize)
                    //for(i in 0 until listSize.toInt()){
                        binding.actor.text = mymovieResponse!!.movieInfoResult?.movieInfo?.actors!!.get(0).peopleNm
                    //}
                }
            }
        })
    }


    fun getWeather(){
        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("MainActivity", "result :" + t.message)
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if(response.code() == 200){
                    val weatherResponse = response.body()
                    Log.d("MainActivity", "result: " + weatherResponse.toString())
//                    var cTemp =  weatherResponse!!.main!!.temp - 273.15  //켈빈을 섭씨로 변환
//                    var minTemp = weatherResponse!!.main!!.temp_min - 273.15
//                    var maxTemp = weatherResponse!!.main!!.temp_max - 273.15

                    binding.infoWeather.text = weatherResponse!!.weather!!.get(0).main
                    binding.region.text = weatherResponse!!.sys!!.country
                    weatherResponse!!.weather!!.get(0).main?.let { weatherImg(it) }
                }
            }
        })

    }


    fun weatherImg(weather: String) {
        if (weather == "Clouds")
            binding.weather.setImageResource(R.drawable.cloud)
        if (weather == "Mist")
            binding.weather.setImageResource(R.drawable.cloudy)
        if(weather == "Haze")
            binding.weather.setImageResource(R.drawable.cloudy)
        if (weather == "Rain")
            binding.weather.setImageResource(R.drawable.rainy)
        if(weather == "Clear")
            binding.weather.setImageResource(R.drawable.sunny)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                var currentImageUrl: Uri? = data?.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                    imgData = bitmap
                    //imgString = bitmapToString(bitmap)
                    binding.myImg.setImageBitmap(bitmap)
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

    fun stringToBitmap(encodedString: String): Bitmap {
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}

interface WeatherService{
    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String) :
            Call<WeatherResponse>
}

class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("main") var main: Main? = null
    @SerializedName("wind") var wind : Wind? = null
    @SerializedName("sys") var sys: Sys? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon : String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()

}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
}

class Sys {
    @SerializedName("country")
    var country: String? = null
    @SerializedName("sunrise")
    var sunrise: Long = 0
    @SerializedName("sunset")
    var sunset: Long = 0
}


interface MymovieService{

    @GET("kobisopenapi/webservice/rest/movie/searchMovieInfo.json?")
    fun getBoxOfficeData(
            @Query("key") key: String,
            @Query("movieCd") movieCd: String) :
            Call<Mymovie>

}

class Mymovie() {
    @SerializedName("movieInfoResult") var movieInfoResult: MovieInfoResult? = null
}

class Actor {
    @SerializedName("peopleNm")
    var peopleNm: String? = null
}


public class Director {
    @SerializedName("peopleNm")
    var peopleNm: String? = null
}

class Genre {
    @SerializedName("genreNm")
    var genreNm: String? = null
}

class Company {
    @SerializedName("companyNm")
    var companyNm: String? = null
    @SerializedName("companyPartNm")
    var companyPartNm: String? = null
}

class MovieInfo {

    @SerializedName("movieCd")
    var movieCd: String? = null
    @SerializedName("movieNm")
    var movieNm: String? = null
    @SerializedName("showTm")
    var showTm: String? = null
    @SerializedName("openDt")
    var openDt: String? = null
    @SerializedName("genres")
    var genres: List<Genre>? = null
    @SerializedName("directors")
    var directors: List<Director>? = null
    @SerializedName("actors")
    var actors: List<Actor>? = null
    @SerializedName("companys")
    var companys: List<Company>? = null

}

class MovieInfoResult {
    @SerializedName("movieInfo") var movieInfo:MovieInfo? =null
}

