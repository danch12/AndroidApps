package com.example.spotifyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class SongListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment(){
        Fragment fragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(MainActivity.EXTRA_TOKEN,getIntent().getStringExtra(MainActivity.EXTRA_TOKEN));
        bundle.putCharSequence(MainActivity.GENRE_TOKEN,getIntent().getStringExtra(MainActivity.GENRE_TOKEN));
        fragment.setArguments(bundle);
        return fragment;
    }
}
