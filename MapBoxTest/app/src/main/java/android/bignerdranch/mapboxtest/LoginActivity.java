package android.bignerdranch.mapboxtest;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Walk;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;

public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "6a0a2cda4c70430794d5aa1f29d0a060";
    private static final String REDIRECT_URI = "https://www.google.com/";
    private static final int REQUEST_CODE = 1337;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("Login", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Login", "Could not initialize Amplify", e);
        }
       /* Amplify.DataStore.clear(()-> Log.i("amplify","successfully cleared"),
                error-> Log.i("amplify","didnt clear- " +error)
                                    );*/
        Walk nwalk = Walk.builder().title("another walk")
                .startLat(51.462711).startLon(-2.590040)
                .endLon(-2.608024).endLat(51.469175)
                .playlistId("5tALyid8xag8f5bt2yr2iP").duration(10).creator("1143043561").build();

        /*Amplify.DataStore.save(nwalk,
                success -> Log.i("Tutorial", "Saved item: " + success.item().getTitle()),
                error -> Log.e("Tutorial", "Could not save item to DataStore", error)
        );*/
        Comment ncomment = Comment.builder().text("hello").walkId("1").build();
       /* Amplify.DataStore.save(ncomment,
                success -> Log.i("Tutorial", "Saved item: " + success.item().getText()),
                error -> Log.e("Tutorial", "Could not save item to DataStore", error)
        );
        Comment againcomment = Comment.builder().text("goodbye").walkId("1").build();
        Amplify.DataStore.save(againcomment,
                success -> Log.i("Tutorial", "Saved item: " + success.item().getText()),
                error -> Log.e("Tutorial", "Could not save item to DataStore", error)
        );*/

        Amplify.DataStore.observe(Walk.class,
                started -> Log.i("Tutorial", "Observation began."),
                change -> Log.i("Tutorial", change.item().toString()),
                failure -> Log.e("Tutorial", "Observation failed.", failure),
                () -> Log.i("Tutorial", "Observation complete.")
        );
    }

    public void onLoginButtonClicked(View view) {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"playlist-read","user-library-read","playlist-modify-public"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }




    //gets bearer token for kaaesApi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("LoginActivity","Got token: " + response.getAccessToken());
                    CredentialsHandler.setToken(this, response.getAccessToken(), response.getExpiresIn(), TimeUnit.SECONDS);
                    startMainActivity(response.getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.d("LoginActivity","Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d("LoginActivity","Auth result: " + response.getType());
            }
        }
    }

    private void startMainActivity(String token) {
        Intent intent = MainActivity.createIntent(this,token);
        startActivity(intent);
        finish();
    }
}
