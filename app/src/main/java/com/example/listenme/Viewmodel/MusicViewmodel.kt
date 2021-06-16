package com.example.listenme.Viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listenme.Data.MyMusic
import java.util.concurrent.TimeUnit

class MusicViewmodel(application: Application) : AndroidViewModel(application) {

    var mediaPlayer: MediaPlayer = MediaPlayer()

    private val _playMusic = MutableLiveData<Boolean>()
    val playMusic: LiveData<Boolean>
        get() = _playMusic

    private val _changeIndex = MutableLiveData<Int>()
    val changeIndex: LiveData<Int>
        get() = _changeIndex

    private val _previousIndex = MutableLiveData<Int>()
    val previousIndex: LiveData<Int>
        get() = _previousIndex

    private val _currentIndex = MutableLiveData<Int>()
    val currentIndex: LiveData<Int>
        get() = _currentIndex

    private val _currentSong = MutableLiveData<MyMusic>()
    val currentSong: LiveData<MyMusic>
        get() = _currentSong

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int>
        get() = _duration

    private val _timing = MutableLiveData<Long>()
    val timing: LiveData<Long>
        get() = _timing


    fun startMusic(index: MyMusic) {
        if (mediaPlayer.isPlaying) {
            Log.d("Karan ", "Null")
            mediaPlayer.start()
        } else {
            Log.d("Karan ", "null")
            mediaPlayer =
                MediaPlayer.create(getApplication<Application>().applicationContext, index.music)
        }
    }

    fun playSong(song: MyMusic) {
        if (mediaPlayer != MediaPlayer.create(getApplication(), song.music)) {
            mediaPlayer.stop()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(getApplication(), song.music)
            _duration.value = mediaPlayer.duration
            mediaPlayer.start()
        }
        _playMusic.value = true
    }


    fun playMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _playMusic.postValue(true)
        } else {
            mediaPlayer.start()
            _playMusic.postValue(false)
        }
    }

    fun adapterSong(myMusic: MyMusic, position: Int) {
        _currentSong.postValue(myMusic)
        _changeIndex.postValue(position)
//        playMusic()
        playSong(myMusic)
    }

    fun nextSong(list: ArrayList<MyMusic>, currentIndex: Int) {
        var index: Int = 0
        index = if (currentIndex < list.size - 1) {
            currentIndex + 1
        } else {
            0
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _playMusic.postValue(true)
        }
        mediaPlayer =
            MediaPlayer.create(getApplication<Application>().applicationContext, list[index].music)
        mediaPlayer.start()
        _playMusic.postValue(false)
        _changeIndex.postValue(index)
        _currentIndex.postValue(index)
        _currentSong.postValue(list[index])
    }

    fun previousSong(list: ArrayList<MyMusic>, currentIndex: Int) {
        var index = 0
        if (currentIndex > 0) {
            index = currentIndex - 1;
        } else {
            index = list.size - 1
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _playMusic.postValue(true)
        }
        mediaPlayer =
            MediaPlayer.create(getApplication<Application>().applicationContext, list[index].music)
        mediaPlayer.start()
        _playMusic.postValue(false)
        _currentIndex.postValue(index)
        _previousIndex.postValue(index)
        _currentSong.postValue(list[index])
    }

    fun setStart(myMusic: ArrayList<MyMusic>) {
        if (_currentSong.value == null) {
            _currentSong.value = myMusic[0]
            _currentIndex.value = 0
            mediaPlayer = MediaPlayer.create(getApplication(), myMusic[0].music)
            _duration.value = mediaPlayer.duration
            playMusic()
            _playMusic.value = false
        }
    }


//    fun changeTime() : String {
//        _timing.postValue(duration.value!!.toLong())
//        return String.format(
//            "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(_timing.value!!),
//            TimeUnit.MILLISECONDS.toSeconds(_timing.value!!) - TimeUnit.MINUTES.toSeconds(
//                (TimeUnit.MILLISECONDS.toMinutes(
//                    _timing.value!!
//                ))
//            )
//        )
//    }


    fun changeSeek(position: Int) {
        mediaPlayer.seekTo(position)
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun stopMusic() {
        mediaPlayer.stop()
    }


}