package com.robert.bitmapdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageview = (ImageView) findViewById(R.id.img);

        Bitmap bitmap = getBitmap();
        imageview.setImageBitmap(bitmap);
    }

    public Bitmap getBitmap() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/picture.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        Log.e(TAG, "getBitmap: height: " + options.outHeight + ", width: " + options.outWidth);
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        Log.e(TAG, "getBitmap: height: " + options.outHeight + ", width: " + options.outWidth);
        saveBitmap(bitmap);
        return bitmap;
    }

    public void saveBitmap(final Bitmap bitmap) {
        new Thread() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory() + "/save.png");
                if (file.exists()) {
                    boolean delete = file.delete();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "文件删除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "保存失败1", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "保存失败2", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }.start();
    }
}
