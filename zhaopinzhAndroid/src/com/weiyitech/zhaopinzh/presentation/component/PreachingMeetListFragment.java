package com.weiyitech.zhaopinzh.presentation.component;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.QueryJob;
import com.weiyitech.zhaopinzh.business.QueryPreaching;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.CampusActivity;
import com.weiyitech.zhaopinzh.presentation.adapter.PreachListScreenSlidePagerAdapter;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;
import com.weiyitech.zhaopinzh.util.PageManageUtil;

import java.util.*;

//import com.umeng.analytics.MobclickAgent;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-22
 * Time: 下午8:39
 * To change this template use File | Settings | File Templates.
 */
public class PreachingMeetListFragment extends Fragment implements BusinessInterface {
    public ViewPager preachMeetingPager;
    public PreachListScreenSlidePagerAdapter preachListScreenSlidePagerAdapter;
    public static Context context = null;
    public int totalPreachConciseCnt = 0;

    public SearchTerm searchTerm;
    public PageManageUtil pageManageUtil;
    int orderBy = 0;
    ActionBar.OnNavigationListener mOnNavigationListener;
    SpinnerAdapter mSpinnerAdapter;
    ActionBar actionBar;
    int dropdownIndex;
    public Map<Integer, ArrayList<PreachmeetingConciseRsp>> pagerData = new LinkedHashMap<Integer, ArrayList<PreachmeetingConciseRsp>>() {
    };
    private View rootView;//缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            //在这里定义Fragment的布局
            super.onCreateView(inflater, container, savedInstanceState);

            if (rootView == null) {
                mOnNavigationListener = new ActionBar.OnNavigationListener() {

                    @Override
                    public boolean onNavigationItemSelected(int position, long itemId) {
                        preachListScreenSlidePagerAdapter.setPageCount(0);
                        preachListScreenSlidePagerAdapter.notifyDataSetChanged();
                        orderBy = position + 1;
                        preachListScreenSlidePagerAdapter = new PreachListScreenSlidePagerAdapter(getChildFragmentManager());
                        preachListScreenSlidePagerAdapter.setContext(getActivity());
                        preachListScreenSlidePagerAdapter.setViewPager(preachMeetingPager);
                        preachMeetingPager.setAdapter(preachListScreenSlidePagerAdapter);
                        pageManageUtil.currentPage = 0;
                        pagerData.clear();
                        Common.setPreachMeetingDropDownPref(getActivity(), position);
                        queryTotal();
                        return true;
                    }
                };

                mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.preach_action_list,
                        android.R.layout.simple_spinner_dropdown_item);
                dropdownIndex = Common.getPreachMeetingDropDownPref(getActivity());
                orderBy = dropdownIndex + 1;
                rootView = inflater.inflate(R.layout.preach_meeting_list_fragment, container, false);
                preachMeetingPager = (ViewPager) rootView.findViewById(R.id.preach_meeting_list_fragment_job_pager);
                pageManageUtil = new PageManageUtil(getActivity(), rootView, preachMeetingPager);
                ViewGroup parent = (ViewGroup) rootView.findViewById(R.id.preach_fragment_layout);
                pageManageUtil.addPageSelectView(parent);

                preachListScreenSlidePagerAdapter = new PreachListScreenSlidePagerAdapter(getChildFragmentManager());
                preachListScreenSlidePagerAdapter.setContext(getActivity());
                preachListScreenSlidePagerAdapter.setViewPager(preachMeetingPager);
                preachMeetingPager.setAdapter(preachListScreenSlidePagerAdapter);
                pageManageUtil.currentPage = 0;
                searchTerm = Common.getSearchSettings(getActivity());
                //queryTotal();
                preachMeetingPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        pageManageUtil.pageSelectSettings(position);
                    }
                });
            }
            if (mSpinnerAdapter != null && mOnNavigationListener != null) {
                actionBar = ((CampusActivity) getActivity()).getSupportActionBar();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
                actionBar.setSelectedNavigationItem(dropdownIndex);
            }
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            return rootView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将获取的网络数据传递给界面，在网络数据达到时被调用
     *
     * @param dataType
     * @param t
     */
    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = null;
        Bundle bundle = new Bundle();
        message = new Message();
        if (dataType == Common.PREACH_MEETING_TOTAL_TYPE) {
            Pager pager = (Pager) t;
            totalPreachConciseCnt = pager.total;
            pageManageUtil.pageCount = totalPreachConciseCnt / Common.ONEPAGEJOBS + (totalPreachConciseCnt % Common.ONEPAGEJOBS == 0 ? 0 : 1);
            preachListScreenSlidePagerAdapter.setPageCount(pageManageUtil.pageCount);
            preachListScreenSlidePagerAdapter.notifyDataSetChanged();
            if (totalPreachConciseCnt == 0) {
                Toast.makeText(getActivity(), "系统未能获取任何职位信息，可能是没有发布，也可能是系统未能查到信息，请您移步官网查看详情", Toast.LENGTH_SHORT).show();
            }
            bundle.putInt("type", Common.ADD_PAGE_INDEX_TYPE);
            bundle.putInt("page_count", pageManageUtil.pageCount);
        } else if (dataType == Common.PREACH_MEETING_LIST_TYPE) {
            /*将Object转换成message*/
            /*将业务层传递过来的数据，分发给不同的关键字，打包，同时将listivew中的数据刷新*/
            PreachmeetingConciseRspPager preachmeetingConciseRspPager = (PreachmeetingConciseRspPager) t;

            ArrayList<PreachmeetingConciseRsp> toPaceableList = new ArrayList<PreachmeetingConciseRsp>();
            toPaceableList.addAll(preachmeetingConciseRspPager.list);

            bundle.putInt("type", Common.PREACH_MEETING_LIST_TYPE);
            bundle.putParcelable("pager", preachmeetingConciseRspPager.pager);
            bundle.putParcelableArrayList("list", toPaceableList);
            int queryPage = preachmeetingConciseRspPager.pager.current / Common.ONEPAGEJOBS;
            bundle.putInt("uipage", queryPage);
        } else if (dataType == Common.TIMEOUT_TYPE) {

        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public void queryTotal() {
        PreachmeetingConciseReq preachmeetingConciseReq = new PreachmeetingConciseReq();
        preachmeetingConciseReq.runningInterval = searchTerm.runningInterval;
        preachmeetingConciseReq.pager = new Pager();
        preachmeetingConciseReq.pager.current = 0;
        preachmeetingConciseReq.pager.length = 10;
        preachmeetingConciseReq.keyWord = searchTerm.keyword;
        QueryPreaching queryPreaching = new QueryPreaching();
        queryPreaching.queryPreachingTotal(preachmeetingConciseReq, this);
        return;
    }

    /**
     * 获取单页能够显示item的个数
     */
    public void queryPreachmeetingList(int pageNumber) {
        /*如果还没有得到结果，请求查询*/
        PreachmeetingConciseReq preachmeetingConciseReq = new PreachmeetingConciseReq();
        preachmeetingConciseReq.orderBy = orderBy;
        preachmeetingConciseReq.pager = new Pager();
        preachmeetingConciseReq.pager.current = pageNumber * Common.ONEPAGEJOBS;
        preachmeetingConciseReq.pager.length = Common.ONEPAGEJOBS;
        preachmeetingConciseReq.runningInterval = searchTerm.runningInterval;
        preachmeetingConciseReq.keyWord = searchTerm.keyword;
        QueryPreaching queryPreaching = new QueryPreaching();
        queryPreaching.queryPreachingList(preachmeetingConciseReq, this);
        return;
    }

    /**
     * 句柄处理界面刷新事件
     */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //此处可以更新UI
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.ADD_PAGE_INDEX_TYPE) {
                pageManageUtil.setVisibleOfPageButtons();
                pageManageUtil.setFirstSelect();
                preachMeetingPager.setCurrentItem(0);
            } else if (dataType == Common.PREACH_MEETING_LIST_TYPE) {
                Pager pager = (Pager) bundle.get("pager");
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
                        MyDatabaseUtil.putLocalPreachIdsToDataBase(preachmeetingConciseRsp.preachMeetingId, preachmeetingConciseRsp.runningTimeBegin, preachmeetingConciseRsp.runningUniversity, getActivity());
                    }
                }
                int uipage = bundle.getInt("uipage");

                preachListScreenSlidePagerAdapter.startUpdate(preachMeetingPager);
                ((PreachsScreenSlidePageFragment) preachListScreenSlidePagerAdapter.instantiateItem(preachMeetingPager, uipage)).updateListView(pager, lv_preachmeetingConciseRsps);
                preachListScreenSlidePagerAdapter.finishUpdate(preachMeetingPager);
            }
        }
    };


    /**
     *
     */
    public void timeoutDo(int req, int position) {
        ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) getActivity().getApplicationContext();
        try {
            if (zhaopinzhApp.runEmployerRspTimeout && req == QueryJob.JOBCONCISEREQ) {
                Toast.makeText(getActivity(), "网络不通畅！", Toast.LENGTH_SHORT).show();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        SearchTerm resumeSearchTerm = Common.getSearchSettings(getActivity());
        if (searchTerm != null) {
            if (!resumeSearchTerm.searchConditionItemEqual(searchTerm)) {
                totalPreachConciseCnt = 0;
                Common.checkPref(getActivity());
                searchTerm = resumeSearchTerm;
                preachListScreenSlidePagerAdapter.setPageCount(0);
                preachListScreenSlidePagerAdapter.notifyDataSetChanged();
                pagerData.clear();
                queryTotal();

            } else if (((CampusActivity) getActivity()).resumeFromWhere == CampusActivity.FROM_SEARCHACTIVITY) {
                searchTerm = resumeSearchTerm;
                Bundle bundle = getActivity().getIntent().getExtras();
                Uri uri;
                if (bundle != null) {
                    String value = bundle.getString("keyword");
                    if (value != null) {
                        try {
                            searchTerm.keyword = value;
                        } catch (Exception e) {

                        }
                        totalPreachConciseCnt = 0;
                        preachListScreenSlidePagerAdapter.setPageCount(0);
                        preachListScreenSlidePagerAdapter.notifyDataSetChanged();
                        pagerData.clear();
                        queryTotal();
                    }else if ((uri = getActivity().getIntent().getData()) != null) {
                        Cursor cursor = getActivity().managedQuery(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int iIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.INPUTTEXT);
                            try {
                                searchTerm.keyword = cursor.getString(iIndex);
                            } catch (Exception e) {

                            }
                            totalPreachConciseCnt = 0;
                            preachListScreenSlidePagerAdapter.setPageCount(0);
                            preachListScreenSlidePagerAdapter.notifyDataSetChanged();
                            pagerData.clear();
                            queryTotal();
                        }
                    }
                } else {

                }
            }
        }
        super.onResume();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.preach_fragment_actions, menu);
    }


    private Date oldTime = new Date();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preach_fragment_refresh:
                if (searchTerm != null) {
                    Date time = new Date();
                    if (time.getTime() - oldTime.getTime() < 2000) {
                        oldTime = time;
                        return true;
                    } else {
                        oldTime = time;
                    }
                    preachMeetingPager.setCurrentItem(0);
                    totalPreachConciseCnt = 0;
                    preachListScreenSlidePagerAdapter.setPageCount(0);
                    preachListScreenSlidePagerAdapter.notifyDataSetChanged();
                    pagerData.clear();
                    queryTotal();
                    pageManageUtil.currentPage = 0;
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
