package android.bignerdranch.mapboxtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Walk;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.lang.ref.WeakReference;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class ExistingPlaylistJourney extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private Button playButton;
    private Point originPosition;
    private Point wayPoint;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private String userId;
    private String walkID;
    private String bearerToken;
    private LocationChangeListeningActivityLocationCallback callback =
            new LocationChangeListeningActivityLocationCallback(this);
    private MapboxDirections client;
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String TAG = "JourneyActivity";
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String CLIENT_ID = "6a0a2cda4c70430794d5aa1f29d0a060";
    private static final String REDIRECT_URI = "https://www.google.com/";
    private static final String EXTRA_WAYPOINT = "WAYPOINT TOKEN";
    private static final String EXTRA_WALK = "WALK TOKEN";
    private boolean currentlyPlaying;
    private boolean playlistActivated;

    public static Intent createIntent(Context context, Point destination, Point origin, String token,Point wayPoint,String walkId){
        Intent intent = new Intent(context, ExistingPlaylistJourney.class);
        intent.putExtra(GenreScreenActivity.EXTRA_DESTINATION,destination);
        intent.putExtra(GenreScreenActivity.EXTRA_ORIGIN,origin);
        intent.putExtra(GenreScreenActivity.EXTRA_TOKEN,token);
        intent.putExtra(ExistingPlaylistJourney.EXTRA_WAYPOINT,wayPoint);
        intent.putExtra(ExistingPlaylistJourney.EXTRA_WALK,walkId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_journey);
        playButton = (Button) findViewById(R.id.playButton);
        playButton.setEnabled(false);
        connectToSpotify();
        currentlyPlaying=true;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyPlaying){
                    mSpotifyAppRemote.getPlayerApi().pause();
                    currentlyPlaying=false;
                }else{
                    mSpotifyAppRemote.getPlayerApi().resume();
                    currentlyPlaying=true;
                }

            }
        });
        Intent intent = getIntent();
        walkID = intent.getStringExtra(ExistingPlaylistJourney.EXTRA_WALK);
        wayPoint = (Point) intent.getSerializableExtra(ExistingPlaylistJourney.EXTRA_WAYPOINT);
        destinationPosition = (Point)intent.getSerializableExtra(GenreScreenActivity.EXTRA_DESTINATION);
        originPosition =(Point)intent.getSerializableExtra(GenreScreenActivity.EXTRA_ORIGIN);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        playlistActivated=false;
        bearerToken = intent.getStringExtra(GenreScreenActivity.EXTRA_TOKEN);
        SpotifyApi kaesApi = new SpotifyApi();
        kaesApi.setAccessToken(bearerToken);
        kaesApi.getService().getMe(new retrofit.Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, retrofit.client.Response response) {
                userId=userPrivate.id;
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure: " + error);
            }
        });
    }





    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.TRAFFIC_NIGHT,
                new Style.OnStyleLoaded() {
                    @Override public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        initLayers(style);
                    }
                });


        client = MapboxDirections.builder()
                .origin(originPosition)
                .addWaypoint(wayPoint)
                .destination(destinationPosition)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.access_token))
                .build();
        getRoute();

    }

    private void connectToSpotify(){
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(ExistingPlaylistJourney.this,connectionParams,new Connector.ConnectionListener(){
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;

                playButton.setEnabled(true);
                Log.d("MainActivity", "Connected to main Spotify api");
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });

    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);
    }

    /**
     * determine whether user has given permissions+ if so we activate
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();
            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //if user rejects permissions access we give dialogue to convince them to give us it
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    //calls back to enableLocation to actually enable all the stuff
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }


    private void getRoute(){
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                if (response.body() == null) {
                    Log.e(TAG,"No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.e(TAG,"No routes found");
                    return;
                }

                // Retrieve the directions route from the API response
                currentRoute = response.body().routes().get(0);
                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Retrieve and update the source designated for showing the directions route
                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                        // Create a LineString with the directions route's geometry and
                        // reset the GeoJSON source for the route LineLayer source
                        if (source != null) {
                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                        }
                    }
                });
            }

            @Override public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }




    private static class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {
        private static final double THETA = 0.01;
        private final WeakReference<ExistingPlaylistJourney> activityWeakReference;

        LocationChangeListeningActivityLocationCallback(ExistingPlaylistJourney activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            ExistingPlaylistJourney activity = activityWeakReference.get();

            if (activity != null) {

                Location location = result.getLastLocation();
                if (location == null) {
                    return;
                }
                activity.originPosition = Point.fromLngLat(location.getLongitude(),location.getLatitude());
                // Create a Toast which displays the new location's coordinates
                Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
                        String.valueOf(result.getLastLocation().getLatitude()),
                        String.valueOf(result.getLastLocation().getLongitude())),
                        Toast.LENGTH_SHORT).show();

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {

                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }

                if (activity.mapboxMap != null && result.getLastLocation() != null){
                    if(destinationInArea(activity.destinationPosition,result)){
                        createPopup();
                    }
                }

                if (activity.mapboxMap != null && result.getLastLocation() != null){
                    if(destinationInArea(activity.wayPoint,result)){
                        playSongs();
                    }
                }
            }
        }

        public void playSongs(){

            ExistingPlaylistJourney activity = activityWeakReference.get();
            if(activity.playlistActivated){
                return;
            }
            if(activity.mSpotifyAppRemote==null){
                Log.d(TAG, "playSongs: isnull");
            }else{
                Amplify.DataStore.query(Walk.class, Where.matches(Walk.ID.eq(activity.walkID)), response->{
                            if(response.hasNext()){
                                Walk walk =response.next();
                                String playlistId = walk.getPlaylistId();
                                activity.mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" +playlistId);
                                activity.playlistActivated = true;
                            }else{
                                Log.d(TAG, "playSongs: " + "none found" );
                            }
                        }, error -> Log.e("MyAmplifyApp", "Query failure", error)

                );
            }

        }


        private void createPopup(){
            ExistingPlaylistJourney activity = activityWeakReference.get();
            LayoutInflater inflater = (LayoutInflater)
                    activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_screen_existing, null);
            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            // lets taps outside the popup also dismiss it
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            Button returnToMain = popupView.findViewById(R.id.popup_button_return);
            returnToMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = MainActivity.createIntent(activity,activity.bearerToken);
                    activity.mSpotifyAppRemote.getPlayerApi().pause();
                    activity.startActivity(intent);
                    activity.finish();
                }
            });

            View view = activity.findViewById(R.id.mapView);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }


        private boolean destinationInArea(Point destinationPosition,LocationEngineResult result) {
            if(Math.abs(destinationPosition.latitude()- result.getLastLocation().getLatitude())<THETA){
                return Math.abs(destinationPosition.longitude() - result.getLastLocation().getLongitude()) < THETA;
            }
            return false;
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            ExistingPlaylistJourney activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}