package com.example.spotifyapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.SeedsGenres;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;

public class SongCatalogue {
    private List<Song> songs;
    private static SongCatalogue sSongCatalogue;
    public static final String TAG = "SongCatalogue";
    private static SpotifyApi api;
    private SongListFragment songList;




    public SongCatalogue(Context context, String accessToken,SongListFragment songList){
        if(api==null){
            api = new SpotifyApi();
            api.setAccessToken(accessToken);


        }
        this.songList=songList;
    }

    private void formatSongs(Recommendations recommendations){

        for(Track track:recommendations.tracks){
            songs.add(new Song(track.artists.get(0).name,track.name,track.id));
        }
    }

    public List<Song> getSongs(String genre){

        songs=new ArrayList<>();
        SpotifyService spotify = api.getService();
        Map<String,Object> genreMap = new HashMap<String, Object>();
        genreMap.put("seed_genres",genre);
        spotify.getRecommendations(genreMap, new SpotifyCallback<Recommendations>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.d(TAG, "failure: " +spotifyError);

            }

            @Override
            public void success(Recommendations recommendations, Response response) {
                Log.d(TAG, "success: "+recommendations.tracks.get(0).name);
                formatSongs(recommendations);
                songList.refreshSongs(songs.size());

            }
        });


    return songs;
    }

}
