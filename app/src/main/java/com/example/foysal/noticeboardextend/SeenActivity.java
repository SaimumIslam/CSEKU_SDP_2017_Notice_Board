package com.example.foysal.noticeboardextend;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class SeenActivity extends AppCompatActivity {


    private TextView t_display,file_display;
    private Button download;
    private ProgressDialog progressdialog;


    String title,description,firstName,date,batch,file,Sfile;
    String download_url = "http://notify.dgted.com/notice/images/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen);

        t_display = (TextView) findViewById(R.id.Description);
        file_display=(TextView)findViewById(R.id.file);
        download=(Button)findViewById(R.id.download);

        final Intent in = getIntent();
        Bundle b = in.getExtras();
        title = b.getString("Title");
        description = b.getString("Description");
        firstName=b.getString("FirstName");
        date=b.getString("Date");
        batch=b.getString("Batch");
        file =b.getString("File");
        Sfile=b.getString("ShowFile");


        if(file.equals("null")||file.equals("NULL")||file.equals(""))
        {

        }
        else
        {
            download.setVisibility(View.VISIBLE);
            file_display.setVisibility(View.VISIBLE);
            file_display.setText("File Title: "+Sfile);
        }

        t_display.setText("Title:  "+title+"\n\nDescription:  "+description+
                "\n\n"+firstName+"\n"+"Batch: "+batch+
                "\n\nPublished at : "+date+"\n\n");
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission_check();
                Toast.makeText(SeenActivity.this,"Downloading",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void permission_check()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
        }
        initialize();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && (grantResults[0]==PackageManager.PERMISSION_GRANTED))
        {
            initialize();
        }
        else
        {
            permission_check();
        }
    }

    public void initialize()
    {
        progressdialog = new ProgressDialog(this);
        progressdialog.setTitle("Downloading....");
        progressdialog.setMax(100);
        progressdialog.setCancelable(false);
        progressdialog.setProgressStyle(progressdialog.STYLE_HORIZONTAL);

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient clint= new OkHttpClient();
                okhttp3.Request request=new okhttp3.Request.Builder()
                        .url(download_url+file)
                        .build();
                okhttp3.Response response=null;
                try
                {
                    response=clint.newCall(request).execute();
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory()+"/Download/"+file);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}