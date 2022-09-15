package com.mim.apollo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mim.apollo.dto.AlertaDTO;
import com.mim.apollo.dto.PacienteDTO;

import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AlertaAPI {


    public class Factory {
        private static AlertaAPI service;

        public static AlertaAPI getInstance() {

            //if (service == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            /*Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();*/

            Gson gson = new GsonBuilder()
                    .setDateFormat("dd-M-yyyy HH:mm:ss")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    //.baseUrl(instance.getBASE_URL())
                    .baseUrl("https://csm-2022.ny-2.paas.massivegrid.net/hackaton/webresources/")
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            service = retrofit.create(AlertaAPI.class);
            return service;
            // } else {
            // return service;
            //}
        }
    }


    @GET("com.mim.usuario/login/{user}/{password}")
    public Call<PacienteDTO> executeLogin(@Path("user") String user,@Path("password") String password);

    @GET("com.mim.alerta/estatus/{secret}")
    public Call<String> retrieveStatus(@Path("secret") String secret);

    @POST("com.mim.alerta")
    public Call<String> createAlerta(@Body AlertaDTO alerta);

    //@POST("com.mim.entities.permisotrabajo")
   // public Call<PermisoTrabajo> persistPermiso(@Body PermisoTrabajo orden);

   // @POST("com.mim.entities.permisotrabajo/update/programado")
    //public Call<PermisoTrabajo> updatePermiso(@Body PermisoTrabajo orden);

    @POST("com.mim.alerta/share/{secret}")
    //@Headers("Content-Type: text/plain")
    public Call<Void> sendMessage(@Path("secret") String secret,@Body String body);


   // @GET("com.mim.entities.permisotrabajo/openPermisos")
    //public Call<List<PermisoTrabajo>> retrieveOpen();


    //upload picture
    @Multipart
    @POST("com.mim.alerta/prime")
    public Call<ResponseBody> uploadImage2(@Part("id") String id, @Part("file") RequestBody imagen);

    @Multipart
    @POST("com.mim.entities.fotos/profile/picture")
    public Call<ResponseBody> uploadImage3(@Part("userid") String user, @Part("id") String id, @Part("file") RequestBody imagen);


}
