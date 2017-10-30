package com.example.foysal.noticeboardextend;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperAdminActivity extends AppCompatActivity {



   private Button create;
    private EditText et_disName,et_ConfirmName,et_password, et_confirmPassword;
    private String discipline,confirmDiscipline, password, confirmPassword;
    private LinearLayout createLayout;
    private ListView LView,listview;

    private List<String> AdminList;
    private List<String> UserList;

    AlertDialog.Builder builder;
    ListAdapter listAdapter,list1;

    String discipline_url = "http://notify.dgted.com/notice/SignUpDisciplineAdmin.php";
    String create_url = "http://notify.dgted.com/notice/noticeTable.php";
    String seenCraeate_url = "http://notify.dgted.com/notice/seenTable.php";
    String Admin_url = "http://notify.dgted.com/notice/AdminUser.php";
    String user_url = "http://notify.dgted.com/notice/UserInDiscipline.php";
    String userAdminApproval = "http://notify.dgted.com/notice/SelectAdmin.php";
    String userAdminDelete = "http://notify.dgted.com/notice/DeleteAdmin.php";
    String DisAdmin_url = "http://notify.dgted.com/notice/DisciplineList.php";
    String favTab_url = "http://notify.dgted.com/notice/FavoriteTable.php";

    private int  AdminId,SuperAdminId,id;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);

        Intent i=getIntent();
        Bundle b=i.getExtras();
        AdminId=b.getInt("AdminId");
        name=b.getString("Name");
        SuperAdminId=b.getInt("SuperAdminId");


        listview = (ListView)findViewById(R.id.UserList);
        LView = (ListView)findViewById(R.id.AdminList);

        if(SuperAdminId==-1)
        {
            id=AdminId;
            UserDiscipline(name);
            getUserAdmin();
            registerForContextMenu(listview);
        }
        else
        {

            id=SuperAdminId;
            AdminDiscipline();
        }
        createLayout=(LinearLayout)findViewById(R.id.edit1);

        et_disName=(EditText)findViewById(R.id.DisName);
        et_ConfirmName=(EditText)findViewById(R.id.ConfirmName);
        et_password=(EditText)findViewById(R.id.Pass);
        et_confirmPassword=(EditText)findViewById(R.id.ConPass);

        discipline = et_disName.getText().toString().toLowerCase();
        confirmDiscipline = et_ConfirmName.getText().toString().toLowerCase();
        password = et_password.getText().toString();
        confirmPassword = et_confirmPassword.getText().toString();
        create=(Button)findViewById(R.id.Create);


      create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                discipline = et_disName.getText().toString().toLowerCase();
                confirmDiscipline = et_ConfirmName.getText().toString().toLowerCase();
                password = et_password.getText().toString();
                confirmPassword = et_confirmPassword.getText().toString();

                if (discipline.equals("") || confirmDiscipline.equals("") || password.equals("")|| confirmPassword.equals("")) {
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields...");
                    displayAlert("Create_failed");

                } else if (!password.equals(confirmPassword)) {
                    builder.setTitle("Password doesn't match....");
                    builder.setMessage("Please check confirm password.");
                    displayAlert("Password Not Match");
                }
                else if(!discipline.equals(confirmDiscipline))
                {
                    builder.setTitle("Don't u agree with our condition");
                    builder.setMessage("Please click on chckbox");
                    displayAlert("Discipline Name Does Not Match");
                }
                else
                {

                    createDiscipline();
                }
            }
        });
    }



    public void setDiscipline()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, discipline_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    builder.setTitle("server response");
                    builder.setMessage(message);
                    if(code.equals("Success"))
                    {
                        Intent intent=getIntent();
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
                    }
                    displayAlert(code);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Discipline",discipline);
                params.put("Password", password);
                return params;
            }
        };
        Mysingleton.getInstance(SuperAdminActivity.this).addToRequestque(stringRequest);
    }

    public void createDiscipline()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, create_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    builder.setTitle("server response");
                    builder.setMessage(message);
                    if(code.equals("Success"))
                    {
                        seenDiscipline();
                    }
                    displayAlert(code);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Discipline",discipline);
                return params;
            }
        };
        Mysingleton.getInstance(SuperAdminActivity.this).addToRequestque(stringRequest);
    }

    public void seenDiscipline()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, seenCraeate_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    builder.setTitle("server response");
                    builder.setMessage(message);
                    if(code.equals("Success"))
                    {
                        CreateFav();
                    }
                    displayAlert(code);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Discipline",discipline);
                params.put("Seen","seen"+discipline);
                return params;
            }
        };
        Mysingleton.getInstance(SuperAdminActivity.this).addToRequestque(stringRequest);
    }

    public void CreateFav()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, favTab_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    builder.setTitle("server response");
                    builder.setMessage(message);
                    if(code.equals("Success"))
                    {
                        setDiscipline();
                    }
                    displayAlert(code);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Discipline",discipline);
                params.put("Fav",discipline+"fav");
                return params;
            }
        };
        Mysingleton.getInstance(SuperAdminActivity.this).addToRequestque(stringRequest);
    }

    public void getUserAdmin()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Admin_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    AdminList = new ArrayList<String>();
                    JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String des = jsonObject.getString("Name");
                        AdminList.add(des);
                    }
                    listAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,AdminList)
                    {
                        public View getView(int position, View convertView, ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    LView.setAdapter(listAdapter);
                    LView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            String select=String.valueOf(parent.getItemAtPosition(position));
                            return false;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("AdminId",String.valueOf(AdminId));
                return params;
            }
        };
        Mysingleton.getInstance(SuperAdminActivity.this).addToRequestque(stringRequest);
    }

    public void UserDiscipline(final String Ename)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, user_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    UserList=new ArrayList<String>();
                    JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String des = jsonObject.getString("Name");
                        UserList.add(des);

                    }
                    list1=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,UserList)
                    {
                        public View getView(int position, View convertView, ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    listview.setAdapter(list1);
                    listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            String select=String.valueOf(parent.getItemAtPosition(position));
                            return false;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Discipline",Ename);
                return params;
            }
        };
        Mysingleton.getInstance(SuperAdminActivity.this).addToRequestque(stringRequest);
    }
    public void AdminDiscipline()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DisAdmin_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    AdminList=new ArrayList<String>();
                    JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String des = jsonObject.getString("DisciplineName");
                        AdminList.add(des);
                    }

                   listAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,AdminList)
                    {
                        public View getView(int position, View convertView, ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    LView.setAdapter(listAdapter);
                    LView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            String select=String.valueOf(parent.getItemAtPosition(position));
                            return false;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Discipline",name);
                return params;
            }
    };
        Mysingleton.getInstance(SuperAdminActivity.this).addToRequestque(stringRequest);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v,menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0,v.getId(),0,"Select Admin");
        menu.add(0,v.getId(),0,"Delete");
    }
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle()=="Select Admin")
        {
            //subscribe
            StringRequest imnport=new StringRequest(Request.Method.POST, userAdminApproval, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
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
                    params.put("UserId", String.valueOf(id));
                    return params;
                }
            };
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(imnport);
        }
        if(item.getTitle()=="Delete")
        {
            StringRequest imnport=new StringRequest(Request.Method.POST, userAdminDelete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
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
                    params.put("UserId", String.valueOf(id));
                    return params;
                }
            };
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(imnport);
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.superadmin, menu);

        return true;
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.createNewDiscipline);
        MenuItem select = menu.findItem(R.id.selectadmin);
        if(SuperAdminId==-1)
        {
            register.setVisible(false);
        }
        else
        {
            register.setVisible(true);
            select.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.createNewDiscipline) {
            createLayout.setVisibility(View.VISIBLE);
            LView.setVisibility(View.GONE);
            return true;
        }
        if (id == R.id.selectadmin) {
            createLayout.setVisibility(View.GONE);
            LView.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayAlert(final String code) {
        builder.setMessage(code);
        builder.setIcon(R.drawable.ic_menu_manage);
        builder.setCancelable(true);
        if (code.equals("Create_failed")) {
            et_disName.setText("");
            et_ConfirmName.setText("");
            et_password.setText("");
            et_confirmPassword.setText("");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(code.equals("Password Not Match"))
        {
            et_password.setText("");
            et_confirmPassword.setText("");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(code.equals("Discipline Name Does Not Match"))
        {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
    public void onBackPressed() {
        Intent newintent=new Intent(SuperAdminActivity.this,AdminHomeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("AdminId",AdminId);
        bundle.putString("Name",name);
        bundle.putInt("SuperAdminId",SuperAdminId);
        newintent.putExtras(bundle);
        SuperAdminActivity.this.finish();
        SuperAdminActivity.this.startActivity(newintent);
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
    }
}
