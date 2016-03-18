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
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.UniversityCampusActivity;
import com.weiyitech.zhaopinzh.presentation.adapter.UniversityPreachListAdapter;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-27
 * Time: 下午2:57
 * To change this template use File | Settings | File Templates.
 */
public class UniversityPreachsScreenSlidePagerFragment extends Fragment {
    public static final String ARG_PAGE = "page";

    private int mPageNumber;
    public ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList;
    public ListView listView = null;
    public UniversityPreachListAdapter universityPreachListAdapter = null;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private View rootView;
    Context context;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static UniversityPreachsScreenSlidePagerFragment create(int pageNumber) {
        UniversityPreachsScreenSlidePagerFragment fragment = new UniversityPreachsScreenSlidePagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public UniversityPreachsScreenSlidePagerFragment() {
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
                .inflate(R.layout.preach_meeting_list_fragment_screen_slide_page, container, false);
        listView = (ListView) rootView.findViewById(R.id.preach_meeting_list_fragment_listview);

        progressBar = (ProgressBar) rootView.findViewById(R.id.preach_meeting_list_fragment_progress);
        progressBar.setMax(100);
        progressTextView = (TextView) rootView.findViewById(R.id.preach_meeting_list_fragment_progress_textView);
        preachmeetingConciseRspArrayList = new ArrayList<PreachmeetingConciseRsp>();
        universityPreachListAdapter = new UniversityPreachListAdapter(getActivity(), preachmeetingConciseRspArrayList);
        listView.setAdapter(universityPreachListAdapter);
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
                    universityPreachListAdapter.setFlagBusy(true);
                    break;
                case 0:
                    universityPreachListAdapter.setFlagBusy(false);
                    break;
                case 1:
                    universityPreachListAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == View.GONE){
                ((UniversityPreachingMeetListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.GONE);
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
                    indexVisible = ((UniversityPreachingMeetListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.getVisibility() != View.GONE;
                    if(deltaY < 0  && indexVisible){
                        Animation goneAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.accelerate_gone);
                        ((UniversityPreachingMeetListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(goneAnim);
                        handler.sendEmptyMessageDelayed(View.GONE, 1000);
                    }
                    if(deltaY > 0 && listView.getChildAt(0).getTop() == 0 && !indexVisible){
                        ((UniversityPreachingMeetListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setVisibility(View.VISIBLE);
                        Animation inAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.decelerate_in);
                        ((UniversityPreachingMeetListFragment)getParentFragment()).pageManageUtil.pageIndexContainer.setAnimation(inAnim);
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

    void insertPreachMeetingToDatabase(PreachmeetingConciseRsp preachmeetingConciseRsp) {
        ContentResolver resolver = context.getContentResolver();
        String selection = "preach_meeting_id = CAST(? AS INT)";
        String[] selectionArgs = {String.valueOf(preachmeetingConciseRsp.preachMeetingId)};
        Cursor cursor = resolver.query(MessageProvider.FAVOR_PREACH_MEETING_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.close();
        } else {
            ContentValues contentValues = new ContentValues();
            Common.putPreachMeeting(contentValues, preachmeetingConciseRsp);
            resolver.insert(MessageProvider.FAVOR_PREACH_MEETING_URI, contentValues);
        }
    }

    boolean preachMeetingIsInList(PreachmeetingConciseRsp preachmeetingConciseRsp, ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList) {
        for (PreachmeetingConciseRsp preachmeetingConciseRspIn : preachmeetingConciseRspArrayList) {
            if (preachmeetingConciseRsp.preachMeetingId == preachmeetingConciseRspIn.preachMeetingId) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param pager
     * @param
     */
    public void updateListView(Pager pager, ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayListExternal) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(MessageProvider.FAVOR_PREACH_MEETING_URI, null, null, null, null);
            ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRsps = new ArrayList<PreachmeetingConciseRsp>();
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    PreachmeetingConciseRsp preachmeetingConciseRsp = new PreachmeetingConciseRsp();
                    preachmeetingConciseRsp.preachMeetingId = cursor.getInt(cursor.getColumnIndex("preach_meeting_id"));
                    preachmeetingConciseRsp.readTimes = cursor.getInt(cursor.getColumnIndex("read_times"));
                    preachmeetingConciseRsp.addTime = cursor.getString(cursor.getColumnIndex("add_time"));
                    preachmeetingConciseRsp.runningTimeBegin = cursor.getString(cursor.getColumnIndex("running_time_begin"));
                    preachmeetingConciseRsp.runningTimeEnd = cursor.getString(cursor.getColumnIndex("preach_meeting_id"));
                    preachmeetingConciseRsp.runningUniversity = cursor.getString(cursor.getColumnIndex("preach_meeting_id"));
                    preachmeetingConciseRsp.runPlace = cursor.getString(cursor.getColumnIndex("preach_meeting_id"));
                    preachmeetingConciseRsp.logoPath = cursor.getString(cursor.getColumnIndex("preach_meeting_id"));
                    preachmeetingConciseRsps.add(preachmeetingConciseRsp);
                }
            }
            progressBar.setIndeterminate(false);
            TextView progressTextView = (TextView) rootView.findViewById(R.id.preach_meeting_list_fragment_progress_textView);
            progressTextView.setVisibility(View.GONE);
            for (int i = 0; i < preachmeetingConciseRspArrayListExternal.size(); ++i) {
                if (preachMeetingIsInList(preachmeetingConciseRspArrayListExternal.get(i), preachmeetingConciseRsps)) {
                    preachmeetingConciseRspArrayListExternal.get(i).isFavorite = true;
                }
            }

            if (cursor != null) cursor.close();
            preachmeetingConciseRspArrayList.clear();
            preachmeetingConciseRspArrayList.addAll(preachmeetingConciseRspArrayListExternal);
            ((UniversityPreachingMeetListFragment) getParentFragment()).pagerData.put(getPageNumber(), preachmeetingConciseRspArrayList);
            progressBar.setVisibility(View.GONE);
            progressTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            universityPreachListAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        UniversityPreachingMeetListFragment universityPreachingMeetListFragment = (UniversityPreachingMeetListFragment)getParentFragment();
        if (preachmeetingConciseRspArrayList.size() == 0) {
            if (universityPreachingMeetListFragment.pagerData.get(getPageNumber()) != null) {
                preachmeetingConciseRspArrayList.clear();
                preachmeetingConciseRspArrayList.addAll(universityPreachingMeetListFragment.pagerData.get(getPageNumber()));
                progressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                universityPreachListAdapter.notifyDataSetChanged();
            } else {
                universityPreachingMeetListFragment.queryPreachmeetingList(mPageNumber);
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
        universityPreachListAdapter.notifyDataSetChanged();
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
