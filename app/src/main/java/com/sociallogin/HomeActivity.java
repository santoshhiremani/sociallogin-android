package com.sociallogin;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sociallogin.managers.SharedPreferenceManager;
import com.sociallogin.model.UserMeta;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    SimpleDraweeView simpleDraweeView;

    TextView nameTextView;

    TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        UserMeta userMeta = getUserModelFromIntent();
        if(userMeta !=null)
            setDataOnNavigationView(userMeta);
    }

    private void setDataOnNavigationView(UserMeta userMeta) {
        if (navigationView != null) {
            setupDrawerContent(userMeta);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_sign_out:
                                drawerLayout.closeDrawers();
                                SharedPreferenceManager.getSharedInstance().clearAllPreferences();
                                startLoginActivity();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
    }

    private void setupDrawerContent(UserMeta userMeta) {
        View headerView = navigationView.getHeaderView(0);

        simpleDraweeView = ButterKnife.findById(headerView, R.id.user_imageview);
        simpleDraweeView.setImageURI(Uri.parse(userMeta.profilePic));

        nameTextView = ButterKnife.findById(headerView, R.id.name_textview);
        nameTextView.setText(userMeta.userName);

        emailTextView = ButterKnife.findById(headerView, R.id.email_textview);
        emailTextView.setText(userMeta.userEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSignOut(View view) {
        finish();
    }

    private UserMeta getUserModelFromIntent()
    {
        Intent intent = getIntent();
        return intent.getParcelableExtra(UserMeta.class.getSimpleName());
    }

    private void startLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
