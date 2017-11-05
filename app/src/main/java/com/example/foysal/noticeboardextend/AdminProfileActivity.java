package com.example.foysal.noticeboardextend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminProfileActivity extends AppCompatActivity {

    int  AdminId,SuperAdminId,Id;
    String name,noticeId;

    private static EditText et_title, et_description;
    private static Button update;
    private static android.widget.Spinner spinner;
    private LinearLayout editLayout;

    private List<Notice> NoticeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoticeAdapter mAdapter;

    String title, description,Seen,table,fileName;

    String[] Notice_Type = {"select your notice type","Only my batch","For all"};
    AlertDialog.Builder builder;
    //the post _writer
    String writer_url = "http://notify.dgted.com/notice/varsityAdmin.php";;
    //edit the post;
    String update_url = "http://notify.dgted.com/notice/edit.php";
    String delete_file = "http://notify.dgted.com/notice/images/file_delete.php";
    //delete the post
    String delete_url = "http://notify.dgted.com/notice/delete.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        final Intent i=getIntent();
        Bundle b=i.getExtras();
        AdminId=b.getInt("AdminId");
        name=b.getString("Name");
        SuperAdminId=b.getInt("SuperAdminId");
        if(AdminId==-1)
        {
            Seen="seenvarsity";
            Id=SuperAdminId;
            table="varsitynotice";
        }
        else
        {
            Seen="disciplineseen";
            Id=AdminId;
            table="discipline";
        }

        editLayout=(LinearLayout)findViewById(R.id.edit);
        et_title = (EditText)findViewById(R.id.Title);
        et_description = (EditText)findViewById(R.id.Description);
        update = (Button) findViewById(R.id.Post);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new NoticeAdapter(NoticeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));


        builder = new AlertDialog.Builder(getApplicationContext());
        //for the drop dwon text suggestion
        spinner = (Spinner)findViewById(R.id.Type);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Notice_Type);
        spinner.setAdapter(adapter);
        //work of edit button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = et_title.getText().toString();
                description = et_description.getText().toString();
                //edit request
                StringRequest stringReques = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>()
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
                                editLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
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
                        params.put("Title", title);
                        params.put("Description", description);
                        params.put("NoticeId",noticeId);
                        params.put("Discipline",table);
                        return params;
                    }
                };
                Mysingleton.getInstance(getApplicationContext()).addToRequestque(stringReques);

            }
        });
        //request for seen own notice
        registerForContextMenu(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Notice notice=NoticeList.get(position);
                String des=notice.getdescription();
                String tit=notice.getTitle();
                String noticeWriter=notice.getnoticeWriter();
                String dat=notice.getDate();
                String bat=notice.getBatch();
                String file=notice.getFile();
                String f2=notice.getShowF();

                final Intent sub=new Intent(AdminProfileActivity.this,SeenActivity.class);
                view.setBackgroundResource(R.color.colorAccent);
                Bundle bundl=new Bundle();
                bundl.putString("Title",tit);
                bundl.putString("Description",des);
                bundl.putString("FirstName",noticeWriter);
                bundl.putString("Date",dat);
                bundl.putString("Batch",bat);
                bundl.putString("File",file);
                bundl.putString("ShowFile",f2);
                sub.putExtras(bundl);
                AdminProfileActivity.this.startActivity(sub);
            }

            @Override
            public void onLongClick(View view, int position)
            {
                Notice notice=NoticeList.get(position);
                noticeId=notice.getNoticeId();
                String tit=notice.getTitle();
                String des=notice.getdescription();
                fileName=notice.getFile();
            }
        }));
        //for getting data
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,writer_url, new com.android.volley.Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String des=jsonObject.getString("Description");
                        String tit=jsonObject.getString("Title");
                        String fn="Me";
                        String nId=jsonObject.getString("NoticeId");
                        String dat=jsonObject.getString("Date");
                        String bat="";
                        String noticeType=jsonObject.getString("NoticeType");
                        String fl=jsonObject.getString("File");
                        String f2=jsonObject.getString("ShowFile");
                        Notice Notice = new Notice(tit,des,"posted By: "+fn,nId,dat,noticeType,fl,f2);//notice type send as batch parameter
                        NoticeList.add(Notice);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"server not response",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("UserId",String.valueOf(Id));
                params.put("Discipline",table);
                return params;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        NoticeList.remove(viewHolder.getAdapterPosition());
                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v,menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0,v.getId(),0,"Edit");
        menu.add(0,v.getId(),0,"Delete");
    }
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle()=="Edit")
        {
            editLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        if(item.getTitle()=="Delete")
        {
            StringRequest stringReques = new StringRequest(Request.Method.POST, delete_url, new Response.Listener<String>()
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
                            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,delete_file, new com.android.volley.Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {
                                    Intent in=getIntent();
                                    AdminProfileActivity.this.finish();
                                    AdminProfileActivity.this.startActivity(in);
                                    overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
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
                                    params.put("file",fileName);
                                    return params;
                                }
                            };
                            Mysingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
                        }
                        else
                        {
                            displayAlert(jsonObject.getString("message"));
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
                    params.put("NoticeId",String.valueOf(Id));
                    params.put("Seen",Seen);
                    params.put("Discipline",table);
                    return params;
                }
            };
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(stringReques);
        }
        return true;
    }
    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_title.setText("");
                et_description.setText("");
                Intent in=getIntent();
                AdminProfileActivity.this.finish();
                AdminProfileActivity.this.startActivity(in);
                overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void onBackPressed() {
        Intent newintent=new Intent(AdminProfileActivity.this,AdminHomeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("AdminId",AdminId);
        bundle.putString("Name",name);
        bundle.putInt("SuperAdminId",SuperAdminId);
        newintent.putExtras(bundle);
        AdminProfileActivity.this.finish();
        AdminProfileActivity.this.startActivity(newintent);
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
    }
}
