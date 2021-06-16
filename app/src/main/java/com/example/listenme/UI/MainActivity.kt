package com.example.listenme.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listenme.R
import com.example.listenme.Viewmodel.MusicViewmodel

class MainActivity : AppCompatActivity() {

    lateinit var vm: MusicViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.frame, MusicFragment()).commit()

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