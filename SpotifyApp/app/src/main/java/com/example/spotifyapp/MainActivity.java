package com.example.spotifyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.SeedsGenres;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    static final String GENRE_TOKEN ="Genre";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "6a0a2cda4c70430794d5aa1f29d0a060";
    private static final String REDIRECT_URI = "https://www.google.com/";
    private SpotifyAppRemote mSpotifyAppRemote;
    public static String[] genres = {"acoustic", "blues" , "deep-house","dance","hip-hop","pop","rock","sleep","techno"};
    private String selectedGenre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String token = intent.getStringExtra(EXTRA_TOKEN);

        Button goButton = (Button) findViewById(R.id.songButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,SongListActivity.class);

                intent.putExtra(MainActivity.EXTRA_TOKEN, token);
                intent.putExtra(MainActivity.GENRE_TOKEN,selectedGenre);
                startActivity(intent);
            }
        });

        Spinner dropdown = findViewById(R.id.spinner1);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                goButton.setEnabled(true);
                selectedGenre= genres[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                goButton.setEnabled(false);
            }

        });
        ArrayAdapter<String> dropDownAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,genres);
        dropdown.setAdapter(dropDownAdapter);

    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

}