package com.gmail.thales_silva_nascimento.alarmmed.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.RetrofitMaps;
import com.gmail.thales_silva_nascimento.alarmmed.model.Example;
import com.gmail.thales_silva_nascimento.alarmmed.model.Result;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FarmaciaProxima extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener,ResultCallback {

    private GoogleMap mMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 5000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private HashMap<String,Result> mapResult;
    private Toolbar toolbar;
    //BottomSheet
    private TextView nomeFarma;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private RatingBar ratingBar;
    private Button btnRotas;
    //Posição do marcador selecionado
    private double latPlace;
    private double lonPlace;

    private int REQUEST_CHECK_SETTINGS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmacia_proxima);

        toolbar = (Toolbar) findViewById(R.id.tbFarmacia);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Fármacias Próximas");

        //Onclick no botão de retonar da toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cria um intent para retorna o valor
                Intent i = new Intent();
                //Adiciona o resultado a ser comparado e a intent
                setResult(1, i);
                finish();
            }
        });

        View bottomView = (View) findViewById(R.id.rlBottomSheet);
        //Diz qual view possui o comportamento do BottomSheetbehavior
        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomView);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);

        nomeFarma = (TextView) findViewById(R.id.tvNomeFarma);
        //Rating Bar
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        //inicializa o Map contendo os resultado
        mapResult = new HashMap<>();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        }
        else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }


        //Verifica se o gps está ligado se não encerra a aplicação



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("Oncreate", "Iniciou o mapa");

        btnRotas = (Button) findViewById(R.id.btnRotas);
        //Onclick
        btnRotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = String.valueOf(latPlace);
                String lon = String.valueOf(lonPlace);
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lon+"&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Verifica se o app possui a permissão para acessar a localização
     * @return
     */
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Função que verifica se o usuário deu a permissão para acessar a localização
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("onMapReady", "Entrou");
        mMap = googleMap;


        //Método chamado quando o usuário clica no mapa.
        //Verifica se o BottomSheet de informação está aberto, se sim então feche igual ao botão voltar
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        //Método chamado quando o usuário clica em um marcador.
        //Verifica de existe um BottoSheet aberto se existe fecha e abre com as novas informações
        //se não atualiza as informação
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                    //Abre novamente com as novas informaçoes
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

                    //Grava na variável LatLngMarker a latitude e longitude do marcador selecionado
                    latPlace = marker.getPosition().latitude;
                    lonPlace = marker.getPosition().longitude;
                    Log.v("Latitude e Lon", String.valueOf(latPlace)+" - "+String.valueOf(lonPlace));

                    //Procura informações no hashMap
                    Result result = mapResult.get(marker.getTitle());
                    if(result != null){
                        nomeFarma.setText(result.getName());
                        if(result.getRating()!= null){
                            ratingBar.setRating(result.getRating().floatValue());
                        }

                    }else{
                        nomeFarma.setText(marker.getTitle());
                        ratingBar.setRating(0.0f);
                    }

                }else {
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                //Retornando true não aparece a infowindows
                return true;
            }
        });

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                Log.v("OnMapReady","if = true");
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            Log.v("OnMapReady","if = false");
        }


    }


    private void build_retrofit_and_get_response(String type) {
        //Url base para fazer a requisição ao google
        String url = "https://maps.googleapis.com/maps/";
        //Instancia o objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Adiciona a interface ao objeto da retrofit
        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
        //Insere as infomarções da requisição
        Call<Example> call = service.getNearbyPlaces(type, String.valueOf(latitude) + "," + String.valueOf(longitude), PROXIMITY_RADIUS);
        //Realiza a requisiçao ao google
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                try {
                    mMap.clear();
                    //Limpa o HashMap
                    mapResult.clear();
                    Example resposta = response.body();
                    Log.v("build_Retrofit", "Resposta: "+resposta.getStatus()+resposta.toString());
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        //Seleciona um item do resultado da busca
                        Result resultadoItem = resposta.getResults().get(i);
                        //Adiciona no HashMap o Marcador
                        mapResult.put(resultadoItem.getName(), resultadoItem);
                        //Insere os dados no marcador
                        Double lat = resultadoItem.getGeometry().getLocation().getLat();
                        Double lng = resultadoItem.getGeometry().getLocation().getLng();
                        String placeName = resultadoItem.getName();
                        String vicinity = resultadoItem.getVicinity();
                        //Cria um marcador
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        // Position of Marker on Map
                        markerOptions.position(latLng);
                        // Adding Title to the Marker
                        markerOptions.title(placeName);

                        // Adding Marker to the Camera.
                        Marker m = mMap.addMarker(markerOptions);

                        // Adding colour to the marker
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    //Latitude e longitude atual do aparelho
                    LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    //Adiciona novamente o marcador
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Posição Atual");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    // Adiciona a posição atual ao mapa
                    mMap.addMarker(markerOptions);


                } catch (Exception e) {
                    Log.v("onResponse", "Erro: " + e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.v("Maps-onFailure", "Erro: " + t.toString());
            }
        });
    }


    /**
     * Instancia a GoogleClientApi
     */
    protected synchronized void buildGoogleApiClient() {
        Log.v("buildGoogleApi", "Entrou");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Log.v("buildGoogleApi", "executou o connect");
    }

    /**
        Callback chamado ao conectar a api GoogleClientAPI
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v("onConnected", "entrou");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(20*10000); //1 minuto e meio
        mLocationRequest.setFastestInterval(50000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
        //startLocationUpdates();

    }

    @Override
    public void onResult(@NonNull com.google.android.gms.common.api.Result locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(FarmaciaProxima.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                    e.printStackTrace();
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    /**
        Callback chamado quando a conexão com a Api GoogleClientAPI é suspendida
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.v("onConected", "ConnectionsSuspended");
    }

    /**
     * Função chamada quando a conexão com o GoogleClientApi falha
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("onConected", "A conexão falhou");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    /**
     * Trigger new location updates at interval
     */
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        Log.v("startLocationUpdates"," entrou");
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Log.v("startLocationUpdates"," mudou position");
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    /**
     * Função chamada quando a posição  do dispositivo é alterada
     * @param location
     */
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posição Atual");

        // Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        // Adding Marker to the Map
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        Log.v("onLocationChange", "adicionou o marcador");

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.5f));

        /*
            Procura as farmacia próxima a localização atual
         */
        build_retrofit_and_get_response("pharmacy");

        Log.v("onLocationChanged", "latitude: "+latitude+" longitude: "+ longitude);
    }

    //Método usado para verificar se ao apertar o botão Back
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED){
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else{
            //Cria um intent para retorna o valor
            Intent i = new Intent();
            //Adiciona o resultado a ser comparado e a intent
            setResult(1, i);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                startLocationUpdates();
                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {
                //Cria um intent para retorna o valor
                Intent i = new Intent();
                //Adiciona o resultado a ser comparado e a intent
                setResult(1, i);
                finish();
            }

        }
    }

}
