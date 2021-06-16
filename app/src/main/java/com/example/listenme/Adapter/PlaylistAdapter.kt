package com.example.listenme.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listenme.Data.MyMusic
import com.example.listenme.R
import com.example.listenme.UI.MusicListFragment
import com.example.listenme.Viewmodel.MusicViewmodel

class PlaylistAdapter(
    private val frag: MusicListFragment,
    private val musicList: ArrayList<MyMusic>,
    private val viewModel: MusicViewmodel
) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.playlist_layout, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        Glide.with(frag.requireContext()).load(musicList[position].image).into(holder.image)
        holder.playlistName.text = musicList[position].name

        holder.itemView.setOnClickListener {
            viewModel.adapterSong(musicList[position], position)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.playlist_image)
        val playlistName: TextView = itemView.findViewById(R.id.title)
        val duration: TextView = itemView.findViewById(R.id.duration)
    }
}