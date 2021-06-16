package com.example.listenme.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listenme.R
import com.example.listenme.Viewmodel.MusicViewmodel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MusicViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel= ViewModelProvider(this).get(MusicViewmodel::class.java)
        supportFragmentManager.beginTransaction().replace(R.id.frame, MusicFragment()).commit()

    }

    override fun onPause() {
        viewModel.pause()
        super.onPause()
    }

    override fun onResume() {
        viewModel.currentSong.observe(this, Observer{
            viewModel.startMusic(it)
            viewModel.playMusic()
        })
        super.onResume()
    }

//    override fun onBackPressed() {
//        if (supportFragmentManager.findFragmentById(R.id.frame) is MusicListFragment) {
//            vm.stopMusic()
//            supportFragmentManager.beginTransaction().replace(R.id.frame, MusicFragment()).commit()
//        } else {
//            super.onBackPressed()
//        }
//    }
}