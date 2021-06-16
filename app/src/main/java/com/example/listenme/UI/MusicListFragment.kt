package com.example.listenme.UI

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listenme.Adapter.PlaylistAdapter
import com.example.listenme.Data.MyMusic
import com.example.listenme.R
import com.example.listenme.Viewmodel.MusicViewmodel

class MusicListFragment(
    private val musicList: ArrayList<MyMusic>
) : Fragment() {

    lateinit var playlistRecyclerView: RecyclerView
    lateinit var search: SearchView
    lateinit var back: ImageView
    lateinit var bottomImage: ImageView
    lateinit var bottomName: TextView
    lateinit var bottom_play: ImageView
    lateinit var bottomRl: RelativeLayout

    val viewmodel: MusicViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setRecyclerView()
        setResources()


        bottomRl.setOnClickListener {
            viewmodel.pause()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, MusicFragment()).commit()
        }

        bottom_play.setOnClickListener {
            viewmodel.playMusic()
            viewmodel.playMusic.observe(viewLifecycleOwner, {
                if (it == false) {
                    bottom_play.setImageResource(R.drawable.pause)
                } else {
                    bottom_play.setImageResource(R.drawable.play)
                }
            })
        }

    }

    private fun setResources() {


        viewmodel.playMusic.observe(viewLifecycleOwner, {
            Log.d("songPlay", it.toString())
            if (it == false) {
                bottom_play.setImageResource(R.drawable.pause)
            } else {
                bottom_play.setImageResource(R.drawable.play)
            }
        })

//        viewmodel.continueSong()
//
//        bottomName.text = musicList[0].name
//        Glide.with(this.requireContext()).load(musicList[0].image).into(bottomImage)

        viewmodel.currentSong.observe(viewLifecycleOwner, {
            Log.d("Song", it.toString())
            if (it != null) {
                viewmodel.playMusic()
                bottomName.text = it.name
                Glide.with(this.requireContext()).load(it.image).into(bottomImage)
            }
        })
    }

    private fun setRecyclerView() {
        val adapter = PlaylistAdapter(this, musicList, viewmodel)
        playlistRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        playlistRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun initView(view: View) {
        playlistRecyclerView = view.findViewById(R.id.music_recyclerView)
        back = view.findViewById(R.id.back_music)
        bottomImage = view.findViewById(R.id.bottom_image)
        bottomName = view.findViewById(R.id.bottom_name)
        bottom_play = view.findViewById(R.id.bottom_play)
        bottomRl = view.findViewById(R.id.bottom_rl)
    }

//    override fun onPause() {
//        viewmodel.stopMusic()
//        super.onPause()
//    }
//
//    override fun onResume() {
//        viewmodel.currentSong.observe(viewLifecycleOwner, {
//            viewmodel.startMusic(it)
//            viewmodel.playMusic()
//        })
//        super.onResume()
//    }
//
//    override fun onStop() {
//        viewmodel.stopMusic()
//        super.onStop()
//    }
}
