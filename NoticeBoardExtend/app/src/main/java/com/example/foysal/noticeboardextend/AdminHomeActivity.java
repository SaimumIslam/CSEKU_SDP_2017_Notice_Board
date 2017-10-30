package com.example.foysal.noticeboardextend;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String name,fileName="NULL";
    String title, description,FileName;
    private int AdminId,SuperAdminId,Id;
    boolean check=false;//file chooser check

    private EditText et_title, et_description,showfile;
    private Button post,file;

    AlertDialog.Builder builder;
    String upload_url = "http://notify.dgted.com/notice/image.php";
    //to post notice
    String post_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i=getIntent();
        Bundle b=i.getExtras();
        AdminId=b.getInt("AdminId");
        name=b.getString("Name");
        SuperAdminId=b.getInt("SuperAdminId");

        if(AdminId==-1)
        {
            Id=SuperAdminId;
            post_url = "http://notify.dgted.com/notice/PostVarsity.php";
        }
        else
        {
            Id=AdminId;
            post_url = "http://notify.dgted.com/notice/PostInDiscipline.php";
        }
        et_title=(EditText)findViewById(R.id.Title);
        et_description=(EditText)findViewById(R.id.Description);
        showfile=(EditText)findViewById(R.id.Fshow);
        post = (Button)findViewById(R.id.Post);
        file=(Button)findViewById(R.id.Filechooser);
        builder = new AlertDialog.Builder(AdminHomeActivity.this);

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermissionsAndOpenFilePicker();
                check=true;
                view.setBackgroundResource(R.color.colorPrimary);            }
        });
        //work of post button
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = et_title.getText().toString();
                description = et_description.getText().toString();
                FileName = showfile.getText().toString();

                if(title.equals("") || description.equals(""))
                {
                    builder.setTitle("Please fill up all again");
                    displayAlert("Fill title ,description, notice type");
                }
                else if(FileName.equals("")&& check)
                {
                    builder.setTitle("Please set the File name");
                    displayAlert("Fill file name");
                }
                else {
                    //notice post request
                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, post_url, new com.android.volley.Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            try
                            {
                                //getting data from json array
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                if (code.equals("Success"))
                                {
                                    builder.setTitle(code);
                                    displayAlert(jsonObject.getString("message"));
                                }
                                else
                                {
                                    builder.setTitle("Post failed");
                                    displayAlert(jsonObject.getString("message"));
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        //pushing data in php
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Title", title);
                            params.put("Description", description);
                            params.put("FileName",fileName);
                            params.put("ShowName",FileName);
                            params.put("AdimId", String.valueOf(Id));
                            return params;
                        }
                    };
                    Mysingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fragment_myprofile) {

            Intent in=new Intent(AdminHomeActivity.this,AdminProfileActivity.class);
            Bundle bundle=new Bundle();

            bundle.putInt("AdminId",AdminId);
            bundle.putString("Name",name);
            bundle.putInt("SuperAdminId",SuperAdminId);
            in.putExtras(bundle);
            AdminHomeActivity.this.finish();
            AdminHomeActivity.this.startActivity(in);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            // Handle the camera action
        } else if (id == R.id.my_admin) {

           Intent intent=new Intent(AdminHomeActivity.this,SuperAdminActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("AdminId",AdminId);
            bundle.putString("Name",name);
            bundle.putInt("SuperAdminId",SuperAdminId);
            intent.putExtras(bundle);
            AdminHomeActivity.this.finish();
            AdminHomeActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);



        } else if (id == R.id.log_out) {

            Session Asession=new Session(AdminHomeActivity.this);
            Asession.removeUser();
            Intent inten=new Intent(AdminHomeActivity.this,LoginActivity.class);
            AdminHomeActivity.this.finish();
            AdminHomeActivity.this.startActivity(inten);
            overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
 private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(AdminHomeActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AdminHomeActivity.this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(AdminHomeActivity.this, new String[]{permission},100);
            }
        } else {
            enable_file();
        }
    }
    private void enable_file() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(10)
                .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && (grantResults[0]==PackageManager.PERMISSION_GRANTED))
        {
            enable_file();
        }
        else
        {
            showError();
        }
    }
    ProgressDialog progress;

    // @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 && resultCode== Activity.RESULT_OK)
        {
            progress=new ProgressDialog(getApplicationContext());
            progress.setTitle("uplaoding");
            progress.setMessage("please wait");
            progress.show();
            final File f=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            final String Content_type=getMimeType(f.getPath());
            fileName=f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\")+1);
            fileName=fileName.substring(fileName.lastIndexOf("/")+1);
            final String file_path=f.getAbsolutePath();

             Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client=new OkHttpClient();
                    RequestBody file_body= RequestBody.create(MediaType.parse(Content_type),f);
                    RequestBody request_body=new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",Content_type)
                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1),file_body)
                            .build();
                    Request request=new Request.Builder()
                            .url(upload_url)
                            .post(request_body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        if(!response.isSuccessful())
                        {
                            throw new IOException("Error"+response);
                        }
                        progress.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            t.start();
        }

    }
    private String getMimeType(String path)
    {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

    }
    private void showError() {
        Toast.makeText(getApplicationContext(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }
    public void displayAlert(String message) {
        builder.setTitle("attenton");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_title.setText("");
                et_description.setText("");
                showfile.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            builder.setTitle("Attention");
            builder.setMessage("Do You Want To Exit?");
            builder.setCancelable(true);

            builder.setPositiveButton("YES, Exit",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("NO, i don't", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog mydialog=builder.create();
            mydialog.show();
        }
    }
}
