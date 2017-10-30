package com.example.foysal.noticeboardextend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApprovedFragment extends Fragment {

    String newNotice_url="http://notify.dgted.com/notice/Approved.php";
    private List<Notice> NoticeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoticeAdapter mAdapter;

    String discipline,batch;
    int userId;

    public ApprovedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_approved, container, false);

        AdminActivity activity = (AdminActivity) getActivity();
        discipline = activity.getdiscipline();
        batch=activity.getBatch();
        userId=activity.getUserId();

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new NoticeAdapter(NoticeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

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
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));
        //for getting data
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
                        String des=jsonObject.getString("Description");
                        String tit=jsonObject.getString("Title");
                        String fn=jsonObject.getString("FirstName");
                        String dat=jsonObject.getString("Date");
                        String bat=jsonObject.getString("Batch");
                        String fl=jsonObject.getString("File");
                        String f2=jsonObject.getString("ShowFile");
                        Notice Notice = new Notice(tit,des,"posted By: "+fn,dat,bat,fl,f2);

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
                Toast.makeText(getContext(),"server not response",Toast.LENGTH_LONG).show();
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
}