package com.weiyitech.zhaopinzh.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.weiyitech.zhaopinzh.presentation.activity.CampusActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.UniversityCampusActivity;
import com.weiyitech.zhaopinzh.struct.PreachingUniversityRsp;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-21
 * Time: 下午10:18
 * To change this template use File | Settings | File Templates.
 */
public class CampusListAdapter extends BaseAdapter {
    ArrayList<PreachingUniversityRsp> preachingUniversityRsps;
    CampusActivity activity;

    public static class ViewHolder {
        public LinearLayout listItem;
        public TextView timesTxtView;
        public ImageView logoImageView;
        public ImageView addNewImageView;
        public RatingBar ratingBar;
    }

    public CampusListAdapter(CampusActivity context, ArrayList<PreachingUniversityRsp> preachingUniversityRsps) {
        super();
        activity = context;
        this.preachingUniversityRsps = preachingUniversityRsps;
    }

    @Override
    public int getCount() {
        return preachingUniversityRsps.size();
    }

    @Override
    public Object getItem(int position) {
        return preachingUniversityRsps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.university_fragment_list_item, null);
            holder = new ViewHolder();
            holder.listItem = (LinearLayout) v.findViewById(R.id.campuse_activity_item);
            holder.timesTxtView = (TextView)v.findViewById(R.id.campus_activity_number);
            holder.logoImageView = (ImageView)v.findViewById(R.id.campus_activity_image_view);
            holder.addNewImageView = (ImageView)v.findViewById(R.id.campus_activity_add_new_image_view);
            holder.ratingBar = (RatingBar)v.findViewById(R.id.campus_activity_rating_bar);
            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();
        final PreachingUniversityRsp preachingUniversityRsp = preachingUniversityRsps.get(position);
        holder.timesTxtView.setText("近期宣讲会" + preachingUniversityRsp.preachCount + "场");
        if(preachingUniversityRsp.addNew){
            holder.addNewImageView.setVisibility(View.VISIBLE);
        } else {
            holder.addNewImageView.setVisibility(View.GONE);
        }

        holder.ratingBar.setRating(preachingUniversityRsp.rating);

        if(preachingUniversityRsp.university.contentEquals("南京大学")){
            holder.logoImageView.setImageResource(R.drawable.nju);
        }else if(preachingUniversityRsp.university.contentEquals("东南大学")){
            holder.logoImageView.setImageResource(R.drawable.seu);
        }else if(preachingUniversityRsp.university.contentEquals("南京理工大学")){
            holder.logoImageView.setImageResource(R.drawable.njust);
        }else if(preachingUniversityRsp.university.contentEquals("南京航空航天大学")){
            holder.logoImageView.setImageResource(R.drawable.nuaa);
        }else if(preachingUniversityRsp.university.contentEquals("河海大学")){
            holder.logoImageView.setImageResource(R.drawable.hhu);
        } else if(preachingUniversityRsp.university.contentEquals("南京师范大学")){
            holder.logoImageView.setImageResource(R.drawable.njnu);
        } else if(preachingUniversityRsp.university.contentEquals("南京农业大学")){
            holder.logoImageView.setImageResource(R.drawable.njau);
        } else if(preachingUniversityRsp.university.contentEquals("南京工业大学")){
            holder.logoImageView.setImageResource(R.drawable.njut);
        } else if(preachingUniversityRsp.university.contentEquals("南京财经大学")){
            holder.logoImageView.setImageResource(R.drawable.njue);
        } else if(preachingUniversityRsp.university.contentEquals("中国药科大学")){
            holder.logoImageView.setImageResource(R.drawable.cpu);
        }
        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView)v.findViewById(R.id.campus_activity_add_new_image_view)).setVisibility(View.GONE);
                Intent intent = new Intent(activity, UniversityCampusActivity.class);
                intent.putExtra("university", preachingUniversityRsp.university);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        return v;

    }
}
