package com.example.spotifyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SongListFragment extends Fragment {
    private static final String ARG_BEARER_TOKEN  = "bearerToken";
    private RecyclerView mSongRecyclerView;
    private SongAdapter mSongAdapter;

    private class SongHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        private TextView mTitleTextView;
        private TextView mArtistTextView;
        private Song mSong;
        public SongHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_song,parent,false));
            //item view is created by parent class when you implement onclicklistener
            // itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.song_title);
            mArtistTextView = (TextView) itemView.findViewById(R.id.song_artist);
        }

        public void bind(Song song){
            mSong = song;
            mTitleTextView.setText(mSong.getSongName());
            mArtistTextView.setText(mSong.getSongArtist());
        }

        /*@Override
        public void onClick(View view){
            Intent intent = MainActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
        }*/
    }
    private class SongAdapter extends RecyclerView.Adapter<SongHolder>{
        private List<Song> mSongs;
        public SongAdapter(List<Song> songs){
            mSongs = songs;
        }

        @NonNull
        @Override
        public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SongHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SongHolder holder, int position) {
            Song song = mSongs.get(position);
            if(position%2==0){
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            holder.bind(song);
        }

        @Override
        public int getItemCount() {
            return mSongs.size();
        }
    }

    public static SongListFragment newInstance(String bearerToken){
        Bundle args = new Bundle();
        args.putSerializable(ARG_BEARER_TOKEN,bearerToken);
        SongListFragment fragment = new SongListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_song_list,container,false);
        mSongRecyclerView = (RecyclerView) view.findViewById(R.id.song_recycler_view);
        mSongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //get bearer token here
        String bearerToken=null;

        bearerToken = getArguments().getString(MainActivity.EXTRA_TOKEN);
        String genre = getArguments().getString(MainActivity.GENRE_TOKEN);

        updateUI(bearerToken,genre);
        return view;
    }

    private void updateUI(String bearerToken,String genre){
        SongCatalogue songCatalogue=  new SongCatalogue(getActivity(),bearerToken,this);
        List<Song> songs=songCatalogue.getSongs(genre);
        mSongAdapter = new SongAdapter(songs);
        mSongRecyclerView.setAdapter(mSongAdapter);

    }


    public void refreshSongs(int numSongs){
        mSongAdapter.notifyItemRangeInserted(0,numSongs);
    }


}

