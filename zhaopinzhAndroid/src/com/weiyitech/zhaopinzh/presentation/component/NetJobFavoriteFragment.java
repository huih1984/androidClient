package com.weiyitech.zhaopinzh.presentation.component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.NetJobFavoriteListAdapter;
import com.weiyitech.zhaopinzh.struct.JobConciseNetRsp;
import com.weiyitech.zhaopinzh.struct.SearchTerm;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;
//import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-5
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public class NetJobFavoriteFragment extends Fragment {
    ListView listView;
    ArrayList<JobConciseNetRsp> jobConciseNetRspArrayList;
    NetJobFavoriteListAdapter netJobFavoriteListAdapter;
    public SearchTerm searchTerm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里定义Fragment的布局
        View view = inflater.inflate(R.layout.net_job_favorite_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.net_job_favoritefragment_list_view);

        jobConciseNetRspArrayList = new ArrayList<JobConciseNetRsp>();
        MyDatabaseUtil.putNetJobFromDataBase(jobConciseNetRspArrayList, getActivity());
        netJobFavoriteListAdapter = new NetJobFavoriteListAdapter(getActivity(), jobConciseNetRspArrayList);
        listView.setAdapter(netJobFavoriteListAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("NetJobFaovriteListFragment"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd("NetJobFavoriteListFragment");
    }
}
