package com.example.foysal.noticeboardextend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegisterActivity extends AppCompatActivity {

    private  Button resister;
    private CheckBox checkBox;
    private EditText et_firstName, et_email, et_password, et_confirmPassword;
    private String firstName,email, password, confirmPassword, discipline="", batch="";


    MaterialSpinner s_batch,s_discipline;
    List<Integer> list=new ArrayList<>();
    ArrayAdapter<Integer> adapter;

    List<String> disciplinelist=new ArrayList<>();
    ArrayAdapter<String> adapter1;

    AlertDialog.Builder builder;
    //work of sign up
    String reg_url = "http://notify.dgted.com/notice/SignUpUser.php";

    String user_url = "http://notify.dgted.com/notice/DisciplineList.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        resister = (Button) findViewById(R.id.Register);
        et_firstName = (EditText) findViewById(R.id.Name);
        et_email = (EditText) findViewById(R.id.Email);
        et_password = (EditText) findViewById(R.id.Password);
        et_confirmPassword = (EditText) findViewById(R.id.ConfirmPassword);

        checkBox=(CheckBox)findViewById(R.id.Condition);

        setBatch();
        setDiscipline();
        s_batch = (MaterialSpinner) findViewById(R.id.Batch);
        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_batch.setAdapter(adapter);

        s_batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=-1)//hint
                {
                    batch=s_batch.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                s_batch.setError("Please select your batch");

            }
        });

        s_discipline= (MaterialSpinner) findViewById(R.id.Discipline);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,disciplinelist);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_discipline.setAdapter(adapter1);

        s_discipline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=-1)//hint
                {
                    discipline=s_discipline.getItemAtPosition(i).toString().toLowerCase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                s_batch.setError("Please select your discipline");

            }
        });

        builder = new AlertDialog.Builder(RegisterActivity.this);

        email = et_email.getText().toString();
        password = et_password.getText().toString();
        confirmPassword = et_confirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if(!TextUtils.isEmpty(email) && !isEmailValid(email))
        {
            et_password.setError(getString(R.string.error_invalid_password));
            focusView = et_password;
            cancel = true;
        }
        else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            et_password.setError(getString(R.string.error_invalid_password));
            focusView = et_password;
            cancel = true;
        }
        else if(!TextUtils.isEmpty(confirmPassword) && !isPasswordValid(confirmPassword))
        {
            et_password.setError(getString(R.string.error_invalid_password));
            focusView = et_password;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        resister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = et_firstName.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                confirmPassword = et_confirmPassword.getText().toString();
                if (firstName.equals("")  || email.equals("") || discipline.equals("")|| batch.equals("")) {
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields...");
                    displayAlert("Reg_failed");

                }else if (!password.equals(confirmPassword)) {
                    builder.setTitle("Password doesn't match....");
                    builder.setMessage("Please check confirm password.");
                    displayAlert("Password not match");
                }
                else if (password.length()< 4)
                {
                    builder.setTitle("Weak Pasword");
                    builder.setMessage("Please check password");
                    displayAlert("Password less than 4 digit");
                }
                else if(!checkBox.isChecked())
                {
                    builder.setTitle("Don't u agree with our condition");
                    builder.setMessage("Please click on chckbox");
                    displayAlert("Unchecked");
                }
                else {
                    //works of sign up
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                builder.setTitle("Success");
                                builder.setMessage(message);
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
                            builder.setTitle("Server not response");
                            Toast.makeText(RegisterActivity.this,"server not response",Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("FirstName", firstName);
                            params.put("Email", email);
                            params.put("Password", password);
                            params.put("Discipline", discipline);
                            params.put("Batch", batch);

                            return params;
                        }
                    };
                    Mysingleton.getInstance(RegisterActivity.this).addToRequestque(stringRequest);
                }
            }
        });
    }
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void displayAlert(final String code) {
        builder.setMessage(code);
        builder.setIcon(R.drawable.ic_menu_manage);
        builder.setCancelable(true);

        if (code.equals("Success"))
        {
            builder.setPositiveButton("Stay Here?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    et_firstName.setText("");
                    et_email.setText("");
                    et_password.setText("");
                    et_confirmPassword.setText("");
                }
            });

            builder.setNegativeButton("Want to SignIn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                    RegisterActivity.this.finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            });
            AlertDialog alertDialo = builder.create();
            alertDialo.show();

        }
        else if (code.equals("Reg_failed")) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_firstName.setText("");
                    et_email.setText("");
                    et_password.setText("");
                    et_confirmPassword.setText("");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(code.equals("Password not match"))
        {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_password.setText("");
                    et_confirmPassword.setText("");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(code.equals("Unchecked"))
        {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(code.equals("Password less than 4 digit"))
        {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_password.setText("");
                    et_confirmPassword.setText("");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    public void setBatch()
    {
        list.add(18);
        list.add(17);
        list.add(16);
        list.add(15);
        list.add(14);
        list.add(13);

    }
    /*
    public void  setDiscipline()
    {
        disciplinelist.add("CSE");
        disciplinelist.add("ECE");
        disciplinelist.add("MATH");
        disciplinelist.add("ARCH");
        disciplinelist.add("URP");
        disciplinelist.add("BAD");
        disciplinelist.add("FARMACY");
        disciplinelist.add("BGE");
        disciplinelist.add("ES");
    }
    */
    public void setDiscipline()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,user_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length-1;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String des = jsonObject.getString("DisciplineName");
                        disciplinelist.add(des);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Net problem pleasse restart ur app",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    }

}