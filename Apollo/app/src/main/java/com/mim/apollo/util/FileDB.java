package com.mim.apollo.util;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mim.apollo.dto.AlertaDTO;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class FileDB {
    //this.getFilesDir()
    public static void dataSetUp(String val, String dir) {
        File file2 = new File(dir,
                "settings.txt");

         readList(val, file2);
    }

    private static void readList(String val, File file) {
        String content;
        if (file.exists()) {

            try {
                //FileUtils.writeStringToFile(file, "porque?", "UTF-8");
                content = FileUtils.readFileToString(file, "UTF-8");
                if (content != null) {
                    val = content;
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
        val = null;
    }

    public static void writeToSettingsFile(String val,String dir) {



        File file = new File(dir,
                "settings.txt");


        try {
            Log.d("myapp", "guardando: "+val);
            FileUtils.writeStringToFile(file, val, "UTF-8");
            //Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
