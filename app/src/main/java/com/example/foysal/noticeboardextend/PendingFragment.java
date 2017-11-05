package com.example.foysal.noticeboardextend;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment {


    String pendingNotice_url="http://notify.dgted.com/notice/pending.php";

    //delete the post
    String delete_url = "http://notify.dgted.com/notice/delete.php";
    String approval = "http://notify.dgted.com/notice/GiveApproval.php";
    String delete_file = "http://notify.dgted.com/notice/images/file_delete.php";

    private List<Notice> NoticeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoticeAdapter mAdapter;

    AlertDialog.Builder builder;

    String discipline,batch,fileName,isAdmin;
    int userId;
    String noticeId;

    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_pending, container, false);


        AdminActivity activity = (AdminActivity) getActivity();
        discipline = activity.getdiscipline();
        batch=activity.getBatch();
        userId=activity.getUserId();
        isAdmin=activity.getIsAdmin();
        
        builder = new AlertDialog.Builder(getContext());

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new NoticeAdapter(NoticeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        //for contex menu
        registerForContextMenu(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
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
                String Sfile=notice.getShowF();

                final Intent sub=new Intent(getContext(),SeenActivity.class);
                view.setBackgroundResource(R.color.click);
                Bundle bundl=new Bundle();
                bundl.putString("Title",tit);
                bundl.putString("Description",des);
                bundl.putString("FirstName",noticeWriter);
                bundl.putString("Date",dat);
                bundl.putString("Batch",bat);
                bundl.putString("File",file);
                bundl.putString("ShowFile",Sfile);
                sub.putExtras(bundl);
                getContext().startActivity(sub);
            }

            @Override
            public void onLongClick(View view, int position)
            {
                Notice notice=NoticeList.get(position);
                noticeId=notice.getNoticeId();
                fileName=notice.getFile();

            }
        }));
        //for getting data
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,pendingNotice_url, new com.android.volley.Response.Listener<String>()
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
                        String fn=jsonObject.getString("FirstName");
                        String nId=jsonObject.getString("NoticeId");
                        String dat=jsonObject.getString("Date");
                        String bat=jsonObject.getString("Batch");
                        String f1=jsonObject.getString("File");
                        String f2=jsonObject.getString("ShowFile");
                        Notice Notice = new Notice(tit,des,"Posted By: "+fn,nId,dat,bat,f1,f2);
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
                Toast.makeText(getContext(),"server not response",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Discipline",discipline);
                params.put("Batch", batch);
                params.put("UserId",String.valueOf(userId));
                return params;
            }
        };
        Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);

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
        return v;
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v,menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0,v.getId(),0,"Approved");
        menu.add(0,v.getId(),0,"Delete");
    }
    public boolean onContextItemSelected(MenuItem item)
    {

        if(item.getTitle()=="Approved")
        {
            StringRequest stringReques = new StringRequest(Request.Method.POST,approval, new Response.Listener<String>()
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
                            Intent in=new Intent(getContext(),AdminActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putInt("UserId",userId);
                            bundle.putString("Discipline",discipline);
                            bundle.putString("Batch",batch);
                            bundle.putString("Admin",isAdmin);
                            in.putExtras(bundle);
                            getActivity().finish();
                            getContext().startActivity(in);
                            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_out, R.anim.fade_in);

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
                    params.put("NoticeId",noticeId);
                    params.put("Discipline",discipline);
                    return params;
                }
            };
            Mysingleton.getInstance(getContext()).addToRequestque(stringReques);
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
                                    Intent in=new Intent(getContext(),AdminActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("UserId",userId);
                                    bundle.putString("Discipline",discipline);
                                    bundle.putString("Batch",batch);
                                    bundle.putString("Admin",isAdmin);
                                    in.putExtras(bundle);
                                    getActivity().finish();
                                    getContext().startActivity(in);
                                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_out, R.anim.fade_in);

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
                            Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);
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
                    params.put("NoticeId",noticeId);
                    params.put("fav",discipline+"fav");
                    params.put("Discipline","seen"+discipline);
                    params.put("seen",discipline);
                    return params;
                }
            };
            Mysingleton.getInstance(getContext()).addToRequestque(stringReques);
        }
        return true;
    }
    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
