package com.example.foysal.noticeboardextend;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ProfileMainActivity extends AppCompatActivity {

    String discipline, batch,isAdmin;
    int userId;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter viewPagerAdapter;

    public String getIsAdmin() {
        return isAdmin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        final Intent i = getIntent();
        Bundle b = i.getExtras();
        userId = b.getInt("UserId");
        discipline = b.getString("Discipline");
        batch = b.getString("Batch");
        isAdmin=b.getString("Admin");

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new UserProfileFragment(), "My notice");
        viewPagerAdapter.addFragments(new Allfiles(), "All files");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public String getDiscipline() {
        return discipline;
    }

    public String getBatch() {
        return batch;
    }

    public int getUserId() {
        return userId;
    }

    public void onBackPressed() {
        Intent newintent = new Intent(this, UserHomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("UserId", userId);
        bundle.putString("Discipline", discipline);
        bundle.putString("Batch", batch);
        bundle.putString("Admin",isAdmin);
        newintent.putExtras(bundle);
        this.finish();
        this.startActivity(newintent);
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
    }
}