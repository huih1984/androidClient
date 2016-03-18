package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.*;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.*;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.SearchSettingListAdapter;
import com.weiyitech.zhaopinzh.presentation.adapter.SearchHistoryAdapter;
import com.weiyitech.zhaopinzh.presentation.component.DictionaryDatabase;
import com.weiyitech.zhaopinzh.presentation.component.DictionaryProvider;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.SearchConditionUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-21
 * Time: 下午6:14
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends CommonActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, AdapterView.OnItemSelectedListener {
    public static final int SEARCH_SETTING_REQ_FLAG = 1008;
    SearchView searchView;
    List<Map<String, ?>> searchConditionList;
    SearchSettingListAdapter searchSettingListAdapter;
    ListView searchHistoryListView;
    SearchHistoryAdapter searchHistoryAdapter;
    ArrayList<String> searchHistoryList;
    ListView searchConditionListView;
    String fromWhere;
    Spinner modelSpinner;

    int searchModel = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        searchView = (SearchView) findViewById(R.id.search_activity_search_view);
        SearchManager searchManager =
                (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        modelSpinner = (Spinner) findViewById(R.id.search_activity_spinner);
        String[] items = {"网络招聘", "校园招聘", "人才市场"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        modelSpinner.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setOnItemSelectedListener(this);
        fromWhere = getIntent().getStringExtra("where");
        setSearchSpinner();

        searchConditionListView = (ListView) findViewById(R.id.search_activity_search_condition_listView);
        searchConditionList = Common.getSearchSettingList(this);
        searchSettingListAdapter = new SearchSettingListAdapter(this, searchConditionList);
        searchConditionListView.setAdapter(searchSettingListAdapter);
        searchHistoryListView = (ListView) findViewById(R.id.search_activity_search_history_listView);
        searchHistoryList = new ArrayList<String>();
        searchHistoryAdapter = new SearchHistoryAdapter(this, searchHistoryList);
        searchHistoryListView.setAdapter(searchHistoryAdapter);

        searchHistoryListView.setVisibility(View.GONE);
    }

    void setSearchSpinner() {
        if (fromWhere != null) {
            if (fromWhere.equals(NetJobConciseListActivity.class.getName())) {
                modelSpinner.setSelection(0);
            } else if (fromWhere.equals(CampusActivity.class.getName())) {
                modelSpinner.setSelection(1);
            } else if (fromWhere.equals(HomeActivity.class.getName())) {
                modelSpinner.setSelection(2);
            }
        } else {
            modelSpinner.setSelection(SearchConditionUtil.getIntSetting("search_modal"));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchModel = position;
        SearchConditionUtil.setIntSetting("search_modal", position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            Intent wordIntent = new Intent();
            setIntentByFromWhere(wordIntent);
            wordIntent.setData(intent.getData());
            startActivity(wordIntent);
            //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            ContentResolver resolver = getContentResolver();
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            ContentValues contentValues = new ContentValues();
            contentValues.put(DictionaryDatabase.ADDTIME, date);
            contentValues.put(DictionaryDatabase.INPUTTEXT, query);
            resolver.insert(DictionaryProvider.CONTENT_URI, contentValues);
            Intent wordIntent = new Intent();
            setIntentByFromWhere(wordIntent);
            wordIntent.putExtra("keyword", query);
            startActivity(wordIntent);
            //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_SETTING_REQ_FLAG) {
            //Common.checkPref(this);
            searchConditionList.clear();
            searchConditionList.addAll(Common.getSearchSettingList(this));
            searchSettingListAdapter.notifyDataSetChanged();
        }
    }

    /*创建菜单*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    void setIntentByFromWhere(Intent intent) {
        intent.putExtra("where", SearchActivity.class.getName());
        if (modelSpinner.getSelectedItemPosition() == 0) {
            intent.setClass(this, NetJobConciseListActivity.class);
        } else if (modelSpinner.getSelectedItemPosition() == 1) {
            intent.setClass(this, CampusActivity.class);
        } else if (modelSpinner.getSelectedItemPosition() == 2) {
            intent.setClass(this, HomeActivity.class);
        }
    }

    void startActivityByFromWhere() {
        Intent intent = null;
//        if (modelSpinner.getSelectedItemPosition() == 0) {
//            intent = new Intent(this, NetJobConciseListActivity.class);
//        } else if (modelSpinner.getSelectedItemPosition() == 1) {
//            intent = new Intent(this, CampusActivity.class);
//        } else if (modelSpinner.getSelectedItemPosition() == 2) {
//            intent = new Intent(this, HomeActivity.class);
//        }
        intent = new Intent(this, NetJobConciseListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivityByFromWhere();
                return true;
            case R.id.search_activity_action_search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivityByFromWhere();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int i) {
        return false;
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}