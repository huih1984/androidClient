package com.weiyitech.zhaopinzh.presentation.component;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
//import com.umeng.analytics.MobclickAgent;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.CampusActivity;
import com.weiyitech.zhaopinzh.presentation.adapter.PreachFavoriteListAdapter;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.struct.SearchTerm;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-22
 * Time: 下午8:40
 * To change this template use File | Settings | File Templates.
 */
public class PreachingMeetFavoriteFragment extends Fragment {
    ListView listView;
    ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList;
    PreachFavoriteListAdapter preachFavoriteListAdapter;
    public SearchTerm searchTerm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里定义Fragment的布局
        View view = inflater.inflate(R.layout.preach_meeting_favorite_fragment, container, false);

        ActionBar actionBar = ((CampusActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        listView = (ListView) view.findViewById(R.id.preach_meeting_favorite_fragment_listView);


        preachmeetingConciseRspArrayList = new ArrayList<PreachmeetingConciseRsp>();
        MyDatabaseUtil.putPreachMeetingFromDataBase(preachmeetingConciseRspArrayList, getActivity());

        preachFavoriteListAdapter = new PreachFavoriteListAdapter(getActivity(), preachmeetingConciseRspArrayList);
        listView.setAdapter(preachFavoriteListAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite_fragment_actions, menu);
    }


    void deleteInvalidItems() {
        Iterator<PreachmeetingConciseRsp> iter = preachmeetingConciseRspArrayList.iterator();
        while (iter.hasNext()) {
            PreachmeetingConciseRsp preachmeetingConciseRsp = iter.next();
            if (!preachmeetingConciseRsp.favoriteValidMessage) {
                ContentResolver resolver = getActivity().getContentResolver();
                ContentValues contentValues = new ContentValues();
                String mSelectionClause = "preach_meeting_id = CAST(? AS INT)";
                String[] mSelectionArgs = {"" + preachmeetingConciseRsp.preachMeetingId};
                resolver.delete(MessageProvider.FAVOR_PREACH_MEETING_URI, mSelectionClause, mSelectionArgs);
                iter.remove();
            }
        }
        preachFavoriteListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite_fragment_delete:
                boolean haveInvalid = false;
                for (int i = 0; i < preachmeetingConciseRspArrayList.size(); ++i) {
                    if (!preachmeetingConciseRspArrayList.get(i).favoriteValidMessage) {
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}