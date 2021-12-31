package com.example.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread



class Game(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //캐릭터 정의
    val myCharacter = BitmapFactory.decodeResource(resources, R.drawable.my_fish)

    //캐릭터 비율조절
    val me_resize = Bitmap.createScaledBitmap(myCharacter, myCharacter.width / 2, myCharacter.height / 2, true)

    val size = 32//1단계(64)

    var fishLevel = 1
    var flag1 = 0

    var fishX = IntArray(4)
    var fishY = IntArray(4)
    var movement = IntArray(4)
    val fishFirst = arrayOfNulls<Bitmap>(4)
    val checkDist = IntArray(4)

    var fishSecondX = IntArray(4)
    var fishSecondY = IntArray(4)
    var movementSecond = IntArray(4)
    val fishSecond = arrayOfNulls<Bitmap>(4)
    val checkDistSecond = IntArray(4)

//    var fishLastX = IntArray(4)
//    var fishLastY = IntArray(4)
//    var movementLast = IntArray(4)
//    val fishLast = arrayOfNulls<Bitmap>(4)
//    val checkDistLast = IntArray(4)


    //터치이동
    var touch_listen: Int = 0

    //쓰레드
    private var gameThread: GameThread? = null

    init{
        if (gameThread == null) {
            gameThread = GameThread()
            gameThread!!.start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDetachedFromWindow() {
        //game_thread.run = false
        super.onDetachedFromWindow()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var handler: move_handler = move_handler()

        var myFish = BitmapFactory.decodeResource(resources, R.drawable.my_fish)
        myFish = Bitmap.createScaledBitmap(
            myFish,
            myCharacter.width / size,
            myCharacter.height / size,
            true
        )
        //Log.d("tag", "meme**********" + "??" + myCharacter.width / size + "**" + myCharacter.height / size)
        canvas!!.drawBitmap(myFish, touched_x.toFloat(), touched_y.toFloat(), null)

//        //물고기들
            fishLevel = 1
            if (flag1 == 0) {
                flag1 = 1

                fishY[0] = 100
                fishY[1] = 250
                fishY[2] = 500
                fishY[3] = 700

                fishX[0] = -3
                fishX[1] = -20
                fishX[2] = -50
                fishX[3] = -5
                fishSecondY[0] = 330
                fishSecondY[1] = 200
                fishSecondY[2] = 400
                fishSecondY[3] = 620

                fishSecondX[0] = -80
                fishSecondX[1] = -70
                fishSecondX[2] = -100
                fishSecondX[3] = -80

//                fishLastY[0] = 600
//                fishLastY[1] = 530
//                fishLastY[2] = 300
//                fishLastY[3] = 200
//
//                fishLastX[0] = -500
//                fishLastX[1] = -320
//                fishLastX[2] = -260
//                fishLastX[3] = -440

            }

            for (i in 0..3) {
                //fishX[i] += dist.toInt()
                if (dist[i] >= 1500f) {
                    clearView(i)
                }
                fishFirst[i] = BitmapFactory.decodeResource(resources, R.drawable.fish1 + i)
                fishFirst[i] = Bitmap.createScaledBitmap(fishFirst[i]!!, 50, 50, true)
                fishFirst[i]?.let {
                    canvas!!.drawBitmap(
                        it, fishX[i].toFloat() + dist[i], fishY[i].toFloat(), null
                        //canvas!!.drawBitmap(it, fishX[i].toFloat(), fishY[i].toFloat(), null
                    )
                }
                //Log.d("tag", "######**********" + i + "??" + fishX[i]+"**"+dist[i])

            }
        if(score >= 5) {

            for (i in 0..3) {
                //fishX[i] += dist.toInt()
                if (distSecond[i] >= 1500f) {
                    clearViewSecond(i)
                }
                fishSecond[i] = BitmapFactory.decodeResource(resources, R.drawable.fish11 + i)
                fishSecond[i] = Bitmap.createScaledBitmap(fishSecond[i]!!, 120, 120, true)
                fishSecond[i]?.let {
                    canvas!!.drawBitmap(
                        it, fishSecondX[i].toFloat() + distSecond[i], fishSecondY[i].toFloat(), null
                        //canvas!!.drawBitmap(it, fishX[i].toFloat(), fishY[i].toFloat(), null
                    )
                }
                Log.d("tag", "######Second**********" + i + "??" + fishSecondX[i] + "**" + distSecond[i])

            }
        }
//        if(score >= 15) {
//
//            for (i in 0..3) {
//                if (distLast[i] >= 1500f) {
//                    clearViewLast(i)
//                }
//                fishLast[i] = BitmapFactory.decodeResource(resources, R.drawable.fish61 + i)
//                fishLast[i] = Bitmap.createScaledBitmap(fishLast[i]!!, 180, 150, true)
//                fishLast[i]?.let {
//                    canvas!!.drawBitmap(
//                        it, fishLastX[i].toFloat() + distLast[i], fishLastY[i].toFloat(), null
//                        //canvas!!.drawBitmap(it, fishX[i].toFloat(), fishY[i].toFloat(), null
//                    )
//                }
//                Log.d("tag", "######Last**********" + i + "??" + fishLastX[i] + "**" + distLast[i])
//
//            }
//        }
//        if(score > 50){
//            fishLevel = 2
//        }
//        else if(score < 70){
//            fishLevel = 3
//        }
//        else{
//            fishLevel = 4
//        }

    }

    private fun clearView(num: Int) {
        dist[num] = 0f
        Log.d("tag", "dist!********" + dist[num])
        val random = Random()
            fishY[num] = random.nextInt(80) * 10
            fishX[num] = random.nextInt(5) * (-10) + random.nextInt(10)//-50부터 0사이
    }

    private fun clearViewSecond(num: Int) {
        val random = Random()
        distSecond[num] = 0f + random.nextInt(10) * (-2)
        Log.d("tag", "distSecond!********" + distSecond[num])
        fishSecondY[num] = random.nextInt(80) * 10
        fishSecondX[num] = random.nextInt(10) * (-10) + random.nextInt(10)//-50부터 0사이
    }

//    private fun clearViewLast(num: Int) {
//        val random = Random()
//        distLast[num] = 0f + random.nextInt(10) * (-4)
//        Log.d("tag", "distSecond!********" + distLast[num])
//        fishLastY[num] = random.nextInt(80) * 10
//        fishLastX[num] = random.nextInt(10) * (-10) + random.nextInt(10)//-50부터 0사이
//    }

    private fun checkLocation() {
       //touchedX, touchedY랑 물고기좌표랑 비교해서 충돌하면 점수올려주고 물고기 삭제
        val random = Random()

        for (i in 0..3) {
            if((touched_x - 15) <= (fishX[i] + dist[i] + 30) && (touched_x + 15) >= (fishX[i]+ dist[i] -30)
                && (touched_y - 5) <= (fishY[i] + 30) && (touched_y + 5) >= (fishY[i] -30) ){
                score++
                fishY[i] = random.nextInt(80) * 10
                fishX[i] = random.nextInt(10) * (-10) + random.nextInt(10)//-100부터 0사이
                dist[i] = 0f
                //Log.d("tag", "삭제!*******" + i)
            }

            if((touched_x ) <= (fishSecondX[i] + distSecond[i] + 55) && (touched_x ) >= (fishSecondX[i]+ distSecond[i] -55)
                && (touched_y )<= (fishSecondY[i] + 55) && (touched_y) >= (fishSecondY[i] -55) ){
                //Log.d("tag", "????*******" + (fishSecondX[i] + distSecond[i]) + " *|* " + fishSecondY[i])
                var intent = Intent(context,ScoreActivity::class.java)
                intent.putExtra("score",score.toString())
                context.startActivity(intent)
            }

//            if(touched_x <= (fishLastX[i] + distLast[i] + 85) && touched_x >= (fishLastX[i]+ distLast[i] - 85)
//                && touched_y <= (fishLastY[i] + 60) && touched_y >= (fishLastY[i] -60) ){
//                Log.d("tag", "????*******" + (fishLastX[i] + distLast[i]) + " *|* " + fishLastY[i])
//                var intent = Intent(context,ScoreActivity::class.java)
//                intent.putExtra("score",score.toString())
//                context.startActivity(intent)
//            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_MOVE) {
            val moveX = event.getX()
            val moveY = event.getY()
            //Log.d("tag", "1**********" + moveX + " ||" + moveY)
            //if (move_y <= (binding.imgMe.height+10) && move_y >= (binding.imgMe.height-10) && move_x <= (binding.imgMe.width+10) && move_x >= (binding.imgMe.height-10)) {
            touched_y = moveY - 30
            touched_x = moveX - 150
            Log.d("tag", "2**********" + touched_x + " ||" + touched_y)
            checkLocation()
            invalidate()
            //}
        }
        return true
    }

        inner class move_handler : android.os.Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            postInvalidate()
        }
    }

//    inner class FishMoveThread : Thread() {
//        var run = true
//        override fun run() {
//            super.run()
//            while (run)
//                try {
//                    dist++
//                    Log.d("tag", "----**********" )
//                    sleep(10)
//                } catch (e: Exception) {
//
//                }
//        }
//    }

    internal inner class GameThread : Thread() {
        var run = true
        override fun run() {
            super.run()
            while (run) {
                postInvalidate()
                sleep(100)
            }
        }
    }

}