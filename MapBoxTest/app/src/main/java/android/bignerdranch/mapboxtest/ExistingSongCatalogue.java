package android.bignerdranch.mapboxtest;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Walk;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.client.Response;

public class ExistingSongCatalogue {
    private SpotifyApi api;
    private String walkId;
    private ExistingSongFragment songFragment;
    private List<Song> songs;
    public static String TAG ="ExistingSongCatalogue";

    public ExistingSongCatalogue(SpotifyApi api,String walkId,ExistingSongFragment songFragment){
        this.api=api;
        this.walkId = walkId;
        this.songFragment = songFragment;
        songs = new ArrayList<>();
    }


    public List<Song> getPlaylist(){
        Amplify.DataStore.query(Walk.class, Where.matches(Walk.ID.eq(walkId)), response->{
            if(response.hasNext()){
                Walk walk =response.next();
                getPlaylistSongs(walk.getPlaylistId(),walk.getCreator());
            }
        }, error -> Log.e("MyAmplifyApp", "Query failure", error)

        );
        return songs;
    }

    public void getPlaylistSongs(String playlistId,String userId){
        SpotifyService spotify = api.getService();
        spotify.getPlaylist(userId,playlistId, new SpotifyCallback<Playlist>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Playlist playlist, Response response) {

                songs.addAll(playlist.tracks.items.stream()
                        .map(playlistTrack -> convertTrackToSong(playlistTrack))
                        .collect(Collectors.toList()));
                songFragment.refreshSongs(songs.size());
            }

            @Override
            public void failure(SpotifyError spotifyError) {
                Log.e(TAG, "failure: ",spotifyError );
            }
        });
    }

    public Song convertTrackToSong(PlaylistTrack track){
        return new Song(track.track,track.track.artists.get(0).name,
                track.track.name,track.track.id,track.track.duration_ms/1000.0);
    }
}
