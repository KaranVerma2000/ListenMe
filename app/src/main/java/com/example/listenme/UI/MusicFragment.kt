package com.example.listenme.UI

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.listenme.Data.MyMusic
import com.example.listenme.R
import com.example.listenme.Viewmodel.MusicViewmodel

class MusicFragment() : Fragment() {

    lateinit var rl: RelativeLayout
    lateinit var playlistOpen: LinearLayout
    lateinit var image: ImageView
    lateinit var rlImage: ImageView
    lateinit var name: TextView
    lateinit var startDuration: TextView
    lateinit var lastDuration: TextView
    lateinit var play: ImageView
    lateinit var pause: ImageView
    lateinit var forward: ImageView
    lateinit var backward: ImageView
    lateinit var playlist: LinearLayout
    lateinit var back_frag: ImageView
    lateinit var seekBar: SeekBar
    private var musicList: ArrayList<MyMusic> = arrayListOf()
    var currentIndex: Int = 0

    private lateinit var handler: Handler

    val musicViewmodel: MusicViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setList()

        handler = Handler()
        musicViewmodel.setStart(musicList)
        musicViewmodel.currentSong.observe(viewLifecycleOwner, Observer {
            musicViewmodel.startMusic(it)
            name.text = it.name
            Log.d("name", it.name)
            Glide.with(this.requireContext()).load(it.image).into(image)
            Glide.with(this.requireContext()).load(it.image).into(rlImage)
            updateSeekBar()
        })

        musicViewmodel.duration.observe(viewLifecycleOwner, Observer {
            seekBar.max = it
            lastDuration.text = getTime(it.toLong())
        })

        changeResources(currentIndex)



        play.setOnClickListener {
            playMusic()
        }

        forward.setOnClickListener {
            nextMusic()
        }

        backward.setOnClickListener {
            previousMusic()
        }

//        val time = musicViewmodel.changeTime()
//        startDuration.text = time

        playlistOpen.setOnClickListener {

//            var unit: MyMusic? = null
//            musicViewmodel.currentSong.observe(viewLifecycleOwner, {
//                unit = it
//            })

            val ft = requireActivity().supportFragmentManager.beginTransaction()
//            ft.setCustomAnimations(
//                R.anim.fade_in,
//                R.anim.slide_up,
//                R.anim.fade_in,
//                R.anim.slide_down
//            )
            musicViewmodel.pause()
            ft.addToBackStack(null)
                .replace(R.id.frame, MusicListFragment(musicList)).commit()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicViewmodel.mediaPlayer.seekTo(progress)
                    seekBar?.progress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


    }

    private fun getTime(millis: Long): String? {
        val buf = StringBuffer()
        val minutes = (millis % (1000 * 60 * 60) / (1000 * 60)).toInt()
        val seconds = (millis % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        buf.append(String.format("%02d", minutes))
            .append(":")
            .append(String.format("%02d", seconds))
        return buf.toString()
    }

    private fun updateSeekBar() {
        val currentPosition = musicViewmodel.mediaPlayer.currentPosition
        seekBar.progress = currentPosition
        val runnable = Runnable {
            updateSeekBar()
            startDuration.text = getTime(musicViewmodel.mediaPlayer.currentPosition.toLong())
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun previousMusic() {
        musicViewmodel.previousSong(musicList, currentIndex)
        musicViewmodel.previousIndex.observe(viewLifecycleOwner, {
            currentIndex = it
        })
        changeResources(currentIndex)
    }

    private fun nextMusic() {
        musicViewmodel.nextSong(musicList, currentIndex)
        musicViewmodel.changeIndex.observe(viewLifecycleOwner, {
            currentIndex = it
            Log.d("currentSong", it.toString())
        })
        changeResources(currentIndex)
    }

    private fun playMusic() {
        musicViewmodel.playMusic()
        changeResources(currentIndex)
    }

    private fun changeResources(currentIndex: Int) {

        musicViewmodel.playMusic.observe(viewLifecycleOwner, {
            if (it == false) {
                play.setImageResource(R.drawable.ic_pause)
            } else {
                play.setImageResource(R.drawable.ic_play)
            }
        })

//        musicViewmodel.currentSong.observe(viewLifecycleOwner, {
//            if (it != null) {
//                Log.d("MusicSong", it.toString())
//                name.text = it.name
//                Glide.with(this.requireContext()).load(it.image).into(image)
//                Glide.with(this.requireContext()).load(it.image).into(rlImage)
//            }
//        })
    }

    private fun setList() {
        musicList.clear()
        musicList.add(
            MyMusic(
                "Believer (Pop Rock) - Imagine Dragons",
                R.drawable.beliver,
                R.raw.believer
            )
        )
        musicList.add(
            MyMusic(
                "Brown Munde (Hip-Hop) - AP Dhillon",
                R.drawable.brown,
                R.raw.brown_munde
            )
        )
        musicList.add(MyMusic("Casanova - King", R.drawable.casanova, R.raw.casanova))
        musicList.add(
            MyMusic(
                "Hall of Fame (Pop Rock) - The Script",
                R.drawable.hall,
                R.raw.hall_of_fame
            )
        )
        musicList.add(MyMusic("Lut Gye - Jubin Nautiyal", R.drawable.lutgye, R.raw.lut_gye))
        musicList.add(
            MyMusic(
                "Pani - Pani (Buzz)- Astha Gill",
                R.drawable.panipani,
                R.raw.pani_pani
            )
        )
        musicList.add(MyMusic("Shor Machega - Honey Singh", R.drawable.shor, R.raw.shor_machega))
        musicList.add(MyMusic("Tu Aake Dekhle - King ", R.drawable.tuaake, R.raw.tu_aake_dekh))
        musicList.add(
            MyMusic(
                "Intentions - Justin Bieber",
                R.drawable.intentions,
                R.raw.intentions
            )
        )
        musicList.add(MyMusic("Peaches - Justin Bieber", R.drawable.peaches, R.raw.peaches))
        musicList.add(
            MyMusic(
                "Let Me Love You - Justin Bieber",
                R.drawable.letmelove,
                R.raw.letmeloveyou
            )
        )
        musicList.add(MyMusic("Tum Dil Ki Dhadkan", R.drawable.dhadhkan, R.raw.dhadkan))
        musicList.add(MyMusic("Kale Je Libaas - KAKA", R.drawable.libaas, R.raw.libaas))
    }

    private fun initView(view: View) {

        rl = view.findViewById(R.id.rl1)
        playlistOpen = view.findViewById(R.id.playlist_open)
        rlImage = view.findViewById(R.id.rlImage)
        image = view.findViewById(R.id.music_image)
        name = view.findViewById(R.id.music_name)
        play = view.findViewById(R.id.play)
        forward = view.findViewById(R.id.forward)
        backward = view.findViewById(R.id.backward)
        seekBar = view.findViewById(R.id.seekbar)
        startDuration = view.findViewById(R.id.start_duration)
        lastDuration = view.findViewById(R.id.end_duration)

    }

}