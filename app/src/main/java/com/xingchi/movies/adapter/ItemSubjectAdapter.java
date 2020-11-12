package com.xingchi.movies.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xingchi.movies.R;
import com.xingchi.movies.pojo.Casts;
import com.xingchi.movies.pojo.Directors;
import com.xingchi.movies.pojo.Subjects;
import com.xingchi.movies.utils.MyApplication;

import java.util.List;

public class ItemSubjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Subjects> subjects=null;
    private Context context;
    private LayoutInflater ll;
    //上拉
    public int upDown=0;

    public ItemSubjectAdapter(List<Subjects> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
        this.ll=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==0){
            View v=ll.inflate(R.layout.item_simple_subject_layout,viewGroup,false);
            return new ItemViewHolder(v);
        }else{
            View v=ll.inflate(R.layout.foot_load_tips,viewGroup,false);
            return new FootViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position<subjects.size()){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return subjects.size()+1;
    }

    /**
     * 区分不同的ViewHolder。进行不同的创建和更新。重写getItemViewType来进行区分
     * @param itemViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemViewHolder, int i) {
        if(getItemViewType(i)==0){
            ((ItemViewHolder)itemViewHolder).update();
        }else{
            ((FootViewHolder)itemViewHolder).update();
        }
    }


    /**
     * 加载更多数据
     * @param subjects
     */

    public void loadMoreData(List<Subjects> subjects){
        if(upDown==0){
            subjects.addAll(this.subjects);
            this.subjects=subjects;
        }else{
            this.subjects.addAll(subjects);
        }

        this.notifyDataSetChanged();
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivItemSimpleSubjectImage;
        private LinearLayout llItemSimpleSubjectRating;
        private RatingBar rbItemSimpleSubjectRating;
        private TextView tvItemSimpleSubjectRating;
        private TextView tvItemSimpleSubjectCount;
        private TextView tvItemSimpleSubjectTitle;
        private TextView tvItemSimpleSubjectOriginalTitle;
        private TextView tvItemSimpleSubjectGenres;
        private TextView tvItemSimpleSubjectDirector;
        private TextView tvItemSimpleSubjectCast;
        
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemSimpleSubjectImage = (ImageView) itemView.findViewById(R.id.iv_item_simple_subject_image);
            llItemSimpleSubjectRating = (LinearLayout) itemView.findViewById(R.id.ll_item_simple_subject_rating);
            rbItemSimpleSubjectRating = (RatingBar) itemView.findViewById(R.id.rb_item_simple_subject_rating);
            tvItemSimpleSubjectRating = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_rating);
            tvItemSimpleSubjectCount = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_count);
            tvItemSimpleSubjectTitle = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_title);
            tvItemSimpleSubjectOriginalTitle = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_original_title);
            tvItemSimpleSubjectGenres = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_genres);
            tvItemSimpleSubjectDirector = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_director);
            tvItemSimpleSubjectCast = (TextView) itemView.findViewById(R.id.tv_item_simple_subject_cast);
        }

        public void update(){
           int position= this.getLayoutPosition();
           Subjects s=subjects.get(position);
           //标题
           tvItemSimpleSubjectTitle.setText(s.getTitle());
           tvItemSimpleSubjectOriginalTitle.setText(s.getOriginal_title());
           //评分星级
           rbItemSimpleSubjectRating.setRating((float) (s.getRating().getAverage()/2));
           tvItemSimpleSubjectRating.setText(""+s.getRating().getAverage());
           //评分人数
            tvItemSimpleSubjectCount.setText("("+s.getCollect_count()+")人评价");
            //genres
            StringBuffer geners=new StringBuffer();
            for (String g:s.getGenres()) {
                geners.append(g);
            }
            tvItemSimpleSubjectGenres.setText(geners);
            //导演
            StringBuffer directors=new StringBuffer();
            for (Directors d:s.getDirectors()) {
                directors.append(d.getName()+",");
            }
            tvItemSimpleSubjectDirector.setText(getSpannableString("导演:", Color.GRAY));
            tvItemSimpleSubjectDirector.append(directors);
            //演员
            StringBuffer casts=new StringBuffer();
            for (Casts c:s.getCasts()) {
                casts.append(c.getName()+",");
            }
            tvItemSimpleSubjectCast.setText(getSpannableString("演员:", Color.GRAY));
            tvItemSimpleSubjectCast.append(casts);
            ImageLoader.getInstance().displayImage(s.getImages().getLarge(),ivItemSimpleSubjectImage, MyApplication.getLoaderOptions());

        }
    }

    /**
     * 对字体的修改颜色
     * @param str
     * @param color
     * @return
     */
    public SpannableString getSpannableString(String str, int color) {
        SpannableString span = new SpannableString(str);
        span.setSpan(new ForegroundColorSpan(
                color), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    class FootViewHolder extends RecyclerView.ViewHolder{

        private ProgressBar pbViewLoadTip;
        private TextView tvViewLoadTip;

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            pbViewLoadTip = (ProgressBar) itemView.findViewById(R.id.pb_view_load_tip);
            tvViewLoadTip = (TextView) itemView.findViewById(R.id.tv_view_load_tip);

        }

        public void update(){
            pbViewLoadTip.setVisibility(View.VISIBLE);
        }
    }

}
