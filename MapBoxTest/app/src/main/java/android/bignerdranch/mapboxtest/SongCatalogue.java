package android.bignerdranch.mapboxtest;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;

public class SongCatalogue {
    private final List<Song> songs;
    public static final String TAG = "SongCatalogue";
    private final SpotifyApi api;
    private final SongListFragment songList;
    private final double journeyTime;
    private final String genre;
    double totalDuration;

    public SongCatalogue(SpotifyApi api, double journeyTime, String genre, SongListFragment songList){
        totalDuration=0;
        this.api = api;
        this.journeyTime=journeyTime;
        this.songList=songList;
        songs=new ArrayList<>();
        this.genre = genre;
    }

    private void formatSongs(Recommendations recommendations){

        for(Track track:recommendations.tracks){
            Song song = new Song(track,track.artists.get(0).name,track.name,track.id,track.duration_ms/1000.0);
            songs.add(song);
            totalDuration+=song.getDuration();
            if(journeyTime<totalDuration){
                return;
            }
        }
        if(journeyTime>totalDuration){
            getSongs();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                getDistinct();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDistinct(){
        HashSet<String> seen=new HashSet<>();
        songs.removeIf(song->!seen.add(song.getId()));

    }

    public List<Song> getSongs(){
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
                formatSongs(recommendations);
                songList.refreshSongs(songs.size());

            }
        });


        return songs;
    }

}

