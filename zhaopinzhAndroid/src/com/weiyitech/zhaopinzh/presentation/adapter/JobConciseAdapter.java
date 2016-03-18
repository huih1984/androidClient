package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.activity.JobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.JobConciseRsp;
import com.weiyitech.zhaopinzh.struct.RunEmployer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-4-12
 * Time: 下午1:42
 * To change this template use File | Settings | File Templates.
 */
public class JobConciseAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<JobConciseRsp> jobConciseRsps;
    int excludeIndex = 0;
    String fromWhere;
    RunEmployer runEmployer;
    public JobConciseAdapter(Activity activity, ArrayList<JobConciseRsp> jobConciseRsps, RunEmployer runEmployer, int excludeIndex, String fromWhere) {
        super();
        this.activity = activity;
        this.jobConciseRsps = jobConciseRsps;
        this.runEmployer = runEmployer;
        this.excludeIndex = excludeIndex;
        this.fromWhere = fromWhere;
    }

    @Override
    public int getCount() {
        return jobConciseRsps.size() - 1;
    }

    @Override
    public Object getItem(int position) {
        if(position < excludeIndex){
            return  jobConciseRsps.get(position);
        }else{
            return jobConciseRsps.get(position + 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public View listItem;
        public TextView jobNameTxtView;
        public TextView timesTxtView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.job_fragment_list_item_expand_item, null);
            holder = new ViewHolder();
            holder.listItem = v;
            holder.jobNameTxtView = (TextView)v.findViewById(R.id.job_name_text_view);
            holder.timesTxtView = (TextView)v.findViewById(R.id.recuitment_text_view);
            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();
        JobConciseRsp jobConciseRsp;
        if(position < excludeIndex){
            jobConciseRsp = jobConciseRsps.get(position);
        }else{
            jobConciseRsp = jobConciseRsps.get(position + 1);
        }
        holder.jobNameTxtView.setText(jobConciseRsp.jobName);
        holder.timesTxtView.setText("招聘人数：" + (jobConciseRsp.demandNumber == 0 ? "不限":jobConciseRsp.demandNumber));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, JobDetailActivity.class);
                intent.putExtra("where", fromWhere);
                intent.putParcelableArrayListExtra("job_consice_rsp_list", jobConciseRsps);
                int index = position < excludeIndex ? position:position + 1;
                intent.putExtra("position", index);
                intent.putExtra("run_employer", runEmployer);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        return v;
    }
}
