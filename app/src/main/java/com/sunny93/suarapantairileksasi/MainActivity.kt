package com.sunny93.suarapantairileksasi

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var sound1: MediaPlayer
    private lateinit var sound2: MediaPlayer
    private lateinit var sound3: MediaPlayer

    var startTime: Long = 10_000L
    var timer = startTime
    lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        sound1 = MediaPlayer.create(this, R.raw.pelni)
        sound2 = MediaPlayer.create(this, R.raw.ombak)
        sound3 = MediaPlayer.create(this, R.raw.camar)

        ivPlayer1.setOnClickListener { playPlayer(1) }
        ivPlayer2.setOnClickListener { playPlayer(2) }
        ivPlayer3.setOnClickListener { playPlayer(3) }

        Picasso.get().load(R.drawable.a).into(bgPlayer1)

        skPlayer1.progress = 100
        val audioManager =
            applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        skPlayer1.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        skPlayer1.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                tvVol1.text = "Media Volume : $progress";
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun playPlayer(i: Int) {
        when (i) {
            1 -> if (sound1.isPlaying) {
                sound1.pause()
                ivPlayer1.setImageResource(R.drawable.ic_play)
                skPlayer1.visibility = View.INVISIBLE
                tvVol1.visibility = View.INVISIBLE
            } else {
                sound1.start()
                sound1.isLooping = true
                ivPlayer1.setImageResource(R.drawable.ic_pause)
                skPlayer1.visibility = View.VISIBLE
                tvVol1.visibility = View.VISIBLE
                startCountdown()
            }
            2 -> if (sound2.isPlaying) {
                sound2.pause()
            } else {
                sound2.start()
                sound2.isLooping = true
            }
            3 -> if (sound3.isPlaying) {
                sound3.pause()
            } else {
                sound3.start()
                sound3.isLooping = true
            }
        }
    }

    private fun startCountdown() {
        tv_main_timer.visibility = View.VISIBLE
        countDownTimer = object : CountDownTimer(timer, 1000) {
            override fun onFinish() {
                tv_main_timer.visibility = View.GONE
                sound1.stop()
                ivPlayer1.setImageResource(R.drawable.ic_play)
                skPlayer1.visibility = View.INVISIBLE
                tvVol1.visibility = View.INVISIBLE
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