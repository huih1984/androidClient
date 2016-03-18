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
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.ZhaopinzhJobListAdapter;
import com.weiyitech.zhaopinzh.struct.CommentConcise;
import com.weiyitech.zhaopinzh.struct.JobDetailZhaopinzh;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-10
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhJobConciseScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";

    private int mPageNumber;
    public ArrayList<JobDetailZhaopinzh> mJobDetailZhaopinzhs;
    public ListView listView = null;
    public ZhaopinzhJobListAdapter zhaopinzhJobListAdapter = null;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private View rootView;
    Context context;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ZhaopinzhJobConciseScreenSlidePageFragment create(int pageNumber) {
        ZhaopinzhJobConciseScreenSlidePageFragment fragment = new ZhaopinzhJobConciseScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ZhaopinzhJobConciseScreenSlidePageFragment() {
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
                .inflate(R.layout.zhaopinzh_job_fragment_screen_slide_page, container, false);

        listView = (ListView) rootView.findViewById(R.id.zhaopinzh_job_activity_listview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.zhaopinzh_job_activity_progress);
        progressBar.setMax(100);
        progressTextView = (TextView) rootView.findViewById(R.id.zhaopinzh_job_activity_progress_textView);
        mJobDetailZhaopinzhs = new ArrayList<JobDetailZhaopinzh>();
        zhaopinzhJobListAdapter = new ZhaopinzhJobListAdapter(getActivity(), mJobDetailZhaopinzhs, getPageNumber());
        listView.setAdapter(zhaopinzhJobListAdapter);
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
                    zhaopinzhJobListAdapter.setFlagBusy(true);
                    break;
                case 0:
                    zhaopinzhJobListAdapter.setFlagBusy(false);
                    break;
                case 1:
                    zhaopinzhJobListAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            //zhaopinzhJobListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == View.GONE){
                ((ZhaopinzhJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.GONE);
            }
            super.handleMessage(msg);
        }
    };

    boolean indexVisible = true;
    int  mLastMotionY;
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
                    indexVisible = ((ZhaopinzhJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.getVisibility() != View.GONE;
                    if(deltaY < 0  && indexVisible){
                        Animation goneAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.accelerate_gone);
                        ((ZhaopinzhJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(goneAnim);
                        handler.sendEmptyMessageDelayed(View.GONE, 1000);
                    }
                    if(deltaY > 0 && listView.getChildAt(0).getTop() == 0 && !indexVisible){
                        ((ZhaopinzhJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.VISIBLE);
                        Animation inAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.decelerate_in);
                        ((ZhaopinzhJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(inAnim);
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
    } ;

    void insertNetJobConciseToDatabase(JobDetailZhaopinzh jobDetailZhaopinzh) {
        ContentResolver resolver = context.getContentResolver();
        String selection = "job_id = CAST(? AS INT)";
        Cursor cursor = resolver.query(MessageProvider.READ_ZHAOPINZH_JOB_DETAIL_URI, null, selection, null, null);
        if (cursor != null) {
            cursor.close();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("job_id", jobDetailZhaopinzh.jobId);
            Common.putZhaopinzhJobDetail(contentValues, jobDetailZhaopinzh);
            resolver.insert(MessageProvider.READ_ZHAOPINZH_JOB_DETAIL_URI, contentValues);
        }
    }

    boolean jobConciseNetIsInList(JobDetailZhaopinzh jobDetailZhaopinzh, ArrayList<JobDetailZhaopinzh> favoriteList) {
        for (JobDetailZhaopinzh jobDetailZhaopinzh1 : favoriteList) {
            if (jobDetailZhaopinzh.jobId == jobDetailZhaopinzh1.jobId) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param pager
     * @param jobDetailZhaopinzhs
     */
    public void updateListView(Pager pager, ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs) {
        try {
            mJobDetailZhaopinzhs.clear();
            mJobDetailZhaopinzhs.addAll(jobDetailZhaopinzhs);
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, null, null, null, null);
            ArrayList<JobDetailZhaopinzh> favoriteList = new ArrayList<JobDetailZhaopinzh>();
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JobDetailZhaopinzh jobDetailZhaopinzh = new JobDetailZhaopinzh();
                    jobDetailZhaopinzh.employerId = cursor.getInt(cursor.getColumnIndex("employer_id"));
                    jobDetailZhaopinzh.jobId = cursor.getInt(cursor.getColumnIndex("job_id"));
                    jobDetailZhaopinzh.employerName = cursor.getString(cursor.getColumnIndex("employer_name"));
                    jobDetailZhaopinzh.jobName = cursor.getString(cursor.getColumnIndex("job_name"));
                    jobDetailZhaopinzh.workplace = cursor.getString(cursor.getColumnIndex("workplace"));
                    jobDetailZhaopinzh.payment = cursor.getString(cursor.getColumnIndex("payment"));
                    jobDetailZhaopinzh.readTimes = cursor.getInt(cursor.getColumnIndex("read_times"));
                    jobDetailZhaopinzh.logoPath = cursor.getString(cursor.getColumnIndex("logo_path"));
                    favoriteList.add(jobDetailZhaopinzh);
                }
            }
            progressBar.setIndeterminate(false);
            progressTextView.setVisibility(View.GONE);
            for (int i = 0; i < mJobDetailZhaopinzhs.size(); ++i) {
                JobDetailZhaopinzh mJobDetailZhaopinzh;
                mJobDetailZhaopinzh = mJobDetailZhaopinzhs.get(i);
                if (jobConciseNetIsInList(mJobDetailZhaopinzh, favoriteList)) {
                    mJobDetailZhaopinzh.isFavorite = true;
                }
                //查询评论数量
                CommentConcise commentConcise = new CommentConcise();
                commentConcise.employerId = mJobDetailZhaopinzhs.get(i).employerId;
                commentConcise.position = i;
                commentConcise.pageIndex = pager.current;
                CommentUtil.queryCommentCnt((CommonFragment) getParentFragment(), commentConcise);
            }

            if (cursor != null) cursor.close();
            ((ZhaopinzhJobListFragment) getParentFragment()).pagerData.put(getPageNumber(), mJobDetailZhaopinzhs);

            progressBar.setVisibility(View.GONE);
            progressTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            zhaopinzhJobListAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void updateCommentCnt(int commentCnt, int position) {
        if (mJobDetailZhaopinzhs != null && mJobDetailZhaopinzhs.size() > position) {
            mJobDetailZhaopinzhs.get(position).commentCnt = commentCnt;
            zhaopinzhJobListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        ZhaopinzhJobListFragment zhaopinzhJobListFragment = (ZhaopinzhJobListFragment) getParentFragment();
        if (mJobDetailZhaopinzhs.size() == 0) {
            if (zhaopinzhJobListFragment.pagerData.get(getPageNumber()) != null) {
                mJobDetailZhaopinzhs.clear();
                mJobDetailZhaopinzhs.addAll(zhaopinzhJobListFragment.pagerData.get(getPageNumber()));
                progressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                zhaopinzhJobListAdapter.notifyDataSetChanged();
            } else {
                zhaopinzhJobListFragment.queryZhaopinzhJobConcise(((ZhaopinzhJobListFragment) getParentFragment()).searchTerm, mPageNumber);
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
        zhaopinzhJobListAdapter.notifyDataSetChanged();
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
