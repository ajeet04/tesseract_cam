package com.example.ajeetyadav.camera;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Anuj Sarawat on 4/30/2017.
 */

public class ImageManipulation {
    public static Bitmap clean_image(Bitmap bitmap){
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Color p;
        for(int i=0;i<bitmap.getHeight();i++){
            for(int j=0;j<bitmap.getWidth();j++){
                int pixel = mutableBitmap.getPixel(j,i);
                int a = Color.alpha(pixel);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                if(r*0.3+g*0.5+b*0.2<150) {
                    mutableBitmap.setPixel(j,i,Color.BLACK);
                }
                else
                    mutableBitmap.setPixel(j,i,Color.WHITE);


                }
            }
            return mutableBitmap;
        }
    }

