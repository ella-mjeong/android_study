package com.example.game

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.Window
import android.view.WindowManager
import com.example.game.databinding.ActivityGameMainBinding
import java.util.*

var time = -1
val dist : Array<Float> = arrayOf(-10f,-10f,-10f,-10f)
val distSecond : Array<Float> = arrayOf(-20f,-80f,-10f,-40f)
//val distLast : Array<Float> = arrayOf(-300f,-20f,-430f,-180f)

class GameMainActivity : AppCompatActivity() {

    private var isRunning = false
    private var timerTask: Timer? = null
    private var scoreTask: Timer? = null
    private var fishTask: Timer? = null

    private lateinit var binding: ActivityGameMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_game_main)
        binding = ActivityGameMainBinding.inflate(layoutInflater)
        val view = binding.root

        // 상단바 확장
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)

    }

    override fun onResume() {
        super.onResume()
        start()
        scoreStart()
        fishMove()
    }
    override fun onPause() {
        super.onPause()
        pause()
    }

    private fun start() {

        timerTask = kotlin.concurrent.timer(period = 1000) {
            time++
            var sec = time
            var min = sec / 60
            if(sec >= 60){
                sec -= 60 * min
            }

            runOnUiThread {
                binding.min.text = "$min"
                binding.sec.text = "$sec"
            }

        }

    }

    private fun scoreStart() {

        scoreTask = kotlin.concurrent.timer(period = 10) {

            runOnUiThread {
                binding.score.text = "$score"
            }
        }

    }

    private fun fishMove() {

        fishTask = kotlin.concurrent.timer(period = 10) {
            runOnUiThread {
                val random = Random()

                dist[0] += 1f
                dist[1] += 3f
                dist[2] += 2f
                dist[3] += 3f

                if(score >= 5) {
                    distSecond[0] += 3f
                    distSecond[1] += 2f
                    distSecond[2] += 3f
                    distSecond[3] += 1f
                }

//                if(score >= 15){
//                    distLast[0] += 1f
//                    distLast[1] += 2f
//                    distLast[2] += 3f
//                    distLast[3] += 3f
//                }

//                for(i in 0..3) {
//                    dist[i] += random.nextInt(4).toFloat() + 1f
//                    if(score >= 5) {
//                        distSecond[i] += random.nextInt(4).toFloat() + 1f
//                    }
//                }
            }
        }

    }


    private fun pause() {
        timerTask?.cancel()//timerTask가 null인지 체크
        fishTask?.cancel()
        scoreTask?.cancel()
    }


    override fun onRestart() {
        super.onRestart()
//        val intent = Intent(this, PlayAgainActivity::class.java)
//        startActivity(intent)
    }

}