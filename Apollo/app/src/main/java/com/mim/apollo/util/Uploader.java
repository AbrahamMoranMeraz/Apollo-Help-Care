package com.mim.apollo.util;

import android.os.AsyncTask;
import android.util.Log;

import com.mim.apollo.dto.AlertaDTO;
import com.mim.apollo.service.AlertaAPI;

import java.io.File;
import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by marcoisaac on 5/20/2016.
 */
public class Uploader extends AsyncTask<AlertaDTO, Void, Boolean> {


    private int codigo;
    private int idAlerta;
    private String basePath;

    public interface UploaderConsumer {
        public void consumeUpload(boolean res, int codigo);
    }

    private UploaderConsumer consumer;

    public Uploader(UploaderConsumer consumer, int codigo, int idAlerta,String basePath) {
        this.consumer = consumer;
        this.codigo = codigo;
        this.idAlerta=idAlerta;
        this.basePath=basePath;
    }

    @Override
    protected Boolean doInBackground(AlertaDTO... params) {



        AlertaAPI service = AlertaAPI.Factory.getInstance();


           String ruta = basePath;
            ruta += "/audiorecordtest.OGG";
            File rep = new File(ruta);


            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("audio/ogg"), rep);


            Call<ResponseBody> res = service.uploadImage2(idAlerta+".ogg", requestFile);
            Response<ResponseBody> response = null;
            try {
                response = res.execute();
                if (response != null) {
                    if (!(response.isSuccessful())) {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (IOException e) {
                //Log.e("myapp",e.getMessage());
                e.printStackTrace();
            }



        return true;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (consumer != null) {
            consumer.consumeUpload(aBoolean, codigo);
        }
    }

}
