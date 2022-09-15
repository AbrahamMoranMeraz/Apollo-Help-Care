package com.mim.apollo;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.RECORD_AUDIO;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mim.apollo.databinding.ActivityMainBinding;
import com.mim.apollo.dto.AlertaDTO;
import com.mim.apollo.dto.PacienteDTO;
import com.mim.apollo.dto.TipoalertaDTO;
import com.mim.apollo.dto.UsuarioDTO;
import com.mim.apollo.service.AlertaAPI;
import com.mim.apollo.ui.gallery.GalleryFragment;
import com.mim.apollo.ui.home.HomeFragment;
import com.mim.apollo.ui.slideshow.SlideshowFragment;
import com.mim.apollo.util.CustomDialog;
import com.mim.apollo.util.FileDB;
import com.mim.apollo.util.LocationTrack;
import com.mim.apollo.util.MyIntentService;
import com.mim.apollo.util.Uploader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Uploader.UploaderConsumer
        , HomeFragment.OnFragmentInteraction
        , GalleryFragment.OnFragmentInteraction
        , SlideshowFragment.OnFragmentInteraction
        , CustomDialog.OnFragmentInteraction {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private String control = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String fileName;

    private Handler mHandler = new Handler();

    private NavController navController;
    private Integer tipoAlerta = 1;
    private PacienteDTO pcr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        readList(new File(this.getFilesDir(),
                "cerveceria.txt"));

        if (bundle != null) {
            if (bundle.get("hola") != null) {
                control = (String) bundle.get("hola");
                //Toast.makeText(MainActivity.this, String.valueOf(bundle.get("hola")), Toast.LENGTH_LONG).show();
            }
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.cerrar)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Toast.makeText(MainActivity.this,String.valueOf(view.getId()), Toast.LENGTH_LONG).show();
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        askPermissions();
        sendNotification();
        File file = new File(getFilesDir(),
                "settings.txt");
        try {
            String data = FileUtils.readFileToString(file, "UTF-8");
            if (data != null) {
                if(data.length()>3) {
                    navController.navigate(R.id.nav_slideshow);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //launchAlarmIfNeeded();
        //Toast.makeText(MainActivity.this,"hola....",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                FileDB.writeToSettingsFile("", getFilesDir().getAbsolutePath());
                startPage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchAlarmIfNeeded(int tipoAlarma) {//1 high, low 2
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
            //Toast.makeText(MainActivity.this,"pt: "+pcr.getIdpaciente(),Toast.LENGTH_LONG).show();
            paciente.setIdpaciente(pcr.getIdpaciente());

            UsuarioDTO usu = new UsuarioDTO();
            if(pcr.getUsuarioIdusuario()!=null) {
                usu.setIdusuario(pcr.getUsuarioIdusuario().getIdusuario());
            }
            paciente.setUsuarioIdusuario(usu);
            alert.setPacienteIdpaciente(paciente);

            TipoalertaDTO tipo = new TipoalertaDTO();
            tipo.setIdtipoalerta(tipoAlarma);

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

                            if (navController != null) {
                                navController.navigate(R.id.nav_slideshow);
                            }

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
    }


    private void askPermissions() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(RECORD_AUDIO);
        permissions.add(CALL_PHONE);

        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);

        //Create Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setLightColor(com.google.android.material.R.color.design_default_color_primary);

            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("hola", "dato2");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent viewIntent = new Intent(this, MyIntentService.class);
        PendingIntent viewPendingIntent = PendingIntent.getService(this, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Create Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123")
                .setContentTitle("Test")
                .setContentText("Hi this is first notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.apollo))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.image))
                        .bigLargeIcon(null))
                // .addAction(R.drawable.ic_launcher_foreground , "View" , viewPendingIntent)
                .setAutoCancel(false);
        notificationManager.notify(0, builder.build());

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void startRecording() {
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.OGG";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.OGG);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d("myapp", "prepare() failed");
        }

        recorder.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //while (true) {
                try {
                    Thread.sleep(15000);
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            Log.d("myapp", "termine de grabar");
                            stopRecording();
                            //onPlay(true);
                            try {
                                uploadAudio();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                }
                //  }
            }
        }).start();
    }

    private void uploadAudio() throws IOException {
        Log.d("myapp", "fin grabacion");


        File file = new File(getFilesDir(),
                "settings.txt");
        String data = FileUtils.readFileToString(file, "UTF-8");
        ;

        FileDB.dataSetUp(data, getFilesDir().toString());
        if (data != null) {
            AlertaDTO[] array = new AlertaDTO[1];
            new Uploader(MainActivity.this, 5, Integer.parseInt(data), getExternalCacheDir().getAbsolutePath()).execute(array);
            //makePhoneCall();
        } else {
            Log.d("myapp", "no estoy entrando...");
        }
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            try {
                stopPlaying();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    onPlay(false);
                }
            });
        } catch (IOException e) {
            Log.d("myapp", "prepare() failed");
        }
    }

    private void stopPlaying() throws IOException {
        player.release();
        player = null;

    }

    public void makePhoneCall() {
        AudioManager audioManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.setSpeakerphoneOn(true);
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "6641632976"));
        startActivity(intent);


    }

    @Override
    public void consumeUpload(boolean res, int codigo) {
        if (res) {
            Toast.makeText(this, "Audio loaded...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void navigateCounter() {
        if (navController != null) {
            navController.navigate(R.id.nav_gallery);
        }
    }

    @Override
    public void setTipoAlerta(int alerta) {
        tipoAlerta = alerta;
    }

    @Override
    public void gotoActivatedAlarm() {
        if (navController != null) {
            sendAlert();

        }
    }

    @Override
    public int getTipoAlerta() {
        return tipoAlerta;
    }

    @Override
    public boolean sendAlert() {
        launchAlarmIfNeeded(tipoAlerta);
        return false;
    }

    @Override
    public String retrieveSecret() {
        File file = new File(getFilesDir(),
                "settings.txt");

        String data = null;
        try {
            data = FileUtils.readFileToString(file, "UTF-8");
            Log.d("myapp", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void startPage() {
        FileDB.writeToSettingsFile("", getFilesDir().getAbsolutePath());
        navController.navigate(R.id.nav_home);
    }

    @Override
    public void sendMensaje(String content) {
        //content="alert991846827";
        if (content == null) {
            return;
        }
        File file = new File(getFilesDir(),
                "settings.txt");
        try {
            String data = FileUtils.readFileToString(file, "UTF-8");
            if (data == null) {
                if (MainActivity.this != null) {
                    Toast.makeText(MainActivity.this, "Secret lost...", Toast.LENGTH_LONG).show();
                }
                return;
            }


            AlertaAPI.Factory.getInstance().sendMessage(data, content).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (MainActivity.this != null) {
                            Toast.makeText(MainActivity.this, "Message sent...!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "falle: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                    Log.d("myapp", "falle....");
                    if (t != null) {
                        Log.d("myapp", t.getLocalizedMessage());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
    }

    private void readList(File file) {
        String content;
        if (file.exists()) {

            try {
                //FileUtils.writeStringToFile(file, "porque?", "UTF-8");
                content = FileUtils.readFileToString(file, "UTF-8");
                //Toast.makeText(MainActivity.this, "contenido: "+content, Toast.LENGTH_SHORT).show();
                if (content != null) {
                    if (content.length() > 0) {
                        Gson gson = new Gson();


                        Type listType = new TypeToken<PacienteDTO>() {
                        }.getType();

                        pcr = gson.fromJson(content, listType);
                        if(pcr==null){
                            //Toast.makeText(MainActivity.this, "estoy siendo nulo....", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   private void logout(){
       File file = new File(this.getFilesDir(),
               "cerveceria.txt");
       if (file != null) {
           if (file.exists()) {
               try {
                   FileUtils.writeStringToFile(file, "", "UTF-8");
                   //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
       Intent intent = new Intent(this, LoginActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);
   }
}