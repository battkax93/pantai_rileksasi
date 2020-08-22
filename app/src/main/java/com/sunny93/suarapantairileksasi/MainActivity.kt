package com.sunny93.suarapantairileksasi

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var sound1: MediaPlayer
    private lateinit var sound2: MediaPlayer
    private lateinit var sound3: MediaPlayer

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mAdView: AdView? = null

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
//        setText()
        println("onResume $dataTime")
    }

    private fun init() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        preference = this.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)

        mAdView = findViewById(R.id.adView)

        sound1 = MediaPlayer.create(this, R.raw.ombak)
        sound2 = MediaPlayer.create(this, R.raw.camar)
        sound3 = MediaPlayer.create(this, R.raw.pelni)

        ivPlayer1.setOnClickListener { playPlayer(1) }
        ivPlayer2.setOnClickListener { playPlayer(2) }
        ivPlayer3.setOnClickListener { playPlayer(3) }

        Picasso.get().load(R.drawable.pantai).into(bgPlayer1)
        Picasso.get().load(R.drawable.camar).into(bgPlayer2)
        Picasso.get().load(R.drawable.fery).into(bgPlayer3)

        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)

        bStop.setOnClickListener {
            stopMedia()
            bStop.visibility = View.GONE
            bPlay.visibility = View.VISIBLE
        }
        bPlay.setOnClickListener {
            playMedia()
            bStop.visibility = View.VISIBLE
            bPlay.visibility = View.GONE
        }
        fab.setOnClickListener {
            isCountDown = false
            TimerDialog(this).show()
        }
    }

    private fun playPlayer(i: Int) {
        when (i) {
            1 -> if (sound1.isPlaying) {
                sound1.pause()
                ivPlayer1.setImageResource(R.drawable.ic_play)
            } else {
                sendAnalytic("1", "sound 1")
                sound1.start()
                sound1.isLooping = true
                ivPlayer1.setImageResource(R.drawable.ic_pause)
//                if(!isCountDown) startCountdown(timer)

            }
            2 -> if (sound2.isPlaying) {
                sound2.pause()
                ivPlayer2.setImageResource(R.drawable.ic_play)
            } else {
                sendAnalytic("2", "sound 2")
                sound2.start()
                sound2.isLooping = true
                ivPlayer2.setImageResource(R.drawable.ic_pause)
//                if(!isCountDown) startCountdown(timer)

            }
            3 -> if (sound3.isPlaying) {
                sound3.pause()
                ivPlayer3.setImageResource(R.drawable.ic_play)
            } else {
                sendAnalytic("3", "sound 3")
                sound3.start()
                sound3.setVolume(10f, 10f)
                sound3.isLooping = true
                ivPlayer3.setImageResource(R.drawable.ic_pause)
//                if(!isCountDown) startCountdown(timer)
            }
        }
    }

    private fun stopMedia() {
        if (sound1.isPlaying) sound1.pause()
        if (sound2.isPlaying) sound2.pause()
        if (sound3.isPlaying) sound3.pause()
        ivPlayer1.setImageResource(R.drawable.ic_play)
        ivPlayer2.setImageResource(R.drawable.ic_play)
        ivPlayer3.setImageResource(R.drawable.ic_play)
    }

    private fun playMedia(){
        sound1.start()
        sound2.start()
        sound3.start()
        ivPlayer1.setImageResource(R.drawable.ic_pause)
        ivPlayer2.setImageResource(R.drawable.ic_pause)
        ivPlayer3.setImageResource(R.drawable.ic_pause)
    }

    private fun sendAnalytic(id: String, name: String) {
        val bundle = Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun startCountdown(tm: Long) {
        isCountDown = true
        vTimer.visibility = View.VISIBLE
        println("start countdown at $tm")
        countDownTimer = object : CountDownTimer(tm, 1000) {
            override fun onFinish() {
                timer = 0
                vTimer.visibility = View.GONE
                if (sound1.isPlaying) sound1.pause()
                if (sound2.isPlaying) sound2.pause()
                if (sound3.isPlaying) sound3.pause()
                ivPlayer1.setImageResource(R.drawable.ic_play)
                ivPlayer2.setImageResource(R.drawable.ic_play)
                ivPlayer3.setImageResource(R.drawable.ic_play)
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