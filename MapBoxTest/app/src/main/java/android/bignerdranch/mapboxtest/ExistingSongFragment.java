package android.bignerdranch.mapboxtest;

import android.content.Intent;
import android.graphics.Color;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Walk;
import com.mapbox.geojson.Point;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;


public class ExistingSongFragment extends Fragment {

    private RecyclerView mSongRecyclerView;
    private ExistingSongFragment.SongAdapter mSongAdapter;
    private Integer currentNumSongs;
    private List<Song> currentSongList;
    private static final String CLIENT_ID = "6a0a2cda4c70430794d5aa1f29d0a060";
    private static final String REDIRECT_URI = "https://www.google.com/";
    private String bearerToken;
    private SpotifyAppRemote mSpotifyAppRemote;
    protected static SpotifyApi kaesApi;
    private Point journeyDestination;
    private Point wayPoint;
    private Point journeyOrigin;
    private String walkID;

    private static final String TAG = "ExistingSongFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_song_list,container,false);
        mSongRecyclerView = (RecyclerView) view.findViewById(R.id.song_recycler_view);
        mSongRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //get bearer token here
        walkID = getArguments().getString(ExistingPlaylistTabActivity.EXTRA_ID);
        findWayPoint();
        bearerToken = getArguments().getString(GenreScreenActivity.EXTRA_TOKEN);
        String genre = getArguments().getString(GenreScreenActivity.GENRE_TOKEN);
        double journeyTime = getArguments().getDouble(GenreScreenActivity.EXTRA_JOURNEY);
        journeyDestination = (Point)getArguments().getSerializable(GenreScreenActivity.EXTRA_DESTINATION);
        journeyOrigin =(Point)getArguments().getSerializable(GenreScreenActivity.EXTRA_ORIGIN);
        kaesApi = new SpotifyApi();
        kaesApi.setAccessToken(bearerToken);
        connectToSpotify();
        updateUI();
        return view;
    }


    public void findWayPoint(){
        Amplify.DataStore.query(Walk.class, Where.matches(Walk.ID.eq(walkID)), response->{
                    if(response.hasNext()){
                        Walk walk =response.next();
                        wayPoint = Point.fromLngLat(walk.getStartLon(),walk.getStartLat());

                    }
                }, error -> Log.e("MyAmplifyApp", "Query failure", error)
        );
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


    public List<Song> createSongList() {
        ExistingSongCatalogue freshSongCatalogue =  new ExistingSongCatalogue(kaesApi, walkID,this);
        return freshSongCatalogue.getPlaylist();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_song_list, menu);
    }

    private void updateUI(){
        currentSongList=createSongList();
        mSongAdapter = new ExistingSongFragment.SongAdapter(currentSongList);
        mSongRecyclerView.setAdapter(mSongAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.confirm_songs) {
            Intent intent = ExistingPlaylistJourney.createIntent(getActivity(),journeyDestination,
                    journeyOrigin,bearerToken,wayPoint,walkID);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void playSongs(){
        if(mSpotifyAppRemote==null){
            Log.d(TAG, "playSongs: isnull");
        }else{
            Amplify.DataStore.query(Walk.class, Where.matches(Walk.ID.eq(walkID)), response->{
                        if(response.hasNext()){
                            Walk walk =response.next();
                            String playlistId = walk.getPlaylistId();
                            mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" +playlistId);
                        }else{
                            Log.d(TAG, "playSongs: " + "none found" );
                        }
                    }, error -> Log.e("MyAmplifyApp", "Query failure", error)

            );
        }

    }



    public void refreshSongs(int numSongs){
        if(currentNumSongs!=null){
            mSongAdapter.notifyItemRangeRemoved(0, currentNumSongs);
        }
        currentNumSongs= numSongs;

        mSongAdapter.notifyItemRangeInserted(0,numSongs);
    }


    private class SongHolder extends RecyclerView.ViewHolder {
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




    }


    private class SongAdapter extends RecyclerView.Adapter<ExistingSongFragment.SongHolder>{
        private List<Song> mSongs;
        public SongAdapter(List<Song> songs){
            mSongs = songs;
        }

        @NonNull
        @Override
        public ExistingSongFragment.SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ExistingSongFragment.SongHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ExistingSongFragment.SongHolder holder, int position) {
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
}

