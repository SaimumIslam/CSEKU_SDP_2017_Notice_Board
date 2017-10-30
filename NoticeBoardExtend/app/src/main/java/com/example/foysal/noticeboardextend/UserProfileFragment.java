package com.example.foysal.noticeboardextend;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {


    private static EditText et_title, et_description;
    private static Button update;
    private static MaterialSpinner spinner;
    private LinearLayout editLayout;

    private List<Notice> NoticeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoticeAdapter mAdapter;

    String title, description, type,fileName,isAdmin;
    String discipline,batch,noticeId;
    int userId;


    String[] Notice_Type = {"select your notice type","Only my batch","For all"};
    AlertDialog.Builder builder;
    //the post _writer
    String writer_url = "http://notify.dgted.com/notice/OwnNotice.php";
    //edit the post;
    String update_url = "http://notify.dgted.com/notice/edit.php";
    String delete_file = "http://notify.dgted.com/notice/images/file_delete.php";
    //delete the post
    String delete_url = "http://notify.dgted.com/notice/delete.php";

    public UserProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_user_profile, container, false);

        ProfileMainActivity activity = (ProfileMainActivity) getActivity();
        discipline = activity.getDiscipline();
        batch=activity.getBatch();
        userId=activity.getUserId();
        isAdmin=activity.getIsAdmin();

        editLayout=(LinearLayout)v.findViewById(R.id.edit);
        et_title = (EditText) v.findViewById(R.id.Title);
        et_description = (EditText) v.findViewById(R.id.Description);
        update = (Button) v.findViewById(R.id.Post);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new NoticeAdapter(NoticeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));


        builder = new AlertDialog.Builder(getContext());
        //for the drop dwon text suggestion
        List<String> list=new ArrayList<>();
        list.add("Only my batch");
        list.add("For all");
        spinner = (MaterialSpinner)v.findViewById(R.id.Type);
        ArrayAdapter adapter1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=-1)//hint
                {
                    type=spinner.getItemAtPosition(i).toString();
                }
                else
                {
                    spinner.setError("Please select your type");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type=spinner.getItemAtPosition(0).toString();

            }
        });
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
                        params.put("NoticeType", type);
                        params.put("NoticeId",noticeId);
                        params.put("Discipline",discipline);
                        return params;
                    }
                };
                Mysingleton.getInstance(getContext()).addToRequestque(stringReques);

            }
        });
        //request for seen own notice
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
                String f2=notice.getShowF();

                final Intent sub=new Intent(getContext(),SeenActivity.class);
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
                getContext().startActivity(sub);
            }

            @Override
            public void onLongClick(View view, int position)
            {
                Notice notice=NoticeList.get(position);
                noticeId=notice.getNoticeId();
                String tit=notice.getTitle();
                String des=notice.getdescription();
                fileName=notice.getFile();

                type=notice.getBatch();//here notice type send as batch;

                et_title.setText(tit);
                et_description.setText(des);
                if(type.equals("Only my batch"))
                    spinner.setSelection(1);
                else if(type.equals("For all"))
                    spinner.setSelection(2);
                else
                    spinner.setSelection(1);
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
                        String bat="My Batch";
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
                Toast.makeText(getContext(),"server not response",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("UserId",String.valueOf(userId));
                params.put("Discipline",discipline);
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
                                    Intent in=new Intent(getContext(),ProfileMainActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("UserId",userId);
                                    bundle.putString("discipline",discipline);
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
                    params.put("Seen","seen"+discipline);
                    params.put("Discipline",discipline);
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
                et_title.setText("");
                et_description.setText("");
                Intent in=new Intent(getContext(),ProfileMainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("UserId",userId);
                bundle.putString("discipline",discipline);
                bundle.putString("Batch",batch);
                bundle.putString("Admin",isAdmin);
                in.putExtras(bundle);
                getActivity().finish();
                getContext().startActivity(in);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
