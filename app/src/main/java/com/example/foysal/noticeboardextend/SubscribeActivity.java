package com.example.foysal.noticeboardextend;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubscribeActivity extends AppCompatActivity {

    String code;
    String dicipline,batch,isAdmin;
    String subs = "http://notify.dgted.com/notice/subscription.php";
    //previously subscribed
    String getlist = "http://notify.dgted.com/notice/subscribed.php";
    //unsubscribe
    String unsub_url = "http://notify.dgted.com/notice/unsubscribe.php";

    ListView listView;
    String[] sub={"17","16","15","14","13","12"};
    String select;
    TextView textView;
    int userId=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        textView=(TextView)findViewById(R.id.text);

        final Intent i=getIntent();
        Bundle b=i.getExtras();
        userId=b.getInt("UserId");
        dicipline=b.getString("Discipline");
        batch=b.getString("Batch");
        isAdmin=b.getString("Admin");
        //previously subscribed
        StringRequest request=new StringRequest(Request.Method.POST, getlist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    int length=jsonArray.length();
                    code="The Batch u already subscribe\n";
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        code+=jsonObject.getString("Batch")+"\n";
                    }
                    textView.setText(code);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error in getting List",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String>params=new HashMap<String,String>();
                params.put("UserId", String.valueOf(userId));
                return params;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestque(request);


        listView=(ListView)findViewById(R.id.subscribe);
        ListAdapter list=new ArrayAdapter<String>(SubscribeActivity.this,android.R.layout.simple_list_item_1,sub)
        {
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        listView.setAdapter(list);
        registerForContextMenu(listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                select=String.valueOf(parent.getItemAtPosition(position));
                return false;
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v,menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0,v.getId(),0,"Subscribe");
        menu.add(0,v.getId(),0,"Unsubscribe");
    }
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle()=="Subscribe")
        {
            //subscribe
            StringRequest imnport=new StringRequest(Request.Method.POST, subs, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {

                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String code=jsonObject.getString("message");
                        if(code.equals("You are alredy subscribe this batch"))
                        {
                            Toast.makeText(SubscribeActivity.this,code, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent i=getIntent();
                            SubscribeActivity.this.finish();
                            SubscribeActivity.this.startActivity(i);
                            overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            })
            {
                protected Map<String,String>getParams()throws AuthFailureError
                {
                    Map<String,String>params=new HashMap<String, String>();
                    params.put("Batch",select);
                    params.put("UserId", String.valueOf(userId));
                    return params;
                }
            };
            Mysingleton.getInstance(SubscribeActivity.this).addToRequestque(imnport);
        }
        if(item.getTitle()=="Unsubscribe")
        {
            StringRequest stringReques = new StringRequest(Request.Method.POST, unsub_url, new Response.Listener<String>()
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
                            Intent i=getIntent();
                            SubscribeActivity.this.finish();
                            SubscribeActivity.this.startActivity(i);
                            overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                //pushing data in php
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Batch",select);
                    params.put("UserId", String.valueOf(userId));
                    return params;
                }
            };
            Mysingleton.getInstance(SubscribeActivity.this).addToRequestque(stringReques);
        }
        return true;
    }
    public void onBackPressed() {
        Intent newintent=new Intent(SubscribeActivity.this,UserHomeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("UserId",userId);
        bundle.putString("Discipline",dicipline);
        bundle.putString("Batch",batch);
        bundle.putString("Admin",isAdmin);
        newintent.putExtras(bundle);
        SubscribeActivity.this.finish();
        SubscribeActivity.this.startActivity(newintent);
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
    }
}
