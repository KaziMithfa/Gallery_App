package com.example.uploadmultiplefiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class Activityfullimage extends AppCompatActivity {

    ZoomageView imageView;
    private String key;
    private static final int REQUEST_CODE= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityfullimage);

        imageView = findViewById(R.id.zoomViewId);
        key = getIntent().getStringExtra("image");

        Glide.with(this).load(key)
                .into(imageView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage();
            }
            else{
                Toast.makeText(this, "Please , provide required permission....", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.save){
            if(ContextCompat.checkSelfPermission(Activityfullimage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){
                saveImage();

            }

            else{
                ActivityCompat.requestPermissions(Activityfullimage.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                },REQUEST_CODE);
            }



        }

        else if(item.getItemId() == R.id.send){
            shareImage();
        }

        return super.onOptionsItemSelected(item);

    }

    private void shareImage(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        Uri uri = getImagetoShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/");
        startActivity(Intent.createChooser(intent,"Share Images"));




    }

    private Uri getImagetoShare(Bitmap bitmap){
        File folder = new File(getCacheDir(),"images");
        Uri uri = null;

        try {

        folder.mkdir();
        File file = new File(folder,"shared_images.jpg");
        FileOutputStream fileOutputStream = null;
        fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

        fileOutputStream.flush();
        fileOutputStream.close();

        uri = FileProvider.getUriForFile(this,"com.example.uploadmultiplefiles",file);

        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return  uri;
    }

    private void saveImage(){

        Uri images  ;
        ContentResolver contentResolver = getContentResolver();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }
        else{
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis() +".jpeg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
        Uri uri = contentResolver.insert(images,contentValues);

        try{

            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            Objects.requireNonNull(outputStream);
            Toast.makeText(this, "Images saved successfully....", Toast.LENGTH_SHORT).show();

        }

        catch(Exception e){

            Toast.makeText(this, "Image is not saved successfully...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();


        }


    }
}