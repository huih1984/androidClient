package com.weiyitech.zhaopinzh.presentation.component;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.ListView;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.QueryFair;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.adapter.FairListAdapter;
import com.weiyitech.zhaopinzh.struct.JobFairConcise;
import com.weiyitech.zhaopinzh.struct.JobFairPager;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.struct.SearchTerm;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;
//import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-8
 * Time: 下午5:19
 * To change this template use File | Settings | File Templates.
 */
public class FairListFragment extends Fragment implements BusinessInterface {
    ListView listView;
    ArrayList<JobFairConcise> jobFairConciseList;
    FairListAdapter fairListAdapter;
    public SearchTerm searchTerm;
    public int totalFairCount;
    private View rootView;//缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里定义Fragment的布局

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.home_activity_fairlist_fragment, container, false);
            listView = (ListView) rootView.findViewById(R.id.job_activity_fairfragment_listView);
            jobFairConciseList = new ArrayList<JobFairConcise>();
            fairListAdapter = new FairListAdapter(getActivity(), jobFairConciseList);

            listView.setAdapter(fairListAdapter);
            ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

            searchTerm = Common.getSearchSettings(getActivity());

            queryTotal(searchTerm);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //此处可以更新UI
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.JOBFAIRCONCISE_TYPE) {
                List<Integer> localFairIds = MyDatabaseUtil.queryFairDatabase(getActivity());
                List<Integer> readFairIds = MyDatabaseUtil.queryFairReadDatabase(getActivity());
                ArrayList<JobFairConcise> jobConsiceList = bundle.getParcelableArrayList("list");
                for (JobFairConcise jobFairConcise : jobConsiceList) {
                    if (localFairIds != null && localFairIds.contains(jobFairConcise.fairId)) {
                        jobFairConcise.isNew = false;
                        jobFairConciseList.add(jobFairConcise);
                    } else {
                        jobFairConcise.isNew = true;
                        jobFairConciseList.add(0, jobFairConcise);
                        MyDatabaseUtil.putLocalFairIdsToDataBase(jobFairConcise.fairId, jobFairConcise.runningTime, getActivity());
                    }
                    if (readFairIds!= null && readFairIds.contains(jobFairConcise.fairId)) {
                        jobFairConcise.read = true;
                    }
                }
                Pager pager = bundle.getParcelable("pager");
                //jobFairConciseList.clear();
                // jobFairConciseList.addAll(jobConsiceList);
//                if(pager.current + pager.length < totalFairCount){
//
//                }
//                listView.setAdapter(fairListAdapter);
//                if(footerView != null)footerView.findViewById(R.id.footer_view_image_view).clearAnimation();
                firstIndex = pager.current + pager.length;
                if (firstIndex < totalFairCount) {
                    fairListAdapter.footViewExist = true;
                } else {
                    fairListAdapter.footViewExist = false;
                }
                fairListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = null;
        Bundle bundle = new Bundle();
        message = new Message();
        if (dataType == Common.FAIRTOTAL_TYPE) {
            totalFairCount = ((Pager) t).total;
            queryFairList(searchTerm);
        } else if (dataType == Common.JOBFAIRCONCISE_TYPE) {
            JobFairPager jobFairPager = (JobFairPager) t;
            ArrayList<JobFairConcise> jobConsiceList = (ArrayList<JobFairConcise>) jobFairPager.list;
            bundle.putInt("type", Common.JOBFAIRCONCISE_TYPE);
            bundle.putParcelableArrayList("list", jobConsiceList);
            bundle.putParcelable("pager", jobFairPager.pager);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private int firstIndex = 0;

    public void queryTotal(SearchTerm searchTerm) {
        searchTerm.pager = new Pager();
        searchTerm.pager.current = 0;
        searchTerm.pager.length = 10;
        QueryFair queryFair = new QueryFair();
        queryFair.queryTotal(searchTerm, this);
        return;
    }

    public void queryFairList(SearchTerm searchTerm) {
        searchTerm.pager = new Pager();
        searchTerm.pager.current = firstIndex;
        searchTerm.pager.length = 10;
        QueryFair queryFair = new QueryFair();
        queryFair.queryFairList(searchTerm, this);
        return;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fair_fragment_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fair_fragment_refresh:
                if (searchTerm != null) {
                    queryFairList(searchTerm);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("FairListFragment"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd("FairListFragment");
    }
}
