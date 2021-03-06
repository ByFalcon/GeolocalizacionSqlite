package com.example.danie.geolocalizacionsqlite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.geolocalizacionsqlite.pojo.Lugar;
import com.example.danie.geolocalizacionsqlite.localizacion.ServicioGeocoder;
import com.example.danie.geolocalizacionsqlite.sqlite.Ayudante;
import com.example.danie.geolocalizacionsqlite.sqlite.GestorLugar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddLugar extends AppCompatActivity {

    private static final int PERMISO_GPS = 1;
    public static final String TAG = MainActivity.class.getSimpleName();

    //private AddressResultReceiver resultReceiver;
    private FusedLocationProviderClient fusedLocationClient;
    private Location ultimaPosicion = null;
    private LocationCallback callback;
    private LocationRequest request;

    private EditText etNombre;
    private Button btGuardar;
    private Button btVolver;
    private Button btSumar;
    private Button btRestar;
    private TextView tvPuntuacion;
    private EditText etComentario;

    Lugar lugar = new Lugar();

    GestorLugar gestor;

    /*class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }
            String resultado = resultData.getString(ServicioGeocoder.Constants.RESULT_DATA_KEY);
            //aquí ya puedo guardar el lugar tengo toda la información
            Log.v(TAG, resultado);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lugar);

        Log.v(TAG, "inicio");

        etNombre = findViewById(R.id.editText);
        btGuardar = findViewById(R.id.button);
        btVolver = findViewById(R.id.btVolver);
        btSumar = findViewById(R.id.btSumar);
        btRestar = findViewById(R.id.btRestar);
        etComentario = findViewById(R.id.etResumen);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);

        gestor = new GestorLugar(this, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.v(TAG, "aquí debe saltar un elemento que explique la razón por la que se necesita el permiso");
            } else {
                Log.v(TAG, "aquí se solicita el permiso (normalmente la primera vez)");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISO_GPS);
            }
        } else {
            Log.v(TAG, "se dispone del permiso por lo que se lanza directamente");
            getLocation();
        }

        btSumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int puntuacion = Integer.parseInt(tvPuntuacion.getText().toString());
                if(puntuacion<5){
                    puntuacion++;
                    tvPuntuacion.setText(String.valueOf(puntuacion));
                }
            }
        });

        btRestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int puntuacion = Integer.parseInt(tvPuntuacion.getText().toString());
                if(puntuacion>1){
                    puntuacion--;
                    tvPuntuacion.setText(String.valueOf(puntuacion));
                }
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lugar.setNombre(etNombre.getText().toString());
                lugar.setComentario(etComentario.getText().toString());
                lugar.setPuntuacion(Integer.parseInt(tvPuntuacion.getText().toString()));
                String date = dateNow();
                lugar.setFecha(date);
                //getLocation();
                Lugar lugarFinal = new Intent().getParcelableExtra("lugarFinal");
                if(lugarFinal != null) {
                    lugar.setLocalidad(lugarFinal.getLocalidad());
                    lugar.setPais(lugarFinal.getPais());
                }
                Long num = gestor.add(lugar);
                if (num != -1){
                    Log.v("ZZZ", "Se ha insertado");
                }
                //finish();
            }
        });
        /*
        pruebas
         */
        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //resultReceiver = new AddressResultReceiver(new Handler());
        callback = createLocationCallback();
        request = createLocationRequest();

        /*fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.v(TAG, "obteniendo última Location conocida");
                        if (location != null) {
                            Log.v(TAG, "última Location no es null");
                            ultimaPosicion = location;
                            lugar.setLatitud(location.getLatitude());
                            lugar.setLongitud(location.getLongitude());

                            //tvUno.setText("latitud: " + location.getLatitude());
                            //tvDos.setText("longitud " + location.getLongitude());
                            //ultimaPosicion.setLatitude(37.161262);
                            //ultimaPosicion.setLongitude(-3.5922742);

                        } else {
                            Log.v(TAG, "última Location es null");
                        }
                    }
                });*/
        fusedLocationClient.requestLocationUpdates(
                request,
                callback,
                null /* Looper */);
    }

    private LocationCallback createLocationCallback() {
        Log.v(TAG, "creando objeto que se ejecuta después de cada nueva Location");
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.v(TAG, "se ha recibido nueva Location");
                for (Location location : locationResult.getLocations()) {
                    ultimaPosicion = location;
                    Log.v(TAG, location.getLatitude() + " " + location.getLongitude());
                }
                requestAddress(ultimaPosicion);
                lugar.setLatitud(ultimaPosicion.getLatitude());
                lugar.setLongitud(ultimaPosicion.getLongitude());
                fusedLocationClient.removeLocationUpdates(callback);

            }
        };
        return locationCallback;
    }

    private void requestAddress(Location location) {
        Intent intent = new Intent(this, ServicioGeocoder.class);
        //intent.putExtra(ServicioGeocoder.Constants.RECEIVER, resultReceiver);
        intent.putExtra(ServicioGeocoder.Constants.LOCATION_DATA_EXTRA, location);
        intent.putExtra(ServicioGeocoder.Constants.LOCATION_DATA_EXTRA2, lugar);
        startService(intent);
    }

    protected LocationRequest createLocationRequest() {
        Log.v(TAG, "creando solicitud de Locations");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        //locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISO_GPS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "con permiso");
                    getLocation();
                } else {
                    Log.v(TAG, "sin permiso");
                }
            }
        }
    }

    public String dateNow(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
