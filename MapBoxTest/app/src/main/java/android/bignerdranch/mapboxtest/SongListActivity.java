package android.bignerdranch.mapboxtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.mapbox.geojson.Point;

public class SongListActivity extends SingleFragmentActivity{
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    static final String EXTRA_JOURNEY = "EXTRA_JOURNEY";
    static final String GENRE_TOKEN ="Genre";
    static final String EXTRA_DESTINATION ="DESTINATION";
    static final String EXTRA_ORIGIN ="ORIGIN";

    public static Intent createIntent(Context context, String accessToken, double journeyDuration,
                                      String genre, Point destination,Point origin){
        Intent intent =  new Intent(context, SongListActivity.class);
        intent.putExtra(SongListActivity.EXTRA_TOKEN, accessToken);
        intent.putExtra(SongListActivity.GENRE_TOKEN, genre);
        intent.putExtra(SongListActivity.EXTRA_JOURNEY, journeyDuration);
        intent.putExtra(SongListActivity.EXTRA_DESTINATION,destination);
        intent.putExtra(SongListActivity.EXTRA_ORIGIN,origin);
        return intent;
    }


    @Override
    protected Fragment createFragment(){
        Fragment fragment = new ExistingSongFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(SongListActivity.EXTRA_TOKEN,getIntent().getStringExtra(SongListActivity.EXTRA_TOKEN));
        bundle.putCharSequence(SongListActivity.GENRE_TOKEN,getIntent().getStringExtra(SongListActivity.GENRE_TOKEN));
        bundle.putSerializable(SongListActivity.EXTRA_DESTINATION,getIntent().getSerializableExtra(SongListActivity.EXTRA_DESTINATION));
        bundle.putSerializable(SongListActivity.EXTRA_ORIGIN,getIntent().getSerializableExtra(SongListActivity.EXTRA_ORIGIN));
        bundle.putDouble(SongListActivity.EXTRA_JOURNEY,getIntent().getDoubleExtra(SongListActivity.EXTRA_JOURNEY,0));
        fragment.setArguments(bundle);
        return fragment;
    }
}
