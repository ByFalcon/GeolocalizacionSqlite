package com.example.danie.geolocalizacionsqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.danie.geolocalizacionsqlite.pojo.Lugar;

public class Detalle extends AppCompatActivity {

    private TextView tvNombre, tvLocalidad, tvPais, tvLatitud, tvLongitud, tvFecha, tvComentario;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Intent i = new Intent();
        Lugar lugar = i.getParcelableExtra("lugar");
        tvNombre.setText(lugar.getNombre());
        tvLocalidad.setText(lugar.getLocalidad());
        tvPais.setText(lugar.getPais());
        tvLatitud.setText(lugar.getLatitud()+"");
        tvLongitud.setText(lugar.getLongitud()+"");
        tvFecha.setText(lugar.getFecha());
        tvComentario.setText(lugar.getComentario());
        ratingBar.setRating(lugar.getPuntuacion());
    }

    public void init(){
        tvNombre = findViewById(R.id.tvNombreD);
        tvLocalidad = findViewById(R.id.tvLocalidadD);
        tvPais = findViewById(R.id.tvPaisD);
        tvLatitud = findViewById(R.id.tvLatitudD);
        tvLongitud = findViewById(R.id.tvLongitudD);
        tvFecha = findViewById(R.id.tvFechaD);
        tvComentario = findViewById(R.id.tvComentarioD);
        ratingBar = findViewById(R.id.ratingBar);
    }
}
