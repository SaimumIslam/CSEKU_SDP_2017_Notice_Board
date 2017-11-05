package com.example.foysal.noticeboardextend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String discipline,batch,isAdmin;
    int userId;
    static String userName;

    String admin_url = "http://notify.dgted.com/notice/Checkadmin.php";

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter viewPagerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent i=getIntent();
        Bundle b=i.getExtras();

        userId=b.getInt("UserId");
        discipline=b.getString("Discipline");
        batch=b.getString("Batch");
        userName=b.getString("Name");
        isAdmin=b.getString("Admin");

        if(isAdmin.equals("true"))
        {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.my_admin).setVisible(true);
        }
        tabLayout =(TabLayout) findViewById(R.id.tabLayout);
        viewPager =(ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new PostNoticeFragment(), "Create Post");
        viewPagerAdapter.addFragments(new NewNoticeFragment(), "New Post");
        viewPagerAdapter.addFragments(new OldNoticeFragment(), "Old Post");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.inflateHeaderView(R.layout.nav_header_user_home);
        TextView test = (TextView) header.findViewById(R.id.textView);
        test.setText(userName);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(UserHomeActivity.this);
            exitbuilder.setTitle("Attention");
            exitbuilder.setMessage("Do You Want To Exit?");
            exitbuilder.setCancelable(true);

            exitbuilder.setPositiveButton("YES, Exit",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            exitbuilder.setNegativeButton("NO, i don't", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog mydialog=exitbuilder.create();
            mydialog.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.fragment_myprofile) {

            Intent in=new Intent(UserHomeActivity.this,ProfileMainActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("UserId",userId);
            bundle.putString("Discipline",discipline);
            bundle.putString("Batch",batch);
            bundle.putString("Admin",isAdmin);
            in.putExtras(bundle);
            UserHomeActivity.this.finish();
            UserHomeActivity.this.startActivity(in);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            // Handle the camera action
        } else if (id == R.id.my_admin) {

            Intent intent=new Intent(UserHomeActivity.this,AdminActivity.class);
            Bundle bun=new Bundle();
            bun.putInt("UserId",userId);
            bun.putString("Discipline",discipline);
            bun.putString("Batch",batch);
            bun.putString("Admin",isAdmin);
            intent.putExtras(bun);
            UserHomeActivity.this.finish();
            UserHomeActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);


        } else if (id == R.id.activity_subscribe) {

            Intent intent=new Intent(UserHomeActivity.this,SubscribeActivity.class);
            Bundle bun=new Bundle();
            bun.putInt("UserId",userId);
            bun.putString("Discipline",discipline);
            bun.putString("Batch",batch);
            bun.putString("Admin",isAdmin);
            intent.putExtras(bun);
            UserHomeActivity.this.finish();
            UserHomeActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        } else if (id == R.id.log_out) {

            Session session=new Session(UserHomeActivity.this);
            session.removeUser();
            Intent inten=new Intent(UserHomeActivity.this,LoginActivity.class);
            UserHomeActivity.this.finish();
            UserHomeActivity.this.startActivity(inten);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public String  getDiscipline(){
        return discipline;
    }
    public String getBatch() {
        return batch;
    }

    public int getUserId() {
        return userId;
    }
    public String getIsAdmin() {
        return isAdmin;
    }
}
