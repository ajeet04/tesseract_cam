package com.example.ajeetyadav.camera;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Anuj Sarawat on 4/30/2017.
 */

public class copyAssets {

    public void copyFile(String filename,String path,Context context) {
        AssetManager assetManager = context.getAssets();
        File Directory = new File(path);
        Directory.mkdirs();
        //File outputFile = new File(Directory, filename);
        try {
            InputStream in = assetManager.open(filename);
            String newFileName = path+"/" + filename;
            OutputStream out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
             e.printStackTrace();
        }

    }
}
