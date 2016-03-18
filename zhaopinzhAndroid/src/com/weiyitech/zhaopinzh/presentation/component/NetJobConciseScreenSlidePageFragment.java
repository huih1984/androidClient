package com.weiyitech.zhaopinzh.presentation.component;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.activity.NetJobConciseListActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.NetJobListAdapter;
import com.weiyitech.zhaopinzh.struct.CommentConcise;
import com.weiyitech.zhaopinzh.struct.JobConciseNetRsp;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-10
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class NetJobConciseScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";

    private int mPageNumber;
    public ArrayList<JobConciseNetRsp> jobConciseNetRsps;
    public ListView listView = null;
    public NetJobListAdapter netJobListAdapter = null;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private View rootView;
    Context context;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static NetJobConciseScreenSlidePageFragment create(int pageNumber) {
        NetJobConciseScreenSlidePageFragment fragment = new NetJobConciseScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public NetJobConciseScreenSlidePageFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.net_job_fragment_screen_slide_page, container, false);

        listView = (ListView) rootView.findViewById(R.id.net_job_activity_listview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.net_job_activity_progress);
        progressBar.setMax(100);
        progressTextView = (TextView) rootView.findViewById(R.id.net_job_activity_progress_textView);
        jobConciseNetRsps = new ArrayList<JobConciseNetRsp>();
        netJobListAdapter = new NetJobListAdapter(getActivity(), jobConciseNetRsps, getPageNumber());
        listView.setAdapter(netJobListAdapter);
        this.rootView = rootView;
        listView.setOnScrollListener(mScrollListener);
//        listView.setOnTouchListener(mOnTouchListener);
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {   //对listview的滚动监听
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            switch (i) {
                case 2:
                    netJobListAdapter.setFlagBusy(true);
                    break;
                case 0:
                    netJobListAdapter.setFlagBusy(false);
                    break;
                case 1:
                    netJobListAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            //netJobListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == View.GONE) {
                ((NetJobListFragment) getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.GONE);
            }
            super.handleMessage(msg);
        }
    };

    boolean indexVisible = true;
    int mLastMotionY;
    int moveReceiveTimes = 0;
    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int y = (int) event.getRawY();
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    moveReceiveTimes = 0;
                    int deltaY = y - mLastMotionY;
                    indexVisible = ((NetJobListFragment) getParentFragment()).pageManageUtil.pageIndexContainer.getVisibility() != View.GONE;
                    if (deltaY < 0 && indexVisible) {
                        Animation goneAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.accelerate_gone);
                        ((NetJobListFragment) getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(goneAnim);
                        handler.sendEmptyMessageDelayed(View.GONE, 1000);
                    }
                    if (deltaY > 0 && listView.getChildAt(0).getTop() == 0 && !indexVisible) {
                        ((NetJobListFragment) getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.VISIBLE);
                        Animation inAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.decelerate_in);
                        ((NetJobListFragment) getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(inAnim);
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (moveReceiveTimes == 0) {
                        mLastMotionY = y;
                    }
                    moveReceiveTimes += 1;
                    break;
            }
            return false;
        }
    };

    void insertNetJobConciseToDatabase(JobConciseNetRsp jobConciseNetRsp) {
        ContentResolver resolver = context.getContentResolver();
        String selection = "job_id = CAST(? AS INT)";
        Cursor cursor = resolver.query(MessageProvider.READ_NET_JOB_DETAIL_URI, null, selection, null, null);
        if (cursor != null) {
            cursor.close();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("job_id", jobConciseNetRsp.jobId);
            Common.putNetJobDetail(contentValues, jobConciseNetRsp);
            resolver.insert(MessageProvider.READ_NET_JOB_DETAIL_URI, contentValues);
        }
    }

    boolean jobConciseNetIsInList(JobConciseNetRsp jobConciseNetRsp, ArrayList<JobConciseNetRsp> favoriteList) {
        for (JobConciseNetRsp jobConciseNetRsp1 : favoriteList) {
            if (jobConciseNetRsp.jobId == jobConciseNetRsp1.jobId) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param pager
     * @param pJobConciseNetRsps
     */
    public void updateListView(Pager pager, ArrayList<JobConciseNetRsp> pJobConciseNetRsps) {
        try {
            jobConciseNetRsps.clear();
            jobConciseNetRsps.addAll(pJobConciseNetRsps);
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, null, null, null, null);
            ArrayList<JobConciseNetRsp> favoriteList = new ArrayList<JobConciseNetRsp>();
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JobConciseNetRsp jobConciseNetRsp = new JobConciseNetRsp();
                    jobConciseNetRsp.employerId = cursor.getInt(cursor.getColumnIndex("employer_id"));
                    jobConciseNetRsp.jobId = cursor.getInt(cursor.getColumnIndex("job_id"));
                    jobConciseNetRsp.employerName = cursor.getString(cursor.getColumnIndex("employer_name"));
                    jobConciseNetRsp.jobName = cursor.getString(cursor.getColumnIndex("job_name"));
                    jobConciseNetRsp.workplace = cursor.getString(cursor.getColumnIndex("workplace"));
                    jobConciseNetRsp.payment = cursor.getString(cursor.getColumnIndex("payment"));
                    jobConciseNetRsp.readTimes = cursor.getInt(cursor.getColumnIndex("read_times"));
                    jobConciseNetRsp.logoPath = cursor.getString(cursor.getColumnIndex("logo_path"));
                    favoriteList.add(jobConciseNetRsp);
                }
            }
            progressBar.setIndeterminate(false);
            progressTextView.setVisibility(View.GONE);
            for (int i = 0; i < jobConciseNetRsps.size(); ++i) {
                JobConciseNetRsp mJobConciseNetRsp;
                mJobConciseNetRsp = jobConciseNetRsps.get(i);
                if (jobConciseNetIsInList(mJobConciseNetRsp, favoriteList)) {
                    mJobConciseNetRsp.isFavorite = true;
                }
                //查询评论数量
                CommentConcise commentConcise = new CommentConcise();
                commentConcise.employerId = jobConciseNetRsps.get(i).employerId;
                commentConcise.position = i;
                commentConcise.pageIndex = pager.current;
                CommentUtil.queryCommentCnt((CommonFragment)getParentFragment(), commentConcise);
            }

            if (cursor != null) cursor.close();
            ((NetJobListFragment) getParentFragment()).pagerData.put(getPageNumber(), jobConciseNetRsps);

            progressBar.setVisibility(View.GONE);
            progressTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            netJobListAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void updateCommentCnt(int commentCnt, int position) {
        if (jobConciseNetRsps != null && jobConciseNetRsps.size() > position) {
            jobConciseNetRsps.get(position).commentCnt = commentCnt;
            netJobListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        NetJobListFragment netJobListFragment = (NetJobListFragment) getParentFragment();
        if (jobConciseNetRsps.size() == 0) {
            if (netJobListFragment.pagerData.get(getPageNumber()) != null) {
                jobConciseNetRsps.clear();
                jobConciseNetRsps.addAll(netJobListFragment.pagerData.get(getPageNumber()));
                progressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                netJobListAdapter.notifyDataSetChanged();
            } else {
                netJobListFragment.queryNetJobConcise(((NetJobListFragment) getParentFragment()).searchTerm, mPageNumber);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        //从其它页面返回，数据改变需要刷新
        netJobListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
