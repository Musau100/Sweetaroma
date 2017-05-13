package com.muusa.sweetaroma.activities;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.muusa.sweetaroma.NearByPlace;
import com.muusa.sweetaroma.R;
import com.muusa.sweetaroma.fragments.PlacesListFragment;
import com.muusa.sweetaroma.utils.App;
import com.muusa.sweetaroma.utils.AppConfig;
import com.muusa.sweetaroma.utils.InternetConnection;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static com.muusa.sweetaroma.utils.AppConfig.GEOMETRY;
import static com.muusa.sweetaroma.utils.AppConfig.ICON;
import static com.muusa.sweetaroma.utils.AppConfig.ID;
import static com.muusa.sweetaroma.utils.AppConfig.LATITUDE;
import static com.muusa.sweetaroma.utils.AppConfig.LOCATION;
import static com.muusa.sweetaroma.utils.AppConfig.LONGITUDE;
import static com.muusa.sweetaroma.utils.AppConfig.OK;
import static com.muusa.sweetaroma.utils.AppConfig.OPENING_HOURS;
import static com.muusa.sweetaroma.utils.AppConfig.OPEN_NOW;
import static com.muusa.sweetaroma.utils.AppConfig.PLACE_ID;
import static com.muusa.sweetaroma.utils.AppConfig.PLACE_NAME;
import static com.muusa.sweetaroma.utils.AppConfig.RATING;
import static com.muusa.sweetaroma.utils.AppConfig.REFERENCE;
import static com.muusa.sweetaroma.utils.AppConfig.RESULTS;
import static com.muusa.sweetaroma.utils.AppConfig.STATUS;
import static com.muusa.sweetaroma.utils.AppConfig.VICINITY;
import static com.muusa.sweetaroma.utils.AppConfig.ZERO_RESULTS;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, FullScreenDialogFragment.OnConfirmListener, FullScreenDialogFragment.OnDiscardListener{

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int INITIAL_ZOOM_LEVEL = 15;
    protected GoogleApiClient mGoogleApiClient;
    public static final int PROXIMITY_RADIUS = 2000;//Distance in Km
    protected LocationRequest mLocationRequest;
    private InternetConnection networkConnection;
    private CoordinatorLayout mainCoordinatorLayout;
    private TextView txtLocation;
    private GoogleMap map;
    private LoadToast loadToast;
    private SpinKitView spinKitViewProgressBounce;
    private MaterialSpinner spinner;
    private Location mCurrentLocation=null;
    private ArrayList<NearByPlace> nearByPlaceArrayList = new ArrayList<>();
    private FullScreenDialogFragment dialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            return;
        }
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkConnection = new InternetConnection(getBaseContext());
        loadToast = new LoadToast(this);

        assignViews();

//        populate spinner with places type
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.places));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                getPermissions();
            }
        }

        if (!networkConnection.isLocationEnabled()) {
            networkConnection.showLocationSettings(mainCoordinatorLayout);
            return;
        }

        if (!networkConnection.isInternetAvailable()) {
            networkConnection.showWirelessSettings(mainCoordinatorLayout);
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final String dialogTag = "dialog";
        if (savedInstanceState != null) {
            dialogFragment =
                    (FullScreenDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogTag);
            if (dialogFragment != null) {
                dialogFragment.setOnConfirmListener(this);
                dialogFragment.setOnDiscardListener(this);
            }
        }



    }
    private void assignViews() {
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        spinKitViewProgressBounce = (SpinKitView) findViewById(R.id.spinKitViewProgressBounce);
        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinator);
//        spinner = (MaterialSpinner) findViewById(R.id.placeTypeSpinner);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        loadToast.setText("Getting your location...")
                .setProgressColor(Color.BLUE)
                .setBackgroundColor(Color.MAGENTA)
                .setTextColor(Color.BLUE)
                .setTranslationY(150)
                .show();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);

        buildGoogleApiClient();

//        spinner on change listener, get nearby places based on selected type
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String placeType=parent.getSelectedItem().toString();
//
//                if(mCurrentLocation!=null){
//                    loadNearByPlaces(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),placeType);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        createLocationRequest();
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getPermissions();
            }
        }

        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {

            loadToast.success();

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), INITIAL_ZOOM_LEVEL));

            // Begin polling for new location updates.
            startLocationUpdates();

            onLocationChanged(mCurrentLocation);
        }
    }
    private void startLocationUpdates() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getPermissions();
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        loadToast.error();
        Log.i(AppConfig.TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(AppConfig.TAG, "Connection suspended");
        if (cause == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (cause == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                getPermissions();
            }
        }

        if (!networkConnection.isLocationEnabled()) {
            networkConnection.showLocationSettings(mainCoordinatorLayout);
            return;
        }

        if (!networkConnection.isInternetAvailable()) {
            networkConnection.showWirelessSettings(mainCoordinatorLayout);
            return;
        }

        if (map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation=location;

//        load the users location details
        getCurrentAddress(location);

//        get the selected item from spinner
        String placeType="restaurant";

        loadNearByPlaces(location.getLatitude(), location.getLongitude(), placeType);
    }
    private void getCurrentAddress(final Location location) {

        showAddressProgressBar(true);
        String city, country;
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {

//                hide the progress bar
                showAddressProgressBar(false);

                final Address address = addresses.get(0);
                result.append(address.getAddressLine(0)).append(", ");
//                address.getAddressLine(0);
                if (address.getLocality() != null) {
                    city = address.getLocality();
                    result.append(city).append(", ");
                }

                if (address.getCountryName() != null) {
                    country = address.getCountryName();
                    result.append(country);
                }

                txtLocation.setText(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNearByPlaces(double latitude, double longitude, String placeType) {

        loadToast.setText("Getting nearby places...")
                .setProgressColor(Color.BLUE)
                .setBackgroundColor(Color.MAGENTA)
                .setTextColor(Color.BLUE)
                .setTranslationY(150)
                .show();

//        String googlePlacesUrl = SAMPLE_DATA_URL;
        String googlePlacesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + latitude + "," + longitude +
                "&radius=" + PROXIMITY_RADIUS +
                "&type="+placeType +
                "&sensor=true" +
                "&key=" +getString(R.string.google_maps_key);

        JsonObjectRequest request = new JsonObjectRequest(googlePlacesUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {

                loadToast.success();

                Log.i(AppConfig.TAG, "onResponse: Result= " + result.toString());

                parseLocationResult(result);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadToast.error();

                        Log.e(AppConfig.TAG, "onErrorResponse: Error= " + error);
                        Log.e(AppConfig.TAG, "onErrorResponse: Error= " + error.getMessage());
                    }
                });

        App.getInstance().addToRequestQueue(request);
    }

    private void parseLocationResult(JSONObject result) {

        String id, place_id, placeName, reference, vicinity = null, openNow,icon = null;
        double latitude, longitude;
        float rating = 0;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        try {
            JSONArray jsonArray = result.getJSONArray(RESULTS);

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

//                clear map and arraylist
                map.clear();
                nearByPlaceArrayList.clear();


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);

                    id = place.getString(ID);
                    place_id = place.getString(PLACE_ID);

                    if (!place.isNull(PLACE_NAME)) {
                        placeName = place.getString(PLACE_NAME);

                        if (!place.isNull(VICINITY)) {
                            vicinity = place.getString(VICINITY);
                        }
                        latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION).getDouble(LATITUDE);
                        longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION).getDouble(LONGITUDE);
                        reference = place.getString(REFERENCE);
                        openNow = "false";
                        if (!place.isNull(OPENING_HOURS)) {
                            openNow = place.getJSONObject(OPENING_HOURS).getString(OPEN_NOW);
                        }
                        if(!place.isNull(ICON)) {
                            icon = place.getString(ICON);
                        }
                        if (!place.isNull(RATING)) {
                            rating = Float.parseFloat(place.getString(RATING));
                        }

                        Log.i(AppConfig.TAG, "parseLocationResult: Id= " + id);
                        Log.i(AppConfig.TAG, "parseLocationResult: Place_Id= " + place_id);
                        Log.i(AppConfig.TAG, "parseLocationResult: Place name= " + placeName);
                        Log.i(AppConfig.TAG, "parseLocationResult: Reference= " + reference);
                        Log.i(AppConfig.TAG, "parseLocationResult: Vicinity= " + vicinity);
                        Log.i(AppConfig.TAG, "parseLocationResult: Icon= " + icon);
                        Log.i(AppConfig.TAG, "parseLocationResult: Latitude= " + latitude);
                        Log.i(AppConfig.TAG, "parseLocationResult: Longitude= " + longitude);
                        Log.i(AppConfig.TAG, "parseLocationResult: Rating= " + rating);
                        Log.i(AppConfig.TAG, "parseLocationResult: OpenNow= " + openNow);

                        NearByPlace nearByPlace = new NearByPlace();
                        nearByPlace.setId(id);
                        nearByPlace.setPlaceId(place_id);
                        nearByPlace.setPlaceName(placeName);
                        nearByPlace.setIcon(icon);
                        nearByPlace.setVicinity(vicinity);
                        nearByPlace.setLatitude(latitude);
                        nearByPlace.setLongitude(longitude);
                        nearByPlace.setReference(reference);
                        nearByPlace.setRating(rating);
                        nearByPlace.setOpenNow(openNow);

                        getRestaurantThumbnail(nearByPlace,place_id);

                        // Add a new marker to the map
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(placeName)
                                .snippet(vicinity));

//                    pass extra data
                        marker.setTag(nearByPlace);

//                        zoom to fit all markers in the map
                        builder.include(new LatLng(latitude, longitude));


//                        this arraylist is the one will pass to our recyclerview
                        nearByPlaceArrayList.add(nearByPlace);
                    }
                }
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                LatLngBounds latLngBounds=builder.build();
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,width,height,padding));

//                add the supermarkets to the recyclerview
//                supermarketsRecyclerView.setAdapter(new SupermarketAdapter(superMarkets, getBaseContext()));

//                SimpleToast.ok(getBaseContext(), jsonArray.length() + " Supermarkets found!");
                Log.i(AppConfig.TAG, "parseLocationResult: Supermarkets Loaded are= " + jsonArray.length());
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {

                Log.e(AppConfig.TAG, "parseLocationResult: Status result= " + ZERO_RESULTS);
                Toast.makeText(getBaseContext(), "No Supermarket found within 5KM radius!!!", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(AppConfig.TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }

    private void getRestaurantThumbnail(final NearByPlace nearByPlace, final String place_id) {
        StringRequest request=new StringRequest(Request.Method.GET, "http://fed699ef.ngrok.io/sweet_aroma_backend/restaurant_thumbnail.php" + "?place_id=" + place_id , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.i(TAG, "onResponse: response");
                try {
                    JSONObject result = new JSONObject(response);
                    Log.i(AppConfig.TAG, "onResponse: "+ result);
                    String message = result.getString("message");
                    if (result.getString("message").equals("success")){
                        JSONObject data = result.getJSONObject("data");
                        Log.i(AppConfig.TAG, "data : " + data);
                        String thumbnail = data.getString("thumbnail");
                        nearByPlace.setIcon(thumbnail);
                        Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                Map param=new HashMap();
                param.put("place_id",place_id);
                return param;
            }
        };

        App.getInstance().addToRequestQueue(request);
    }

    private void showAddressProgressBar(boolean show) {
        if (show) {
            spinKitViewProgressBounce.setVisibility(View.VISIBLE);
        } else {
            spinKitViewProgressBounce.setVisibility(View.GONE);
        }
    }

    private void getPermissions() {
        new TedPermission(this)
                .setDeniedMessage("We need all these permissions for us to display the map")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(AppConfig.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConfirm(@Nullable Bundle result) {

    }

    @Override
    public void onDiscard() {

    }

    @Override
    public void onBackPressed() {
        if (dialogFragment != null && dialogFragment.isAdded()) {
            dialogFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_listMenu) {
            final Bundle args = new Bundle();
            args.putParcelableArrayList(AppConfig.MAP_LIST_ARRAYLIST, nearByPlaceArrayList);

            dialogFragment = new FullScreenDialogFragment.Builder(MapsActivity.this)
                    .setTitle("Nearby Places List")
                    .setOnConfirmListener(MapsActivity.this)
                    .setOnDiscardListener(MapsActivity.this)
                    .setContent(PlacesListFragment.class, args)
                    .build();

            dialogFragment.show(getSupportFragmentManager(), AppConfig.TAG);
        }

        return super.onOptionsItemSelected(item);
    }
}
