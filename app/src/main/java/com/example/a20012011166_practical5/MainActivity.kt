package com.example.a20012011166_practical5

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.os.Handler
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Start the media player
        PlayBtn.setOnClickListener{
            if(pause){
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
                Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()
            }else{

                mediaPlayer = MediaPlayer.create(applicationContext,R.raw.song)
                mediaPlayer.start()
                Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()

            }
            initializeSeekBar()
            PlayBtn.isEnabled = false
            ShuffleBtn.isEnabled = true
            PreviousSkipBtn.isEnabled = true
            NextSkipBtn.isEnabled = true
            StopBtn.isEnabled = true

            mediaPlayer.setOnCompletionListener {
                PlayBtn.isEnabled = true
                ShuffleBtn.isEnabled = false
                PreviousSkipBtn.isEnabled = false
                NextSkipBtn.isEnabled = false
                StopBtn.isEnabled = false
                Toast.makeText(this,"end",Toast.LENGTH_SHORT).show()
            }
        }
        // Stop the media player
        StopBtn.setOnClickListener{
            if(mediaPlayer.isPlaying || pause.equals(true)){
                pause = false
                seek_bar.setProgress(0)
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)

                PlayBtn.isEnabled = true
                ShuffleBtn.isEnabled = false
                PreviousSkipBtn.isEnabled = false
                NextSkipBtn.isEnabled = false
                StopBtn.isEnabled = false
                tv_pass.text = "00 sec"
                tv_due.text = "00 sec"
                Toast.makeText(this,"media stop",Toast.LENGTH_SHORT).show()
            }
        }
        // Seek bar change listener
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }
    // Method to initialize seek bar and audio stats
    private fun initializeSeekBar() {
        seek_bar.max = mediaPlayer.seconds

        runnable = Runnable {
            seek_bar.progress = mediaPlayer.currentSeconds

            tv_pass.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            tv_due.text = "${mediaPlayer.seconds} sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}
// Creating an extension property to get the media player time duration in seconds
val MediaPlayer.seconds:Int
    get() {
        return this.duration / 1000
    }
// Creating an extension property to get media player current position in seconds
val MediaPlayer.currentSeconds:Int
    get() {
        return this.currentPosition/1000
    }