package com.robert.dagger2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Inject
    NetworkApi networkApi;

    private static String SCAN_PATH;
    private boolean go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean injected = networkApi == null ? false : true;
        TextView userAvailable = (TextView) findViewById(R.id.target);
        userAvailable.setText("Dependency injection worked: " + String.valueOf(injected));

        SCAN_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "Camera" + File.separator+"1.jpg";

        System.out.println(" SCAN_PATH  " + SCAN_PATH);
    }

    public void goAlbum(View view) {
        MediaScanner mediaScanner = new MediaScanner(this);
        mediaScanner.scanFile(new File(SCAN_PATH), "image/*");
        mediaScanner.setOnFinishListener(new MediaScanner.OnFinishListener() {
            @Override
            public void onFinish(String path, Uri uri) {
                if (!go) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                    go = true;
                }
            }
        });
    }


}
