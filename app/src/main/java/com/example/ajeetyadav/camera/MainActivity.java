package com.example.ajeetyadav.camera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {
    ImageView imageHolder;
    EditText height,width;
    String logo,imagePath,Logo;
    String selectedImagePath;
    String filemanagerstring;
    Button setResolution;
    int column_index;
    TextView tv;
    private final int REQUEST_CODE = 20;
    private static final int SELECT_PICTURE = 1;
 int count=0,w,h;
    Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.text);


        tv.setMovementMethod(new ScrollingMovementMethod());
        height=(EditText)findViewById(R.id.et1);
        width=(EditText)findViewById(R.id.et2) ;
        File f=new File("/data/data/" + getPackageName() + "/tesseract/tessdata/eng.traineddata");
        if(!f.exists()){
            Log.d("txt","copying");
            copyAssets ob=new copyAssets();
            ob.copyFile("eng.traineddata","/data/data/" + getPackageName() + "/tesseract/tessdata",getBaseContext());
            Log.d("txt","cpied"+f.exists());
        }

        imageHolder = (ImageView)findViewById(R.id.captured_photo);
        try {
            imageHolder.setImageBitmap(getBitmapFromAsset("dis.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }


        Button capturedImageButton = (Button)findViewById(R.id.photo_button);

        capturedImageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, REQUEST_CODE);
            }
        });

        imageHolder.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }



            @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                imageHolder.setImageResource(android.R.color.transparent);
                if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                try {
                    if(height.getText().toString()!=null && width.getText().toString()!=null) {
                        h = Integer.parseInt(height.getText().toString());
                        w = Integer.parseInt(width.getText().toString());
                        tv.setText(h+" "+w);
                    }
                    else{
                        h=0;
                        w=0;
                    }

                } catch (Exception e) {
                    e.printStackTrace();


            }

            Bitmap bMapScaled;
            //Bitmap ne=ImageManipulation.clean_image(bitmap);
            if(w>0 && h>0) {
                bMapScaled = Bitmap.createScaledBitmap(bitmap, h, w, true);
            }
            else
                bMapScaled = bitmap;

            try {
                Log.d("txt", getText(bitmap));
            }
            catch(Exception e){
                e.printStackTrace();
            }

            storeCameraPhotoInSDCard(bMapScaled, count++);



            imageHolder.setImageBitmap(bitmap);
        }
        if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // Get the path from the Uri
                //String path = getPathFromURI(selectedImageUri);
                //Log.d("path" , path);
                // Set the image in ImageView

                try {
                    Bitmap bitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);;
                    if(bitMap!=null)


                    tv.setText(getText(bitMap));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageHolder.setImageURI(selectedImageUri);
            }

        }

    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;

    }




    private void storeCameraPhotoInSDCard(Bitmap bitmap,int c){
        File outputFile = new File(Environment.getExternalStorageDirectory(),"IMG020318"+c+".png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(MainActivity.this, "successfully saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory()+"/"+filename);
        if(imageFile.exists()) {

            try {
                FileInputStream fis = new FileInputStream(imageFile);
                bitmap = BitmapFactory.decodeStream(fis);
               // tv.setText(getText(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        else
            Toast.makeText(this, "image not found", Toast.LENGTH_SHORT).show();
        return bitmap;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getText(Bitmap bitmap) throws IOException{
        String txt="";
        TessBaseAPI tb = new TessBaseAPI();
        tb.init( "/data/data/" + getPackageName()+"/tesseract/","eng");

        /*
        if (rotate != 0) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            // Rotating Bitmap & convert to ARGB_8888, required by tess
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
        }*/
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        tb.setImage(bitmap);
        txt = tb.getUTF8Text();
        tb.end();
        tv.setText(txt);
        return txt;
    }

    public Bitmap getBitmapFromAsset(String filePath) {

        AssetManager assetManager = getBaseContext().getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

}