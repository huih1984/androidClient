package com.weiyitech.zhaopinzh.presentation.component;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.QueryJob;
import com.weiyitech.zhaopinzh.business.QueryZhaopinzhJob;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.CommonActivity;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.activity.ZhaopinzhJobConciseListActivity;
import com.weiyitech.zhaopinzh.presentation.adapter.ZhaopinzhJobScreenSlidePagerAdapter;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.PageManageUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-5
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhJobListFragment extends CommonFragment {
    public ViewPager jobPager;
    public ZhaopinzhJobScreenSlidePagerAdapter zhaopinzhJobScreenSlidePagerAdapter;
    public static Context context = null;
    public int totalJobConciseCnt = 0;

    public SearchTerm searchTerm;
    int orderBy = 0;
    private View rootView;//缓存Fragment view
    public PageManageUtil pageManageUtil;
    public Map<Integer, ArrayList<JobDetailZhaopinzh>> pagerData = new LinkedHashMap<Integer, ArrayList<JobDetailZhaopinzh>>() {
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里定义Fragment的布局
        super.onCreateView(inflater, container, savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.home_activity_joblist_fragment, container, false);
            jobPager = (ViewPager) rootView.findViewById(R.id.job_activity_job_pager);
            pageManageUtil = new PageManageUtil(getActivity(), rootView, jobPager);
            ViewGroup parent = (ViewGroup) rootView.findViewById(R.id.job_activity_layout);
            pageManageUtil.addPageSelectView(parent);

            jobPager = (ViewPager) rootView.findViewById(R.id.job_activity_job_pager);

            zhaopinzhJobScreenSlidePagerAdapter = new ZhaopinzhJobScreenSlidePagerAdapter(getChildFragmentManager());
            zhaopinzhJobScreenSlidePagerAdapter.setContext(getActivity());
            zhaopinzhJobScreenSlidePagerAdapter.setViewPager(jobPager);
            jobPager.setAdapter(zhaopinzhJobScreenSlidePagerAdapter);
            pageManageUtil.currentPage = 0;
            searchTerm = Common.getSearchSettings(getActivity());
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null) {
                searchTerm.employerId = bundle.getInt("employerId");
            }
            queryTotal(searchTerm);
            jobPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    pageManageUtil.pageSelectSettings(position);
                }
            });
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
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
        if (dataType == Common.JOBTOTAL_TYPE) {
            Pager pager = (Pager) t;
            totalJobConciseCnt = pager.total;
            pageManageUtil.pageCount = totalJobConciseCnt / Common.ONEPAGEJOBS + (totalJobConciseCnt % Common.ONEPAGEJOBS == 0 ? 0 : 1);
            zhaopinzhJobScreenSlidePagerAdapter.setPageCount(pageManageUtil.pageCount);
            zhaopinzhJobScreenSlidePagerAdapter.notifyDataSetChanged();
            if (totalJobConciseCnt == 0) {
                Toast.makeText(getActivity(), "系统未能获取任何职位信息，可能是没有发布，也可能是系统未能查到信息，请您移步官网查看详情", Toast.LENGTH_SHORT).show();
            }
            bundle.putInt("type", Common.ADD_PAGE_INDEX_TYPE);
            bundle.putInt("page_count", pageManageUtil.pageCount);
        } else if (dataType == Common.ZHAOPINZH_JOB_CONCISE_TYPE) {
            /*将Object转换成message*/
            /*将业务层传递过来的数据，分发给不同的关键字，打包，同时将listivew中的数据刷新*/
            ZhaopinzhJobPager zhaopinzhJobPager = (ZhaopinzhJobPager) t;
            int queryPage = zhaopinzhJobPager.pager.current / Common.ONEPAGEJOBS;
            ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs = (ArrayList<JobDetailZhaopinzh>) zhaopinzhJobPager.list;
            bundle.putInt("type", Common.ZHAOPINZH_JOB_CONCISE_TYPE);
            bundle.putParcelable("pager", zhaopinzhJobPager.pager);
            bundle.putParcelableArrayList("list", jobDetailZhaopinzhs);
            bundle.putInt("uipage", queryPage);
        } else if (dataType == Common.TIMEOUT_TYPE) {
            bundle.putInt("type", Common.TIMEOUT_TYPE);
            bundle.putInt("req", (Integer) t);
            if (QueryJob.JOBDETAILREQ.equals(t)) {
                bundle.putInt("position", position[0]);
            }
        } else if (dataType == Common.COMMENT_TOTAL_TYPE) {
            bundle.putInt("type", Common.COMMENT_TOTAL_TYPE);
            bundle.putParcelable("comment_concise", (CommentConcise) t);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * @param searchTerm
     */
    public void queryTotal(SearchTerm searchTerm) {
        searchTerm.pager = new Pager();
        searchTerm.pager.current = 0;
        searchTerm.pager.length = 10;
        List<String> industryList = Common.getIndustryList(getActivity());
        String industryStr = new String();
        List<String> majorList = Common.getMajorList(getActivity());
        String majorStr = new String();
        if (searchTerm.industryTypes != null) {
            for (Integer industry : searchTerm.industryTypes) {
                industryStr += industryList.get(industry) + ",";
            }
            for (Integer major : searchTerm.majorReqs) {
                majorStr += majorList.get(major) + ",";
            }
//            if(industryStr.length() != 0 || majorStr.length() != 0){
//                Toast.makeText(this, "您当前设置了筛选条件" + industryStr + majorStr, Toast.LENGTH_SHORT).show();
//            }
        }
        QueryZhaopinzhJob queryZhaopinzhJob = new QueryZhaopinzhJob();
        queryZhaopinzhJob.queryJobTotal(searchTerm, this);
        return;
    }

    /**
     * 获取单页能够显示item的个数
     */
    public void queryZhaopinzhJobConcise(SearchTerm searchTerm, int pageNumber) {
        /*如果还没有得到结果，请求查询*/
        if (orderBy > 0) {
            searchTerm.orderBy = orderBy;
        }
        searchTerm.pager = new Pager();
        searchTerm.pager.current = pageNumber * Common.ONEPAGEJOBS;

        searchTerm.pager.length = Common.ONEPAGEJOBS;
        QueryZhaopinzhJob queryZhaopinzhJob = new QueryZhaopinzhJob();
        queryZhaopinzhJob.queryJobConcise(searchTerm, this);
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
                jobPager.setCurrentItem(0);
            } else if (dataType == Common.ZHAOPINZH_JOB_CONCISE_TYPE) {
                Pager pager = (Pager) bundle.get("pager");
                ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs = bundle.getParcelableArrayList("list");
                int uipage = bundle.getInt("uipage");
                if (getActivity() != null && !((CommonActivity) getActivity()).destroyed) {
                    zhaopinzhJobScreenSlidePagerAdapter.startUpdate(jobPager);
                    ((ZhaopinzhJobConciseScreenSlidePageFragment) zhaopinzhJobScreenSlidePagerAdapter.instantiateItem(jobPager, uipage)).updateListView(pager, jobDetailZhaopinzhs);
                    zhaopinzhJobScreenSlidePagerAdapter.finishUpdate(jobPager);
                }
            } else if (dataType == Common.COMMENT_TOTAL_TYPE) {
                CommentConcise commentConcise = (CommentConcise) bundle.get("comment_concise");
                if (getActivity() != null && !((CommonActivity) getActivity()).destroyed) {
                    zhaopinzhJobScreenSlidePagerAdapter.startUpdate(jobPager);
                    ((ZhaopinzhJobConciseScreenSlidePageFragment) zhaopinzhJobScreenSlidePagerAdapter.instantiateItem(jobPager, commentConcise.pageIndex)).updateCommentCnt(commentConcise.total, commentConcise.position);
                    zhaopinzhJobScreenSlidePagerAdapter.finishUpdate(jobPager);
                }
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

    /**
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        SearchTerm resumeSearchTerm = Common.getSearchSettings(getActivity());
        if (searchTerm != null) {
            if (!resumeSearchTerm.searchConditionItemEqual(searchTerm)) {
                totalJobConciseCnt = 0;
                Common.checkPref(getActivity());
                searchTerm = resumeSearchTerm;
                zhaopinzhJobScreenSlidePagerAdapter.setPageCount(0);
                zhaopinzhJobScreenSlidePagerAdapter.notifyDataSetChanged();
                pagerData.clear();
                queryTotal(searchTerm);

            } else if (((ZhaopinzhJobConciseListActivity) getActivity()).resumeFromWhere == HomeActivity.FROM_SEARCHACTIVITY) {
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
                        totalJobConciseCnt = 0;
                        zhaopinzhJobScreenSlidePagerAdapter.setPageCount(0);
                        zhaopinzhJobScreenSlidePagerAdapter.notifyDataSetChanged();
                        pagerData.clear();
                        queryTotal(searchTerm);
                    } else if ((uri = getActivity().getIntent().getData()) != null) {
                        Cursor cursor = getActivity().managedQuery(uri, null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int iIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.INPUTTEXT);
                            try {
                                searchTerm.keyword = cursor.getString(iIndex);
                            } catch (Exception e) {

                            }
                            totalJobConciseCnt = 0;
                            zhaopinzhJobScreenSlidePagerAdapter.setPageCount(0);
                            zhaopinzhJobScreenSlidePagerAdapter.notifyDataSetChanged();
                            pagerData.clear();
                            queryTotal(searchTerm);
                        }
                    }
                } else {

                }
            }
        }
        super.onResume();
        //MobclickAgent.onPageStart("ZhaopinzhJobListFragment");
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
        inflater.inflate(R.menu.job_activity_actions, menu);
    }


    private Date oldTime = new Date();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.job_activity_refresh:
                if (searchTerm != null) {
                    Date time = new Date();
                    if (time.getTime() - oldTime.getTime() < 2000) {
                        oldTime = time;
                        return true;
                    } else {
                        oldTime = time;
                    }
                    jobPager.setCurrentItem(0);
                    totalJobConciseCnt = 0;
                    //isTotalpagesQueried = false;
                    zhaopinzhJobScreenSlidePagerAdapter.setPageCount(0);
                    zhaopinzhJobScreenSlidePagerAdapter.notifyDataSetChanged();
                    pagerData.clear();
                    queryTotal(searchTerm);
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
        //MobclickAgent.onPageEnd("ZhaopinzhJobListFragment");
    }
}

