package com.weiyitech.zhaopinzh.presentation.component;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
//import com.umeng.analytics.MobclickAgent;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.QueryFavoriteJob;
import com.weiyitech.zhaopinzh.business.QueryJob;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.FavoriteListAdapter;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;
import com.weiyitech.zhaopinzh.util.slideexpandablelistview.ExpandCollapseAnimation;
import com.weiyitech.zhaopinzh.util.slideexpandablelistview.SlideExpandableListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-27
 * Time: 上午8:47
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteListFragment extends Fragment implements BusinessInterface {
    ActionSlideExpandableListView listView;
    ArrayList<RunEmployerComponent> runEmployerComponentArrayList;
    FavoriteListAdapter favoriteListAdapter;
    public SearchTerm searchTerm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里定义Fragment的布局
        View view = inflater.inflate(R.layout.home_activity_favoritefragment, container, false);

        ActionBar actionBar = ((HomeActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        listView = (ActionSlideExpandableListView) view.findViewById(R.id.home_activity_favoritefragment_listView);

        setListViewExtraAction();

        runEmployerComponentArrayList = new ArrayList<RunEmployerComponent>();
        MyDatabaseUtil.putRunEmployerFromDataBase(runEmployerComponentArrayList, getActivity());

        favoriteListAdapter = new FavoriteListAdapter(getActivity(), runEmployerComponentArrayList);
        listView.setAdapter(favoriteListAdapter);
        return view;
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
                    List<JobConciseRsp> jobConciseRsps = runEmployerComponentArrayList.get(position).jobConciseRspList;
                    if (jobConciseRsps == null) {
                        JobConciseReq jobConciseReq = new JobConciseReq();
                        jobConciseReq.employerId = runEmployerComponentArrayList.get(position).runEmployer.employerId;
                        jobConciseReq.fairId = runEmployerComponentArrayList.get(position).runEmployer.fairId;
                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        Fragment fragment = homeActivity.getSupportFragmentManager().findFragmentByTag("favoritelist");
                        QueryFavoriteJob queryFavoriteJob = new QueryFavoriteJob();
                        queryFavoriteJob.queryJobConcise(jobConciseReq, fragment, position);
                        runEmployerComponentArrayList.get(position).expanded = true;
                    } else {
                        if (jobConciseRsps.size() == 0) {
                            Toast.makeText(getActivity(), "没有该企业的职位信息，请参考现场招聘海报！", 3000).show();
                        }
                       /* handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.setSelection(position);
                            }
                        }, 200); */
                    }


                } else {
                    favoriteListAdapter.detailClickedItem = -1;
                    runEmployerComponentArrayList.get(position).expanded = false;
                   /* handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 1000);  */
                }
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.enableExpandOnItemClick();
        return listView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite_fragment_actions, menu);
    }

    void deleteInvalidItems() {
        Iterator<RunEmployerComponent> iter = runEmployerComponentArrayList.iterator();
        while (iter.hasNext()) {
            RunEmployerComponent runEmployerComponent = iter.next();
            if (!runEmployerComponent.favoriteValidMessage) {
                ContentResolver resolver = getActivity().getContentResolver();
                ContentValues contentValues = new ContentValues();
                String mSelectionClause = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
                String[] mSelectionArgs = {"" + runEmployerComponent.runEmployer.employerId,
                        "" + runEmployerComponent.runEmployer.fairId};
                resolver.delete(MessageProvider.FAVOR_RUNEMPLOYER_URI, mSelectionClause, mSelectionArgs);
                resolver.delete(MessageProvider.STAND_URI, mSelectionClause, mSelectionArgs);
                iter.remove();
            }
        }
        favoriteListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite_fragment_delete:
                boolean haveInvalid = false;
                for (int i = 0; i < runEmployerComponentArrayList.size(); ++i) {
                    if (!runEmployerComponentArrayList.get(i).favoriteValidMessage) {
                        haveInvalid = true;
                        break;
                    }
                }
                if (haveInvalid) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("确定");
                    builder.setMessage("确定要删除过期的收藏职位吗？");
                    //当点确定按钮时从服务器上下载 新的apk 然后安装
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteInvalidItems();
                        }
                    });
                    //当点取消按钮时进行登录
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "没有过期的收藏职位需要删除,删除单条记录请在列表中删除！", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = null;
        Bundle bundle = new Bundle();
        message = new Message();
        if (dataType == Common.RUNEMPLOYER_TYPE) {
            /*将Object转换成message*/
            /*将业务层传递过来的数据，分发给不同的关键字，打包，同时将listivew中的数据刷新*/
            RunEmployerPager runEmployerPager = (RunEmployerPager) t;
            int queryPage = runEmployerPager.pager.current / Common.ONEPAGEJOBS;
            List<RunEmployer> runEmployers = runEmployerPager.list;
            ArrayList<RunEmployer> toPaceableList = new ArrayList<RunEmployer>();
            toPaceableList.addAll(runEmployers);

            bundle.putInt("type", Common.RUNEMPLOYER_TYPE);
            bundle.putParcelable("pager", runEmployerPager.pager);
            bundle.putParcelableArrayList("list", toPaceableList);
            bundle.putInt("uipage", queryPage);
        } else if (dataType == Common.JOBCONSICE_TYPE) {
            ArrayList<JobConciseRsp> jobConciseRsps = (ArrayList<JobConciseRsp>) t;
            bundle.putInt("type", Common.JOBCONSICE_TYPE);
            bundle.putInt("position", position[0]);
            bundle.putParcelableArrayList("list", jobConciseRsps);
        } else if (dataType == Common.JOBDETAIL_TYPE) {
            bundle.putInt("type", Common.JOBDETAIL_TYPE);
            bundle.putInt("position", position[0]);
            bundle.putParcelable("detail", (JobDetailRsp) t);
        } else if (dataType == Common.TIMEOUT_TYPE) {
            bundle.putInt("type", Common.TIMEOUT_TYPE);
            bundle.putInt("req", (Integer) t);
            if (QueryJob.JOBDETAILREQ.equals(t)) {
                bundle.putInt("position", position[0]);
            }
        } else if (dataType == Common.EMPLOYERDETAIL_TYPE) {
            bundle.putInt("type", Common.EMPLOYERDETAIL_TYPE);
            bundle.putInt("position", position[0]);
            bundle.putParcelable("detail", (EmployerDetailRsp) t);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * @param jobDetailReq
     */
    public void queryJobDetail(JobDetailReq jobDetailReq) {
        QueryJob queryJob = new QueryJob();
        queryJob.queryJobDetail(jobDetailReq, getActivity());
    }

    public void updateJobConciseView(List<JobConciseRsp> jobConciseRsps, final int position) {
        try {
            runEmployerComponentArrayList.get(position).jobConciseRspList = (ArrayList<JobConciseRsp>) jobConciseRsps;
            favoriteListAdapter.notifyDataSetChanged();
            if (runEmployerComponentArrayList.get(position).jobConciseRspList.size() == 0) {
                Toast.makeText(getActivity(), "没有该企业的职位信息，请参考现场招聘海报！", 3000).show();
            }
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (position < listView.getChildCount() - 1) {
//                        listView.setSelection(position);
//                    }
//                }
//            }, 200);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 句柄处理界面刷新事件
     */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //此处可以更新UI
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.JOBCONSICE_TYPE) {
                ArrayList<JobConciseRsp> jobConciseRsps = bundle.getParcelableArrayList("list");
                updateJobConciseView(jobConciseRsps, bundle.getInt("position"));
            } else if (dataType == Common.EMPLOYERDETAIL_TYPE) {
            } else if (dataType == Common.TIMEOUT_TYPE) {

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart("FavoriteListFragment"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd("FavoriteListFragment");
    }
}
