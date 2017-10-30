package com.example.foysal.noticeboardextend;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;


public class Allfiles extends Fragment {

    String discipline,batch;
    int userId;
    private ProgressDialog progressdialog;

    String file_url = "http://notify.dgted.com/notice/showfiles.php";
    String download_url = "http://notify.dgted.com/notice/images/";

    ListAdapter listAdapter;
    ListView listView;

    public Allfiles() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_allfiles, container, false);
        listView=(ListView)v.findViewById(R.id.list);

        ProfileMainActivity activity = (ProfileMainActivity) getActivity();
        discipline = activity.getDiscipline();
        batch=activity.getBatch();
        userId=activity.getUserId();

        StringRequest stringReques = new StringRequest(com.android.volley.Request.Method.POST,file_url,new com.android.volley.Response.Listener<String>()
        {
            public void onResponse(final String response)
            {
                try
                {
                    ArrayList<String> list=new ArrayList<String>();
                    final JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String code = jsonObject.getString("File");
                        if(code.equals("null")||code.equals("NULL"))
                        {

                        }
                        else
                        {
                            list.add(code);
                        }
                    }
                    listAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list)
                    {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    listView.setAdapter(listAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String file=String.valueOf(parent.getItemAtPosition(position));
                            initialize(file);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Dicipline",discipline);
                params.put("Batch", batch);
                params.put("UserId",String.valueOf(userId));
                return params;
            }
        };
        Mysingleton.getInstance(getContext()).addToRequestque(stringReques);

        return  v;
    }
    public void initialize(final String file)
    {
        progressdialog = new ProgressDialog(getContext());
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
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(),file);
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

    private boolean writeResponseBodyToDisk(ResponseBody body, String file) {
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
