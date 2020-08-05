package com.sunny93.suarapantairileksasi

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var sound1: MediaPlayer
    private lateinit var sound2: MediaPlayer
    private lateinit var sound3: MediaPlayer

    var timer: Long = 0
    var isCountDown: Boolean = false
    private lateinit var countDownTimer: CountDownTimer

    private var dataTime: Long = 0
    var prefTimeData: String = "TIME DATA"
    private lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onResume() {
        super.onResume()
        dataTime = preference.getLong(prefTimeData, 0)
        timer = dataTime
        println("onResume $dataTime")
    }

    private fun init() {
        preference = this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)

        sound1 = MediaPlayer.create(this, R.raw.pelni)
        sound2 = MediaPlayer.create(this, R.raw.ombak)
        sound3 = MediaPlayer.create(this, R.raw.camar)

        ivPlayer1.setOnClickListener { playPlayer(1) }
        ivPlayer2.setOnClickListener { playPlayer(2) }
        ivPlayer3.setOnClickListener { playPlayer(3) }

        Picasso.get().load(R.drawable.a).into(bgPlayer1)
        Picasso.get().load(R.drawable.a).into(bgPlayer2)
        Picasso.get().load(R.drawable.a).into(bgPlayer3)

        fab.setOnClickListener { TimerDialog(this).show() }
    }

    private fun playPlayer(i: Int) {
        when (i) {
            1 -> if (sound1.isPlaying) {
                sound1.pause()
                ivPlayer1.setImageResource(R.drawable.ic_play)
            } else {
                sound1.start()
                sound1.isLooping = true
                ivPlayer1.setImageResource(R.drawable.ic_pause)
                if(!isCountDown) startCountdown()
            }
            2 -> if (sound2.isPlaying) {
                sound2.pause()
                ivPlayer2.setImageResource(R.drawable.ic_play)
            } else {
                sound2.start()
                sound2.isLooping = true
                ivPlayer2.setImageResource(R.drawable.ic_pause)
                if(!isCountDown) startCountdown()
            }
            3 -> if (sound3.isPlaying) {
                sound3.pause()
                ivPlayer3.setImageResource(R.drawable.ic_play)
            } else {
                sound3.start()
                sound3.isLooping = true
                ivPlayer3.setImageResource(R.drawable.ic_pause)
                if(!isCountDown) startCountdown()
            }
        }
    }

    private fun startCountdown() {
        isCountDown = true
        tv_main_timer.visibility = View.VISIBLE
        countDownTimer = object : CountDownTimer(timer, 1000) {
            override fun onFinish() {
                timer = 0
                tv_main_timer.visibility = View.GONE
                if(sound1.isPlaying) sound1.pause()
                if(sound2.isPlaying) sound2.pause()
                if(sound3.isPlaying) sound3.pause()
                ivPlayer1.setImageResource(R.drawable.ic_play)
                isCountDown = false
            }

            override fun onTick(millisUntilFinished: Long) {
                timer = millisUntilFinished
                setText()
            }
        }.start()
    }

    fun setText() {
        val format = String.format("%02d:%02d", (timer / 1000) / 60, (timer / 1000) % 60)
        tv_main_timer.text = format
    }

}