package android.bignerdranch.mapboxtest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.geojson.Point;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class SongListFragment extends Fragment {

    private static final String TAG = "SongListFragment";
    protected RecyclerView mSongRecyclerView;
    protected SongListFragment.SongAdapter mSongAdapter;
    protected Integer currentNumSongs;
    protected List<Song> currentSongList;
    private static final String CLIENT_ID = "6a0a2cda4c70430794d5aa1f29d0a060";
    private static final String REDIRECT_URI = "https://www.google.com/";
    protected String userId;
    protected String bearerToken;
    protected SpotifyAppRemote mSpotifyAppRemote;
    protected static SpotifyApi kaesApi;
    protected Point journeyDestination;
    protected Point journeyOrigin;

    private class SongHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        private TextView mTitleTextView;
        private TextView mArtistTextView;
        private Song mSong;
        public SongHolder(LayoutInflater inflater, ViewGroup parent){
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
    private class SongAdapter extends RecyclerView.Adapter<SongListFragment.SongHolder>{
        private List<Song> mSongs;
        public SongAdapter(List<Song> songs){
            mSongs = songs;
        }

        @NonNull
        @Override
        public SongListFragment.SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SongListFragment.SongHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SongListFragment.SongHolder holder, int position) {
            Song song = mSongs.get(position);
            if(position%2==0){
                holder.itemView.setBackgroundColor(Color.rgb(112,128,144));
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_song_list,container,false);
        mSongRecyclerView = (RecyclerView) view.findViewById(R.id.song_recycler_view);
        mSongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //get bearer token here
        bearerToken = getArguments().getString(GenreScreenActivity.EXTRA_TOKEN);
        String genre = getArguments().getString(GenreScreenActivity.GENRE_TOKEN);
        double journeyTime = getArguments().getDouble(GenreScreenActivity.EXTRA_JOURNEY);
        journeyDestination = (Point)getArguments().getSerializable(GenreScreenActivity.EXTRA_DESTINATION);
        journeyOrigin =(Point)getArguments().getSerializable(GenreScreenActivity.EXTRA_ORIGIN);
        kaesApi = new SpotifyApi();
        kaesApi.setAccessToken(bearerToken);
        kaesApi.getService().getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                userId=userPrivate.id;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: "+error);
            }
        });
        updateUI(genre,journeyTime);
        connectToSpotify();
        return view;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_song_list, menu);
    }


    protected abstract List<Song> createSongList(String genre,double journeyTime);

    private void updateUI(String genre,double journeyTime){
        currentSongList=createSongList(genre,journeyTime);
        mSongAdapter = new SongListFragment.SongAdapter(currentSongList);
        mSongRecyclerView.setAdapter(mSongAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.confirm_songs) {
            playSongs();
            Intent intent = JourneyActivity.createIntent(getActivity(),journeyDestination,
                    journeyOrigin,bearerToken);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void playSongs(){
        if(mSpotifyAppRemote==null){
            Log.d(TAG, "playSongs: isnull");
        }else{
            createPlaylist();
        }

    }

    public void createPlaylist(){
        SpotifyService apiService = kaesApi.getService();
        Map<String,Object> playlistParams = new HashMap<>();
        playlistParams.put("name","testPlaylist");
        apiService.createPlaylist(userId,playlistParams,new Callback<Playlist>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Playlist playlist, Response response) {
                String tracksString= currentSongList.stream()
                        .map(track -> "spotify:track:"+track.getId())
                        .collect(Collectors.joining(","));
                Map<String,Object> tracksMap = new HashMap<>();
                tracksMap.put("uris",tracksString);
                apiService.addTracksToPlaylist(userId, playlist.id, tracksMap, new HashMap<>(), new Callback<Pager<PlaylistTrack>>() {
                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        Log.d(TAG, "success: "+playlist.id);
                        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:"+playlist.id);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "failure: "+error);
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: "+error);

            }
        });
    }

    /*code to connect to spotify found here https://developer.spotify.com/documentation/android/quick-start/*/
    private void connectToSpotify(){
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(getActivity(),connectionParams,new Connector.ConnectionListener(){
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected to main Spotify api");
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });

    }


    public void refreshSongs(int numSongs){
        if(currentNumSongs!=null){
            mSongAdapter.notifyItemRangeRemoved(0, currentNumSongs);
        }
        currentNumSongs= numSongs;

        mSongAdapter.notifyItemRangeInserted(0,numSongs);
    }
}
