package android.bignerdranch.mapboxtest;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;

public class GenreScreenActivity extends AppCompatActivity {
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    static final String EXTRA_JOURNEY = "EXTRA_JOURNEY";
    static final String GENRE_TOKEN ="Genre";
    static final String EXTRA_DESTINATION ="DESTINATION";
    static final String EXTRA_ORIGIN ="ORIGIN";

    public static String[] genres = {"acoustic", "blues" , "deep-house","dance","hip-hop","pop","rock","sleep","techno"};
    private String selectedGenre;

    public static Intent createIntent(Context context, String accessToken, double journeyDuration,
                                      Point destination,Point origin){
        Intent intent =  new Intent(context, GenreScreenActivity.class);
        intent.putExtra(GenreScreenActivity.EXTRA_TOKEN, accessToken);
        intent.putExtra(GenreScreenActivity.EXTRA_JOURNEY, journeyDuration);
        intent.putExtra(GenreScreenActivity.EXTRA_DESTINATION,destination);
        intent.putExtra(GenreScreenActivity.EXTRA_ORIGIN,origin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        Intent intent = getIntent();
        String token = intent.getStringExtra(EXTRA_TOKEN);
        double journeyLength = intent.getDoubleExtra(EXTRA_JOURNEY,0);
        Point destination = (Point) intent.getSerializableExtra(EXTRA_DESTINATION);
        Point origin = (Point) intent.getSerializableExtra(EXTRA_ORIGIN);
        Button goButton = (Button) findViewById(R.id.songButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =SongListActivity.createIntent(GenreScreenActivity.this,token,
                            journeyLength,selectedGenre,destination,origin);
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
}
