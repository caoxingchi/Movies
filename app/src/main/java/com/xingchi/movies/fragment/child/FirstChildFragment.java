package com.xingchi.movies.fragment.child;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.xingchi.movies.R;
import com.xingchi.movies.adapter.ItemSubjectAdapter;
import com.xingchi.movies.pojo.MoviesRoot;
import com.xingchi.movies.pojo.Subjects;
import com.xingchi.movies.utils.MyApplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstChildFragment extends Fragment{

    private static final String TAG = "FirstChildFragment";
    private Button loadData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ItemSubjectAdapter itemSubjectAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_child_fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onCreateView: "+TAG);
        initView(view);
        initData(view);

    }

    public void initView(View view){
       // loadData = (Button) view.findViewById(R.id.loadData);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    public void initData(final View view){
        //loadData.setOnClickListener(this);
        itemSubjectAdapter = new ItemSubjectAdapter(ss,FirstChildFragment.this.getActivity());
        LinearLayoutManager llm=new LinearLayoutManager(FirstChildFragment.this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //格子布局
        GridLayoutManager glm=new GridLayoutManager(FirstChildFragment.this.getActivity(),2);

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(itemSubjectAdapter);
        //下拉加载数据
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemSubjectAdapter.upDown=0;
                volley();
            }
        });

        /**
         * 上划监听，底部加载数据
         * newState ==Scroll_STATE_IDLE &&
         *
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleTtem;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                      if((newState==recyclerView.SCROLL_STATE_IDLE)&&
                        (lastVisibleTtem+2>itemSubjectAdapter.getItemCount())&&
                        (itemSubjectAdapter.getItemCount()-1<moviesRoot.getTotal())){
                          //底部加载数据
                          itemSubjectAdapter.upDown=1;
                            volley();
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //最后一行显示的数据，先获得布局管理器
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleTtem=llm.findLastVisibleItemPosition();
            }
        });
        volley();
    }


    /**
     * 网络加载数据框架方法
     */
    int start=0;
    List<Subjects> ss=new ArrayList<>();
    MoviesRoot moviesRoot;
    //{"start":0,"count":1}
    public void volley(){
        String url="https://api.douban.com/v2/movie/in_theaters?apikey=0df993c66c0c636e29ecbb5344252a4a";
        Log.i(TAG, "volley: "+url);
        String data = "{\"start\":" + start + ",\"count\":2}";
        Log.i(TAG, "volley: "+data);

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.GET,
                url,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: "+response.toString());
                        Gson gson=new Gson();
                        moviesRoot=gson.fromJson(response.toString(),MoviesRoot.class);
                        start = moviesRoot.getStart()+moviesRoot.getCount();
                        List<Subjects> s=moviesRoot.getSubjects();
                        Log.i(TAG, "onResponse: "+start);
                        ss=moviesRoot.getSubjects();
                        itemSubjectAdapter.loadMoreData(ss);
                        //加载完毕后关闭刷新
                        swipeRefreshLayout.setRefreshing(false);

                    }
                },     new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: "+error.toString());
                //加载失败后关闭刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        MyApplication.addRequest(jsonObjectRequest,TAG);

    }
}
