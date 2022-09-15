package com.mim.apollo.util;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mim.apollo.dto.AlertaDTO;
import com.mim.apollo.dto.PacienteDTO;
import com.mim.apollo.dto.TipoalertaDTO;
import com.mim.apollo.dto.UsuarioDTO;
import com.mim.apollo.service.AlertaAPI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIntentService extends Service {

    public MyIntentService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle.get("tipo") == null) {
            Log.d("myapp","DATO TIPO ALERTA VIENE NULO");
            super.onStartCommand(intent, flags, startId);
        }

        Log.d("myapp", "I got this awesome intent and will now do stuff in the background!");
        // .... do what you like
        LocationTrack locationTrack = new LocationTrack(getBaseContext());


        if (locationTrack.canGetLocation()) {


            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            locationTrack.stopListener();
            Log.d("myapp", "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude));
            AlertaDTO alert = new AlertaDTO();
            alert.setEstatus("Open");
            alert.setLat(Double.toString(latitude));
            alert.setLng(Double.toString(longitude));

            PacienteDTO paciente = new PacienteDTO();
            paciente.setIdpaciente(10);

            UsuarioDTO usu = new UsuarioDTO();
            usu.setIdusuario(1);
            paciente.setUsuarioIdusuario(usu);
            alert.setPacienteIdpaciente(paciente);

            TipoalertaDTO tipo = new TipoalertaDTO();
            tipo.setIdtipoalerta((Integer) bundle.get("tipo"));

            alert.setTipoalertaIdtipoalerta(tipo);


            AlertaAPI.Factory.getInstance().createAlerta(alert).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d("myapp", "servicio exitoso");
                        if (response.body() != null) {
                            Log.d("myapp", getFilesDir().toString());
                            Log.d("myapp", response.body());
                            FileDB.writeToSettingsFile(response.body(), getFilesDir().getAbsolutePath());

                        }
                    } else {
                        Log.d("myapp", "fallo servicio");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        } else {
            Log.d("myapp", "error location");
            //locationTrack.showSettingsAlert();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("myapp", "bind stuff");
        return null;
    }
/* @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("myapp", "I got this awesome intent and will now do stuff in the background!");
        // .... do what you like
        Toast.makeText(getBaseContext(),"SOY EL SERVICIO....!!!",Toast.LENGTH_LONG).show();
    }*/
}