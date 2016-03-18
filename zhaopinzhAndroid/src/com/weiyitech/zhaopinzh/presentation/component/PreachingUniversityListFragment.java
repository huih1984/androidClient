package com.weiyitech.zhaopinzh.presentation.component;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.ListView;
//import com.umeng.analytics.MobclickAgent;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.QueryPreaching;
import com.weiyitech.zhaopinzh.presentation.activity.CampusActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.CampusListAdapter;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.struct.PreachingUniversityRsp;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.struct.SearchTerm;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-22
 * Time: 下午5:38
 * To change this template use File | Settings | File Templates.
 */
public class PreachingUniversityListFragment extends Fragment implements BusinessInterface {
    public SearchTerm searchTerm;

    private View rootView;//缓存Fragment view
    CampusActivity campusActivity;
    ListView listView;
    CampusListAdapter campusListAdapter;
    ArrayList<PreachingUniversityRsp> preachingUniversityRsps = new ArrayList<PreachingUniversityRsp>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里定义Fragment的布局
        campusActivity = (CampusActivity) getActivity();
        if (rootView == null) {

            rootView = inflater.inflate(R.layout.university_fragment, container, false);
            listView = (ListView) rootView.findViewById(R.id.campus_activity_list_view);
            campusListAdapter = new CampusListAdapter(campusActivity, preachingUniversityRsps);
            listView.setAdapter(campusListAdapter);

            ActionBar actionBar = campusActivity.getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

            searchTerm = Common.getSearchSettings(getActivity());
            queryPreachingUniversityList(searchTerm);
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
            if (dataType == Common.PREACH_MEETING_UNIVERSITY_TYPE) {
                ArrayList<PreachingUniversityRsp> lv_preachingUniversityRsps = bundle.getParcelableArrayList("list");
                preachingUniversityRsps.clear();
                if (lv_preachingUniversityRsps != null)
                    preachingUniversityRsps.addAll(lv_preachingUniversityRsps);
                campusListAdapter.notifyDataSetChanged();
                /**
                 * 查询完个数再查整体ids
                 */
                queryPreachIds(searchTerm);
            } else if (dataType == Common.PREACH_MEETING_IDS_TYPE) {
                ArrayList<PreachmeetingConciseRsp> lv_preachmeetingConciseRsps = bundle.getParcelableArrayList("list");
                if (getActivity() == null) {
                    return;
                }
                List<Integer> ids = MyDatabaseUtil.queryPreachDatabase(getActivity());
                Map<String, Boolean> addNew = new HashMap<String, Boolean>();
                for (PreachmeetingConciseRsp preachmeetingConciseRsp : lv_preachmeetingConciseRsps) {
                    if (ids != null && ids.contains(preachmeetingConciseRsp.getPreachMeetingId())) {
                        preachmeetingConciseRsp.isNew = false;
                    } else {
                        addNew.put(preachmeetingConciseRsp.getRunningUniversity(), true);
                        preachmeetingConciseRsp.isNew = true;
                        //MyDatabaseUtil.putLocalPreachIdsToDataBase(preachmeetingConciseRsp.preachMeetingId, preachmeetingConciseRsp.runningTimeBegin, preachmeetingConciseRsp.runningUniversity, getActivity());
                    }
                }
                for (PreachingUniversityRsp preachingUniversityRsp : preachingUniversityRsps) {
                    if (addNew.get(preachingUniversityRsp.university) != null) {
                        preachingUniversityRsp.addNew = true;
                    }
                }
                campusListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = null;
        Bundle bundle = new Bundle();
        message = new Message();
        if (dataType == Common.PREACH_MEETING_UNIVERSITY_TYPE) {
            ArrayList<PreachingUniversityRsp> l_preachingUniversityRsps = (ArrayList<PreachingUniversityRsp>) t;
            bundle.putInt("type", Common.PREACH_MEETING_UNIVERSITY_TYPE);
            bundle.putParcelableArrayList("list", l_preachingUniversityRsps);
        } else if (dataType == Common.PREACH_MEETING_IDS_TYPE) {
            ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList = (ArrayList<PreachmeetingConciseRsp>) t;
            bundle.putInt("type", Common.PREACH_MEETING_IDS_TYPE);
            bundle.putParcelableArrayList("list", preachmeetingConciseRspArrayList);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public void queryPreachingUniversityList(SearchTerm searchTerm) {
        searchTerm.pager = new Pager();
        searchTerm.pager.current = 0;
        searchTerm.pager.length = 10;
        QueryPreaching queryPreaching = new QueryPreaching();
        queryPreaching.queryPreachUniversityList(searchTerm, this);
        return;
    }

    public void queryPreachIds(SearchTerm searchTerm) {
        searchTerm.pager = new Pager();
        searchTerm.pager.current = 0;
        searchTerm.pager.length = 1000;
        QueryPreaching queryPreaching = new QueryPreaching();
        queryPreaching.queryPreachingIds(searchTerm, this);
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
                    queryPreachingUniversityList(searchTerm);
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
        //MobclickAgent.onPageStart("PreachingUniversityListFragment"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd("PreachingUniversityListFragment");
    }
}