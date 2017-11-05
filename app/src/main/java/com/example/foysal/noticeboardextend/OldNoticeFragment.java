package com.example.foysal.noticeboardextend;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
public class OldNoticeFragment extends Fragment {



    String newNotice_url="http://notify.dgted.com/notice/OldNotice.php";
    String favorite= "http://notify.dgted.com/notice/Favorite.php";

    private List<Notice> NoticeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoticeAdapter mAdapter;


    String discipline,batch,Seen,fav,noticeId;
    int userId;

    public OldNoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_old_notice, container, false);

        UserHomeActivity activity = (UserHomeActivity) getActivity();
        discipline = activity.getDiscipline();
        batch=activity.getBatch();
        userId=activity.getUserId();
        Seen="seen"+discipline;
        fav=discipline+"fav";


        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new NoticeAdapter(NoticeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

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

                if (bat.equals("varsitynotice"))
                {
                    Seen="seenvarsity";
                    fav="varsityfav";
                }
                else if(bat.equals("discipline"))
                {
                    Seen="disciplineseen";
                    fav="disciplinefav";
                }
                else
                {
                    Seen="seen"+discipline;
                    fav=discipline+"fav";
                }

                final Intent sub=new Intent(getContext(),SeenActivity.class);
                view.setBackgroundResource(R.color.colorAccent);
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
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left);

            }

            @Override
            public void onLongClick(View view, int position) {
                Notice notice = NoticeList.get(position);
                noticeId = notice.getNoticeId();
            }
        }));
        //for getting data
        database();

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
    public void database()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,newNotice_url, new com.android.volley.Response.Listener<String>()
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

                        String id=jsonObject.getString("NoticeId");
                        String des=jsonObject.getString("Description");
                        String tit=jsonObject.getString("Title");
                        String fn=jsonObject.getString("FirstName");
                        String dat=jsonObject.getString("Date");
                        String bat=jsonObject.getString("Batch");
                        String fl=jsonObject.getString("File");
                        String f2=jsonObject.getString("ShowFile");
                        Notice Notice = new Notice(tit,des,"Posted By: "+fn,id,dat,bat,fl,f2);
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
                Toast.makeText(getContext(),"Server not response",Toast.LENGTH_LONG);
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Dicipline",discipline);
                params.put("Batch", batch);
                params.put("Seen","seen"+discipline);
                params.put("UserId",String.valueOf(userId));
                return params;
            }
        };
        Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v,menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0,v.getId(),0,"Favorite");
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle()=="Favorite")
        {
            StringRequest stringReques = new StringRequest(Request.Method.POST,favorite, new Response.Listener<String>()
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
                            Toast.makeText(getContext(),"this is add on fav",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
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
                    params.put("Fav",fav);
                    params.put("UserId", String.valueOf(userId));
                    return params;
                }
            };
            Mysingleton.getInstance(getContext()).addToRequestque(stringReques);
        }
        return true;
    }
}

