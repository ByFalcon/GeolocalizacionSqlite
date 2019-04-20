package com.example.danie.geolocalizacionsqlite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danie.geolocalizacionsqlite.pojo.Lugar;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>
        implements View.OnClickListener{

    List<Lugar> lugares;
    private View.OnClickListener listener;

    public Adaptador(List<Lugar> lugares){
        this.lugares = lugares;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, null, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvNombre.setText(lugares.get(i).getNombre());
        viewHolder.tvLatitud.setText(String.valueOf(lugares.get(i).getLatitud()));
        viewHolder.tvLongitud.setText(String.valueOf(lugares.get(i).getLongitud()));
    }

    @Override
    public int getItemCount() {
        if(lugares != null)
            return lugares.size();
        return 0;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvLatitud, tvLongitud;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreD);
            tvLatitud = itemView.findViewById(R.id.tvLatitudD);
            tvLongitud = itemView.findViewById(R.id.tvLongitudD);

        }
    }
}
