package com.weiyitech.zhaopinzh.presentation.component;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.activity.JobsOfFairActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.FairJobListAdapter;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.slideexpandablelistview.ExpandCollapseAnimation;
import com.weiyitech.zhaopinzh.util.slideexpandablelistview.SlideExpandableListAdapter;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-22
 * Time: 下午6:18
 * To change this template use File | Settings | File Templates.
 */
public class FairJobsScreenSlidePageFragment extends Fragment {
    public static final String ARG_PAGE = "page";

    private int mPageNumber;
    public ArrayList<RunEmployerComponent> runEmployerComponents;
    public ActionSlideExpandableListView listView = null;
    public FairJobListAdapter fairJobListAdapter = null;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private View rootView;
    Context context;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FairJobsScreenSlidePageFragment create(int pageNumber) {
        FairJobsScreenSlidePageFragment fragment = new FairJobsScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FairJobsScreenSlidePageFragment() {
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
                .inflate(R.layout.home_activity_fragment_screen_slide_page, container, false);

        listView = (ActionSlideExpandableListView) rootView.findViewById(R.id.job_activity_listview);

        setListViewExtraAction();
        progressBar = (ProgressBar) rootView.findViewById(R.id.job_activity_progress);
        progressBar.setMax(100);
        progressTextView = (TextView) rootView.findViewById(R.id.job_activity_progress_textView);
        runEmployerComponents = new ArrayList<RunEmployerComponent>();
        fairJobListAdapter = new FairJobListAdapter(getActivity(), runEmployerComponents, getPageNumber());
        listView.setAdapter(fairJobListAdapter);
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
                    fairJobListAdapter.setFlagBusy(true);
                    break;
                case 0:
                    fairJobListAdapter.setFlagBusy(false);
                    break;
                case 1:
                    fairJobListAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            //fairJobListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == View.GONE){
                ((FairJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.GONE);
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
                    indexVisible = ((FairJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.getVisibility() != View.GONE;
                    if(deltaY < 0  && indexVisible){
                        Animation goneAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.accelerate_gone);
                        ((FairJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(goneAnim);
                        handler.sendEmptyMessageDelayed(View.GONE, 1000);
                    }
                    if(deltaY > 0 && listView.getChildAt(0).getTop() == 0 && !indexVisible){
                        ((FairJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.VISIBLE);
                        Animation inAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.decelerate_in);
                        ((FairJobListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(inAnim);
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

    void insertReadRunEmployerToDatabase(RunEmployer runEmployer) {
        ContentResolver resolver = context.getContentResolver();
        String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
        String[] selectionArgs = {String.valueOf(runEmployer.employerId), String.valueOf(runEmployer.fairId)};
        Cursor cursor = resolver.query(MessageProvider.READ_RUNEMPLOYER_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.close();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("employer_id", runEmployer.employerId);
            contentValues.put("fair_id", runEmployer.fairId);
            contentValues.put("running_time", runEmployer.runningTime);
            resolver.insert(MessageProvider.READ_RUNEMPLOYER_URI, contentValues);
        }
    }

    /**
     * 创建listview，列表视图
     *
     * @return
     */
    public ActionSlideExpandableListView setListViewExtraAction() {
        listView.setiExtraAction(new SlideExpandableListAdapter.IExtraAction() {
            @Override
            public void itemExpandAction(View button, View target, final int position, int action) {
                 /*查询position位置的数据是否已经存在，存在直接从内存去，否则发送查询请求*/
                   /*查询职位详情*/
                if (action == ExpandCollapseAnimation.EXPAND) {
                    insertReadRunEmployerToDatabase(runEmployerComponents.get(position).runEmployer);
                    List<JobConciseRsp> jobConciseRsps = runEmployerComponents.get(position).jobConciseRspList;
                    if (jobConciseRsps == null) {
                        JobConciseReq jobConciseReq = new JobConciseReq();
                        jobConciseReq.employerId = runEmployerComponents.get(position).runEmployer.employerId;
                        jobConciseReq.fairId = runEmployerComponents.get(position).runEmployer.fairId;
                        JobsOfFairActivity jobsOfFairActivity = (JobsOfFairActivity) context;
                        Fragment fragment = jobsOfFairActivity.getSupportFragmentManager().findFragmentByTag("fairjoblist");
                        ((FairJobListFragment) fragment).queryJobConcise(jobConciseReq, position);
                        runEmployerComponents.get(position).expanded = true;
                    } else {
                        fairJobListAdapter.detailClickedItem = -1;
                        runEmployerComponents.get(position).expanded = false;
                        Fragment fragment = ((JobsOfFairActivity) context).getSupportFragmentManager().findFragmentByTag("fairjoblist");
                        if (jobConciseRsps.size() == 0) {
                            Toast.makeText(fragment.getActivity(), "没有该企业的职位信息，请参考现场招聘海报！", 3000).show();
                        }
                        ((FairJobListFragment) fragment).handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.setSelection(position);
                            }
                        }, 200);
                    }
                }
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.enableExpandOnItemClick();
        return listView;
    }

    boolean runEmployerIsInList(RunEmployer runEmployer, ArrayList<RunEmployer> runEmployers) {
        for (RunEmployer runEmployerIn : runEmployers) {
            if (runEmployer.fairId == runEmployerIn.fairId && runEmployer.employerId == runEmployerIn.employerId) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param pager
     * @param runEmployers
     */
    public void updateListView(Pager pager, ArrayList<RunEmployer> runEmployers) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(MessageProvider.FAVOR_RUNEMPLOYER_URI, null, null, null, null);
            ArrayList<RunEmployer> favoriteList = new ArrayList<RunEmployer>();
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    RunEmployer runEmployer = new RunEmployer();
                    runEmployer.employerId = cursor.getInt(cursor.getColumnIndex("employer_id"));
                    runEmployer.fairId = cursor.getInt(cursor.getColumnIndex("fair_id"));
                    favoriteList.add(runEmployer);
                }
            }
            progressBar.setIndeterminate(false);
            TextView progressTextView = (TextView) rootView.findViewById(R.id.job_activity_progress_textView);
            progressTextView.setVisibility(View.GONE);
            ArrayList<RunEmployerComponent> runEmployerComponentArrayList = new ArrayList<RunEmployerComponent>();
            for (int i = 0; i < runEmployers.size(); ++i) {
                RunEmployerComponent mRunEmployerComponent = new RunEmployerComponent();
                mRunEmployerComponent.runEmployer = runEmployers.get(i);
                runEmployerComponentArrayList.add(mRunEmployerComponent);

                if (runEmployerIsInList(runEmployers.get(i), favoriteList)) {
                    mRunEmployerComponent.isFavorite = true;
                }

                //查询评论数量
                CommentConcise commentConcise = new CommentConcise();
                commentConcise.employerId = runEmployerComponentArrayList.get(i).runEmployer.employerId;
                commentConcise.position = i;
                commentConcise.pageIndex = pager.current;
                CommentUtil.queryCommentCnt((CommonFragment) getParentFragment(), commentConcise);
            }

            if (cursor != null) cursor.close();
            runEmployerComponents.clear();
            runEmployerComponents.addAll(runEmployerComponentArrayList);
            ((FairJobListFragment) getParentFragment()).pagerData.put(getPageNumber(), runEmployerComponents);

            progressBar.setVisibility(View.INVISIBLE);

            listView.setVisibility(View.VISIBLE);
            fairJobListAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void updateCommentCnt(int commentCnt, int position) {
        if (runEmployerComponents != null && runEmployerComponents.size() > position) {
            runEmployerComponents.get(position).commentCnt = commentCnt;
            fairJobListAdapter.notifyDataSetChanged();
        }
    }

    public void updateJobConciseView(List<JobConciseRsp> jobConciseRsps, final int position) {
        try {
            Fragment fragment = ((JobsOfFairActivity) context).getSupportFragmentManager().findFragmentByTag("fairjoblist");
            runEmployerComponents.get(position).jobConciseRspList = (ArrayList<JobConciseRsp>) jobConciseRsps;
            if (runEmployerComponents.get(position).jobConciseRspList.size() == 0) {
                Toast.makeText(fragment.getActivity(), "没有该企业的职位信息，请参考现场招聘海报！", 3000).show();
            }
            fairJobListAdapter.notifyDataSetChanged();
            ((FairJobListFragment) fragment).handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.setSelection(position);
                }
            }, 200);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        FairJobListFragment fairJobListFragment = (FairJobListFragment) getParentFragment();
        if (runEmployerComponents.size() == 0) {
            if (fairJobListFragment.pagerData.get(getPageNumber()) != null) {
                runEmployerComponents.clear();
                runEmployerComponents.addAll(fairJobListFragment.pagerData.get(getPageNumber()));
                progressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                fairJobListAdapter.notifyDataSetChanged();
            } else {
                fairJobListFragment.queryRunEmployer(fairJobListFragment.searchTerm, mPageNumber);
            }
        }
        BitSet bitSet = listView.getAdapter().getOpenItems();

        for (int i = 0; i < bitSet.length(); ++i) {
            bitSet.set(i, false);
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
        fairJobListAdapter.notifyDataSetChanged();
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