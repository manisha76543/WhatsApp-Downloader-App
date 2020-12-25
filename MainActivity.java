package com.example.ws_new;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

//public class MainActivity extends AppCompatActivity implements MediaScannerConnection.MediaScannerConnectionClient
public class MainActivity extends AppCompatActivity implements MediaScannerConnection.MediaScannerConnectionClient
{

    File imageFile;
    Button statusBtn, savedStatusBtn, wpGallery;
    public String[] allFiles;
    private String SCAN_PATH;
    private static final String FILE_TYPE = "image/*";
    private MediaScannerConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        //setUpRefreshLayout();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();


        statusBtn = (Button) findViewById(R.id.status_button);
        savedStatusBtn = (Button) findViewById(R.id.saved_status_button);
        wpGallery = (Button) findViewById(R.id.whatsApp_gallery_btn);

        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addActivity.class);
                startActivity(intent);
            }
        });

        wpGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/WS Downloader");
                intent.setDataAndType(uri, "*/ *");
                startActivity(Intent.createChooser(intent,"Open folder"));
            }
        });



        savedStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileAvailability();
                   startScan();

            }
        });


    }

    private void checkFileAvailability() {
        if (Environment.getExternalStorageState() != null) {
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/WS Downloader/");
            if (folder.exists()) {
                allFiles = folder.list();
                //  uriAllFiles= new Uri[allFiles.length];
                if (allFiles.length != 0) {
                    for (int i = 0; i < allFiles.length; i++) {
                        Log.d("all file path" + i, allFiles[i] + allFiles.length);
                    }
                    //  Uri uri= Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/yourfoldername/"+allFiles[0]));
                    SCAN_PATH = Environment.getExternalStorageDirectory().toString() + "/WS Downloader/" + allFiles[0];
                    System.out.println(" SCAN_PATH  " + SCAN_PATH);

                    Log.d("SCAN PATH", "Scan Path " + SCAN_PATH);
                }
            }
        }
        else
        {
            Toast.makeText(this, "no file exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void startScan() {
        Log.d("Connected","success"+conn);
        if (conn!=null) {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(this,this);
        conn.connect();
    }

    @Override
    public  void onMediaScannerConnected() {
        Log.d("onMediaScannerConnected","success"+conn);
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted", uri + "success" + conn);
            System.out.println("URI " + uri);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        } finally {
            conn.disconnect();
            conn = null;
        }
    }
    }
//}