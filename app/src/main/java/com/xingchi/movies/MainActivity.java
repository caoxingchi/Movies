package com.xingchi.movies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingchi.movies.fragment.FirstFragment;
import com.xingchi.movies.fragment.FourthFragment;
import com.xingchi.movies.fragment.SecondFragment;
import com.xingchi.movies.fragment.ThirdFragment;
import com.xingchi.movies.pojo.MoviesRoot;
import com.xingchi.movies.pojo.Subjects;
import com.xingchi.movies.utils.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //{"start":2,"count":2}
                String url="https://douban.uieee.com/v2/movie/in_theaters";
                final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        "{\"start\":0,\"count\":2}",
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int start=0;
                        Gson gson=new Gson();
                        MoviesRoot moviesRoot=gson.fromJson(response.toString(), MoviesRoot.class);
                        start = moviesRoot.getStart()+moviesRoot.getCount();
                        List<Subjects> s=moviesRoot.getSubjects();
                        Log.i(TAG, "onResponse: "+start);
                        Snackbar sb = Snackbar.make(view,"title:"+s.get(0).getTitle()+":: year:"+s.get(0).getYear(),Snackbar.LENGTH_SHORT);
                        View v = sb.getView();
                        TextView snackbar_text = (TextView)v.findViewById(R.id.snackbar_text);
                        snackbar_text.setTextColor(getResources().getColor(R.color.colorAccent));
                        sb.show();
                    }
                },     new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: "+error.toString());
                    }
                });

                MyApplication.addRequest(jsonObjectRequest,TAG);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        fm=this.getSupportFragmentManager();
        switchFragment("首页");
    }

    FragmentManager fm;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title =item.getTitle().toString();
        switchFragment(title);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String [] titles={"首页","收藏","top250","设置"};
    Fragment[] fs={new FirstFragment(),new SecondFragment(),new ThirdFragment(),new FourthFragment()};
    //临时保存正在显示的Fragment
    Fragment mFragment=new FirstFragment();
    /**
     * 切换fragment
     */
    public void switchFragment(String title){
        FragmentTransaction t=fm.beginTransaction();
        Fragment f=fm.findFragmentByTag(title);
        Log.e(TAG, "switchFragment: "+title);
        if(f!=null){
            t.hide(mFragment).show(f);
            mFragment=f;
            Log.i(TAG, "switchFragment: "+mFragment.getTag());
        }else{
            for(int i=0;i< titles.length;i++){
                if(titles[i].equals(title)){
                    t.hide(mFragment);
                    t.add(R.id.mainLayout,fs[i],title);
                    Log.i(TAG, "switchFragment: "+i+"fragment");
                    //保存正在被显示的Fragment
                    mFragment=fs[i];
                }
            }
        }
        t.commit();
        this.getSupportActionBar().setTitle(title);
    }
}
