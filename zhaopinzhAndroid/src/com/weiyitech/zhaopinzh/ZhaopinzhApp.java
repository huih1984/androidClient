package com.weiyitech.zhaopinzh;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.baidu.frontia.FrontiaApplication;
import com.weiyitech.zhaopinzh.struct.CommentConcise;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-27
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhApp extends FrontiaApplication {
    public List experienceList;
    public List educationList;
    public List jobTypeList;
    public List runningIntervalList;
    public List companyTypeList;
    public List companyScaleList;
    public List industryList;
    public Typeface robotoCondenseBoldFont;
    public Typeface robotoCondenseLightFont;
    public String activeActivity;
    public Map<List<Integer>, Integer> employerDetailClickedMap = new HashMap<List<Integer>, Integer>();
    public int jobActivityPageIndex;
    public float hardDensity;
    public int xdpi;
    public int ydpi;
    public int widthPixels;
    public Map<Integer, String> standPicMap = new HashMap<Integer, String>();
    public List<CommentConcise> commentConciseList = new ArrayList<CommentConcise>();
    private static ZhaopinzhApp instance;

    public static ZhaopinzhApp getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        experienceList = Common.getAssetList(this, "experienceyear.json");
        educationList = Common.getAssetList(this, "educationlevel.json");
        companyTypeList = Common.getAssetList(this, "companytype.json");
        companyScaleList = Common.getAssetList(this, "companyscale.json");
        industryList = Common.getAssetList(this, "industry.json");
        jobTypeList = Common.getAssetList(this, "jobtype.json");
        runningIntervalList = Common.getAssetList(this, "runningInterval.json");
//        robotoCondenseBoldFont = Typeface.createFromAsset(getAssets(), "DroidSansFallback.ttf");
//        robotoCondenseLightFont = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Light.ttf");
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        hardDensity = displayMetrics.density;
        xdpi = (int) displayMetrics.xdpi;
        ydpi = (int) displayMetrics.ydpi;
        widthPixels = displayMetrics.widthPixels;
        instance = this;
//        SDKInitializer.initialize(this);
       // String dev = getDeviceInfo(this);
        super.onCreate();
    }


    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    private ConcurrentHashMap map = new ConcurrentHashMap();

    public void addActivityToMap(Context context) {
        map.put(context.getClass().getName(), context);
    }

    public boolean activityInMap(String activityName) {
        return map.get(activityName) != null;
    }

    public Context getActivtiyInMap(String activityName) {
        if (activityInMap(activityName)) {
            return (Context) map.get(activityName);
        }
        return null;
    }

    public void removeActivityFromMap(String activityName) {
        map.remove(activityName);
    }

    public boolean getTotalTimeout = false;
    public boolean runEmployerRspTimeout = false;
    public boolean jobDetailReqTimeout = false;
    public boolean jobConciseRspTimeout = false;
    public boolean employerDetailReqTimeout = false;

    public boolean versionReqTimeout = false;

    public boolean standRspTimeout = false;

    public boolean trafficRspTimeout = false;

    public boolean fairRspTimeout = false;
    public boolean fairDetailRspTimeout = false;
    public boolean campusTimeout = false;

    public String getActiveActivity() {
        return activeActivity;
    }

    public void setActiveActivity(String activeActivity) {
        this.activeActivity = activeActivity;
    }
}
