package com.weiyitech.zhaopinzh.presentation.component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.ZhaopinzhJobFavoriteListAdapter;
import com.weiyitech.zhaopinzh.struct.JobDetailZhaopinzh;
import com.weiyitech.zhaopinzh.struct.SearchTerm;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-5
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhJobFavoriteFragment extends Fragment {
    ListView listView;
    ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs;
    ZhaopinzhJobFavoriteListAdapter zhaopinzhJobFavoriteListAdapter;
    public SearchTerm searchTerm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里定义Fragment的布局
        View view = inflater.inflate(R.layout.zhaopinzh_job_favorite_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.zhaopinzh_job_favoritefragment_list_view);

        jobDetailZhaopinzhs = new ArrayList<JobDetailZhaopinzh>();
        MyDatabaseUtil.putZhaopinzhJobFromDataBase(jobDetailZhaopinzhs, getActivity());
        zhaopinzhJobFavoriteListAdapter = new ZhaopinzhJobFavoriteListAdapter(getActivity(), jobDetailZhaopinzhs);
        listView.setAdapter(zhaopinzhJobFavoriteListAdapter);
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

