package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.dragsortlistview.DragSortListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-26
 * Time: 上午8:21
 * To change this template use File | Settings | File Templates.
 */
public class CampusFragmentOrderSettingActivity extends ListActivity {
    private ArrayAdapter<String> adapter;

    private String[] array;
    private ArrayList<String> list;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    String item=adapter.getItem(from);

                    adapter.notifyDataSetChanged();
                    adapter.remove(item);
                    adapter.insert(item, to);
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    adapter.remove(adapter.getItem(which));
                }
            };

    private DragSortListView.DragScrollProfile ssProfile =
            new DragSortListView.DragScrollProfile() {
                @Override
                public float getSpeed(float w, long t) {
                    if (w > 0.8f) {
                        // Traverse all views in a millisecond
                        return ((float) adapter.getCount()) / 0.001f;
                    } else {
                        return 10.0f * w;
                    }
                }
            };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_fragment_order_setting_activity);

        DragSortListView lv = (DragSortListView) getListView();
        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        lv.setDragScrollProfile(ssProfile);

        array = getResources().getStringArray(R.array.campus_activity_fragment_name_list);
        List<Integer> order = Common.getCampusActivityFragmentSettingOrder(this);
        list = new ArrayList<String>();
        list.add(array[order.get(0)]);
        list.add(array[order.get(1)]);
        list.add(array[order.get(2)]);

        adapter = new ArrayAdapter<String>(this, R.layout.list_item_handle_right, R.id.text, list);
        setListAdapter(adapter);
    }

    @Override
    protected void onStop() {
        List<Integer> order = new ArrayList<Integer>();
        Common.setCampusActivityFragmentSettingOrder(this, list);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}