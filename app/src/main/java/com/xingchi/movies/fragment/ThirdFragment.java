package com.xingchi.movies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xingchi.movies.R;
import com.xingchi.movies.utils.MyApplication;


public class ThirdFragment extends Fragment {


    private static final String TAG = "ThirdFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.third_fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onCreateView: "+TAG);
        ImageView iv = view.findViewById(R.id.third_imageView);
        String uri="http://n.sinaimg.cn/sinacn/w660h990/20180222/6ce8-fyrswmu9023947.jpg";
        ImageLoader.getInstance().displayImage(uri,iv, MyApplication.getLoaderOptions());
    }

}
