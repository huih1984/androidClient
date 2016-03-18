package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.*;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.weibo.sina.AccessTokenKeeper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-23
 * Time: 下午7:01
 * To change this template use File | Settings | File Templates.
 */
public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    PreferenceScreen mPreferenceScreen;
    PreferenceScreen industryPreferenceScreen;
    PreferenceScreen jobTypePreferenceScreen;
    PreferenceScreen majorReqPreferenceScreen;
    PreferenceScreen homeShowPreferenceScreen;

    public boolean CHANGED_FLAG;
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(SettingActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ListView v = getListView();
        View footView = getLayoutInflater().inflate(R.layout.setting_activity, null);
        v.addFooterView(footView);
        Button button = (Button) footView.findViewById(R.id.clear_account_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(SettingActivity.this);
                if (oauth2AccessToken.isSessionValid()) {
                    new LogoutAPI(oauth2AccessToken).logout(mLogoutListener);
                }
                Util.clearSharePersistent(SettingActivity.this);
            }
        });
        mPreferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//        industryPreferenceScreen = (PreferenceScreen) findPreference("industry_setting");
//        jobTypePreferenceScreen = (PreferenceScreen) findPreference("job_type_setting");
//        majorReqPreferenceScreen = (PreferenceScreen) findPreference("major_req_setting");
        homeShowPreferenceScreen = (PreferenceScreen) findPreference("pref_key_home_show_setting");
//        addIndustryCheckBoxPreference();
//        addJobTypeCheckBoxPreference();
//        addMajorReqCheckBoxPreference();
//
//        addPublishListPreference();
//        addExpReqPreference();
//        addEduReqPreference();

        // addCompanyScalePreference();
        ZhaopinzhApp app = ((ZhaopinzhApp) getApplicationContext());
        app.addActivityToMap(this);
        CHANGED_FLAG = false;
    }


//    /**
//     * *****************************************************************************
//     */
//
//    /*动态添加行业类别的值选项*/
//    boolean addIndustryCheckBoxPreference() {
//
//        List<String> industryList = getIndustryList();
//        if (industryList != null) {
//            for (int i = 0; i < industryList.size(); ++i) {
//                CheckBoxPreference preference = new CheckBoxPreference(this);
//                preference.setKey(new String("industry" + i));
//                preference.setTitle(industryList.get(i));
//                preference.setDefaultValue(false);
//                industryPreferenceScreen.addPreference(preference);
//            }
//
//            industryPreferenceScreen.setSummary(getSelectedIndustries());
//        }
//        return true;
//    }
//
//    /*动态添加职位类型的值选项*/
//    boolean addJobTypeCheckBoxPreference() {
//
//        List<String> jobTypeList = getJobTypeList();
//        if (jobTypeList != null) {
//            for (int i = 0; i < jobTypeList.size(); ++i) {
//                CheckBoxPreference preference = new CheckBoxPreference(this);
//                preference.setKey(new String("jobtype" + i));
//                preference.setTitle(jobTypeList.get(i));
//                preference.setDefaultValue(false);
//                jobTypePreferenceScreen.addPreference(preference);
//            }
//
//            jobTypePreferenceScreen.setSummary(getSelectedJobType());
//        }
//        return true;
//    }
//
//    /*动态添加职位类型的值选项*/
//    boolean addMajorReqCheckBoxPreference() {
//
//        List<String> majorReqList = getMajorList();
//        if (majorReqList != null) {
//            for (int i = 0; i < majorReqList.size(); ++i) {
//                CheckBoxPreference preference = new CheckBoxPreference(this);
//                preference.setKey(new String("majortype" + i));
//                preference.setTitle(majorReqList.get(i));
//                preference.setDefaultValue(false);
//                majorReqPreferenceScreen.addPreference(preference);
//            }
//
//            majorReqPreferenceScreen.setSummary(getSelectedMajorReq());
//        }
//        return true;
//    }
//
//    /*动态添加发布时间的值选项*/
//    boolean addPublishListPreference() {
//        PreferenceCategory preferenceCategory = (PreferenceCategory) mPreferenceScreen.getPreference(0);
//        List<String> publishTimeList = getPublishTimeList();
//        if (publishTimeList != null) {
//            ListPreference preference = new ListPreference(this);
//            preference.setEntries(publishTimeList.toArray(new CharSequence[publishTimeList.size()]));
//            preference.setEntryValues(publishTimeList.toArray(new CharSequence[publishTimeList.size()]));
//            preference.setKey("publishtimelist");
//            preference.setDefaultValue("不限");
//            preference.setDialogTitle("发布时间");
//            preference.setTitle("发布时间");
//            preferenceCategory.addPreference(preference);
//            preference.setSummary(getSelectedPublishTime());
//        }
//        return true;
//    }
//
//    /*动态添加经验要求的值选项*/
//    boolean addExpReqPreference() {
//        PreferenceCategory preferenceCategory = (PreferenceCategory) mPreferenceScreen.getPreference(0);
//        List<String> list = getExpReqList();
//        if (list != null) {
//            ListPreference preference = new ListPreference(this);
//            preference.setEntries(list.toArray(new CharSequence[list.size()]));
//            preference.setEntryValues(list.toArray(new CharSequence[list.size()]));
//            preference.setKey("expreqlist");
//            preference.setDefaultValue("不限");
//            preference.setDialogTitle("经验要求");
//            preference.setTitle("经验要求");
//            preferenceCategory.addPreference(preference);
//            preference.setSummary(getSelectedExpReq());
//        }
//        return true;
//    }
//
//    /*动态添加学历要求的值选项*/
//    boolean addEduReqPreference() {
//        PreferenceCategory preferenceCategory = (PreferenceCategory) mPreferenceScreen.getPreference(0);
//        List<String> list = getEduReqList();
//        if (list != null) {
//            ListPreference preference = new ListPreference(this);
//            preference.setEntries(list.toArray(new CharSequence[list.size()]));
//            preference.setEntryValues(list.toArray(new CharSequence[list.size()]));
//            preference.setKey("edureqlist");
//            preference.setDefaultValue("不限");
//            preference.setDialogTitle("学历要求");
//            preference.setTitle("学历要求");
//            preferenceCategory.addPreference(preference);
//            preference.setSummary(getSelectedEduReq());
//        }
//        return true;
//    }
//
//    /*动态添加公司规模的值选项*/
//    boolean addCompanyScalePreference() {
//        PreferenceCategory preferenceCategory = (PreferenceCategory) mPreferenceScreen.getPreference(0);
//        List<String> list = getCompanyScaleList();
//        if (list != null) {
//            ListPreference preference = new ListPreference(this);
//            preference.setEntries(list.toArray(new CharSequence[list.size()]));
//            preference.setEntryValues(list.toArray(new CharSequence[list.size()]));
//            preference.setKey("companyscalelist");
//            preference.setDefaultValue("不限");
//            preference.setDialogTitle("公司规模");
//            preference.setTitle("公司规模");
//            preferenceCategory.addPreference(preference);
//            preference.setSummary(getSelectedCompanyScale());
//        }
//        return true;
//    }

    /**
     * ************************Common get CheckPrefs***************************************
     */
    String getSummaryOfCheckPrefs(String propertyName, List<String> propertyNames) {
        String returnStr = new String();
        boolean shouldEnabled = true;
        for (int i = 0; i < propertyNames.size(); ++i) {
            CheckBoxPreference preference = (CheckBoxPreference) findPreference(new String(propertyName + i));
            if (preference == null) {
                return "不限";
            }
            if (i == 0) {
                if (preference.isChecked()) {
                    shouldEnabled = false;
                }

            } else {
                if (!shouldEnabled) {
                    preference.setEnabled(false);
                } else {
                    preference.setEnabled(true);
                }
            }
            if (preference.isChecked()) {
                returnStr += propertyNames.get(i) + ", ";
            }
        }
        try {
            returnStr = returnStr.substring(0, returnStr.length() - 2);
            if (returnStr.contains("不限")) {
                returnStr = "不限";
            }
        } catch (Exception e) {
            Log.i("getSelectedIndustries", "取子字符串异常");
        }
        return returnStr;
    }

    /**
     * *****************************************************************************
     */

    void setCheckPrefsWhenChange0(String propertyName, int size) {
        if (propertyName == null) return;
        if (!propertyName.equals("industry0") && !propertyName.equals("jobtype0") && !propertyName.equals("majortype0")) {
            return;
        }
        boolean shouldEnabled = true;
        for (int i = 0; i < size; ++i) {
            CheckBoxPreference preference = (CheckBoxPreference) findPreference(new String(propertyName.substring(0, propertyName.length() - 1) + i));
            if (i == 0) {
                if (!preference.isChecked()) {
                    shouldEnabled = true;
                } else {
                    shouldEnabled = false;
                }
            } else {
                preference.setEnabled(shouldEnabled);
                if (shouldEnabled) {
                    preference.setChecked(false);
                }
            }
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        CHANGED_FLAG = true;
        synchronized (this) {
//            if (key.contains("industry")) {
//                setCheckPrefsWhenChange0(key, Common.INDUSTRYNUM);
//                String summary = getSelectedIndustries();
//                industryPreferenceScreen.setSummary(summary);
//                onContentChanged();
//            } else if (key.contains("jobtype")) {
//                setCheckPrefsWhenChange0(key, Common.JOBTYPENUM);
//                String summary = getSelectedCompanyType();
//                jobTypePreferenceScreen.setSummary(summary);
//                onContentChanged();
//            } else if (key.contains("majortype")) {
//                setCheckPrefsWhenChange0(key, Common.MAJORTYPENUM);
//                String summary = getSelectedMajorReq();
//                majorReqPreferenceScreen.setSummary(summary);
//                onContentChanged();
//            } else
            if (key.contains("list")) {
                ListPreference listPreference = (ListPreference) findPreference(key);
                listPreference.setSummary(listPreference.getValue());
            }

            if (key.equals("pref_key_portrait_setting")) {
                if (sharedPreferences.getString("pref_key_portrait_setting", "竖屏显示").equals("竖屏显示")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }         /*
            ListView listView = getListView();
            listView.setAdapter(listView.getAdapter());   */
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CHANGED_FLAG = false;
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZhaopinzhApp app = ((ZhaopinzhApp) getApplicationContext());
        app.removeActivityFromMap(this.getClass().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void goHome() {
        Intent intent = new Intent(this, TalkActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goHome();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome();
                break;
        }
        return true;
    }
}