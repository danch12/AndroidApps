package android.bignerdranch.mapboxtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mapbox.geojson.Point;

public class ExistingPlaylistActivity extends AppCompatActivity {
    static final String EXTRA_ID = "EXTRA_ID";

    public static Intent createIntent(Context context, String accessToken, double journeyDuration,
                                      String id, Point destination, Point origin){
        Intent intent =  new Intent(context, SongListActivity.class);
        intent.putExtra(SongListActivity.EXTRA_TOKEN, accessToken);
        intent.putExtra(ExistingPlaylistActivity.EXTRA_ID, id);
        intent.putExtra(SongListActivity.EXTRA_JOURNEY, journeyDuration);
        intent.putExtra(SongListActivity.EXTRA_DESTINATION,destination);
        intent.putExtra(SongListActivity.EXTRA_ORIGIN,origin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_playlist);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment_song = fm.findFragmentById(R.id.song_recycler_view);
        if(fragment_song==null){
            fragment_song = createSongFragment();
            fm.beginTransaction().add(R.id.song_recycler_view,fragment_song).commit();
        }
    }


    private Fragment createSongFragment(){
        Fragment fragment = new ExistingSongFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(SongListActivity.EXTRA_TOKEN,getIntent().getStringExtra(SongListActivity.EXTRA_TOKEN));
        bundle.putCharSequence(SongListActivity.GENRE_TOKEN,"rock");
        bundle.putSerializable(SongListActivity.EXTRA_DESTINATION,getIntent().getSerializableExtra(SongListActivity.EXTRA_DESTINATION));
        bundle.putSerializable(SongListActivity.EXTRA_ORIGIN,getIntent().getSerializableExtra(SongListActivity.EXTRA_ORIGIN));
        bundle.putDouble(SongListActivity.EXTRA_JOURNEY,getIntent().getDoubleExtra(SongListActivity.EXTRA_JOURNEY,0));
        fragment.setArguments(bundle);
        return fragment;
    }





}