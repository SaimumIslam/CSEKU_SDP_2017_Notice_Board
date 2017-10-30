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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostNoticeFragment extends Fragment {

    String discipline,batch,fileName="NULL";
    String title, description, type,FileName="NULL";
    int userId;
    boolean check=false;//file chooser check

    private EditText et_title, et_description,showfile;
    private Button post,file;

    MaterialSpinner spinner;

    AlertDialog.Builder builder;
    String upload_url = "http://notify.dgted.com/notice/image.php";
    //to post notice
    String post_url = "http://notify.dgted.com/notice/Post.php";

    public PostNoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_notice_post, container, false);

        UserHomeActivity activity = (UserHomeActivity) getActivity();
        discipline = activity.getDiscipline();
        batch=activity.getBatch();
        userId=activity.getUserId();


        et_title = (EditText)v.findViewById(R.id.Title);
        et_description = (EditText)v.findViewById(R.id.Description);
        showfile = (EditText)v.findViewById(R.id.Fshow);
        post = (Button)v.findViewById(R.id.Post);
        file=(Button)v.findViewById(R.id.Filechooser);
        builder = new AlertDialog.Builder(getContext());
        if(activity.getIsAdmin().equals("true"))
        {
            file.setVisibility(View.VISIBLE);
            showfile.setVisibility(View.VISIBLE);
        }
        //for the drop dwon text suggestion

        List<String> list=new ArrayList<>();
        list.add("Only my batch");
        list.add("For all");

        spinner = (MaterialSpinner) v.findViewById(R.id.Type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=-1)//hint
                {
                    type=spinner.getItemAtPosition(i).toString();
                }
                else
                {
                    type=spinner.getItemAtPosition(0).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                type=spinner.getItemAtPosition(0).toString();

            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermissionsAndOpenFilePicker();
                check=true;
                view.setBackgroundResource(R.color.colorPrimary);            }
        });
        //work of post button
        post.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        title = et_title.getText().toString();
                                        description = et_description.getText().toString();
                                        FileName = showfile.getText().toString();

                                        if(FileName.equals("")&&check)
                                        {
                                            builder.setTitle("Please set the file name");
                                            displayAlert("Fill title ,description, notice type");
                                        }
                                        if (title.equals("") || description.equals("") || type.equals(""))
                                        {
                                            builder.setTitle("Please fill up all again");
                                            displayAlert("Fill title ,description, notice type");
                                        } else {
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
                                                            Log.d("post", "else");
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
                                                    params.put("NoticeType", type);
                                                    params.put("FileName",fileName);
                                                    params.put("ShowName",FileName);
                                                    params.put("Discipline",discipline);
                                                    params.put("UserId", String.valueOf(userId));
                                                    return params;
                                                }
                                            };
                                            Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);
                                        }
                                    }
                                }
        );
        return v;
    }
    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission},100);
            }
        } else {
            enable_file();
        }
    }
    private void enable_file() {
        new MaterialFilePicker()
                .withSupportFragment(this)
                .withRequestCode(10)
                .withHiddenFiles(true)
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
            progress=new ProgressDialog(getContext());
            progress.setTitle("uplaoding");
            progress.setMessage("please wait");
            progress.show();

            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    File f=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String Content_type=getMimeType(f.getPath());
                    fileName=f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\")+1);
                    fileName=fileName.substring(fileName.lastIndexOf("/")+1);
                    String file_path=f.getAbsolutePath();
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
        Toast.makeText(getContext(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }
    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_title.setText("");
                et_description.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
