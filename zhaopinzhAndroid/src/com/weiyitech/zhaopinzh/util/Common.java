package com.weiyitech.zhaopinzh.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.filemanager.FileHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-28
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
public class Common {
    public static final int ONEPAGEJOBS = 15;
    public static final int BASE_INT = 1000;
    public static final int JOBCONSICE_TYPE = BASE_INT + 1;
    public static final int JOBDETAIL_TYPE = BASE_INT + 2;
    public static final int TIMEOUT_TYPE = BASE_INT + 3;
    public static final int NET_WORK_DISCONNECTED = BASE_INT + 35;
    public static final int VERSION_QUERY_TYPE = BASE_INT + 4;
    public static final int VERSION_QUERY_NO_UPDATE_TYPE = BASE_INT + 6;
    public static final int APK_TYPE = BASE_INT + 5;
    public static final int EMPLOYERDETAIL_TYPE = BASE_INT + 7;
    public static final int STANDPIC_DETAIL_TYPE = BASE_INT + 12;
    public static final int STANDPIC_GOT_PIC_TYPE = BASE_INT + 13;
    public static final int TRAFFIC_DETAIL_TYPE = BASE_INT + 8;
    public static final int ADD_PAGE_INDEX_TYPE = BASE_INT + 9;
    public static final int JOBTOTAL_TYPE = BASE_INT + 10;
    public static final int RUNEMPLOYER_TYPE = BASE_INT + 11;
    public static final int JOBFAIRCONCISE_TYPE = BASE_INT + 14;
    public static final int JOBFAIRDETAIL_TYPE = BASE_INT + 15;
    public static final int PREACH_MEETING_UNIVERSITY_TYPE = BASE_INT + 16;
    public static final int PREACH_MEETING_LIST_TYPE = BASE_INT + 17;
    public static final int PREACH_MEETING_TYPE = BASE_INT + 18;
    public static final int PREACH_MEETING_TOTAL_TYPE = BASE_INT + 19;
    public static final int FAIRTOTAL_TYPE = BASE_INT + 20;
    public static final int LOCATION_IN_MAP_TYPE = BASE_INT + 21;
    public static final int WEB_JOB_TYPE = BASE_INT + 22;
    public static final int NETJOBTOTAL_TYPE = BASE_INT + 23;
    public static final int NETJOBCONCISE_TYPE = BASE_INT + 24;
    public static final int NETJOBDETAIL_TYPE = BASE_INT + 25;
    public static final int PREACH_MEETING_IDS_TYPE = BASE_INT + 26;
    public static final int COMMENT_TYPE = BASE_INT + 27;
    public static final int USER_TYPE = BASE_INT + 28;
    public static final int COMMENT_TOTAL_TYPE = BASE_INT + 29;
    public static final int COMMENT_AVARAGE_TYPE = BASE_INT + 30;
    public static final int RATING_TYPE = BASE_INT + 31;
    public static final int ADVERTISEMENT_EMPLOYER_TYPE = BASE_INT + 32;
    public static final int ZHAOPINZH_JOB_DETAIL_TYPE = BASE_INT + 33;
    public static final int ZHAOPINZH_JOB_CONCISE_TYPE = BASE_INT + 34;
    public static final String prefFile = "com.weiyitech.zhaopinzh.presentation_preferences";
    public static final int INDUSTRYNUM = 38;
    public static final int JOBTYPENUM = 40;
    public static final int MAJORTYPENUM = 12;
    public static final int INDUSTRY_TYPE = BASE_INT + 41;
    public static final int JOB_TYPE = BASE_INT + 42;
    public static final int PUBLISH_TYPE = BASE_INT + 43;
    public static final int EXPERIENCE_TYPE = BASE_INT + 44;
    public static final int EDUCATION_TYPE = BASE_INT + 45;
    public static final String URL ="http://www.maojuren.com/ms-android";//"http://www.work020.com/ms-android";//
    public static final int TIMEOUTSECONDS = 30000;
    public static final int QUERYVERSIONTIMEOUTSECONDS = 3000;

    // "http://192.168.1.110:8080/ms-android";
    public static HashMap<String, String> objToHash(Object obj) throws IllegalArgumentException, IllegalAccessException {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        Class clazz = obj.getClass();
        List<Class> clazzs = new ArrayList<Class>();

        do {
            clazzs.add(clazz);
            clazz = clazz.getSuperclass();
        } while (!clazz.equals(Object.class));

        for (Class iClazz : clazzs) {
            Field[] fields = iClazz.getDeclaredFields();
            for (Field field : fields) {
                Object objVal = null;
                field.setAccessible(true);
                objVal = field.get(obj);
                /*将objVal转换成String*/
                hashMap.put(field.getName(), objVal.toString());
            }
        }

        return hashMap;
    }

    //带编码的
    public static String inputStream2String(InputStream in, String encoding) throws Exception {
        StringBuffer out = new StringBuffer();
        InputStreamReader inread = new InputStreamReader(in, encoding);

        char[] b = new char[4096];
        for (int n; (n = inread.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }

        return out.toString();
    }


    public static ArrayList toList(JSONArray array) throws JSONException {
        ArrayList list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            if (array.get(i) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                list.add(jsonObject.keys().next().toString());
            } else if (array.get(i) instanceof String) {
                list.add(array.get(i));
            }
        }

        return list;
    }

    /*获取资产文件夹下文件的字符串*/
    public static String getAssetFileString(Context context, String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = null;
            inputStream = assetManager.open(fileName);
            String string = Common.inputStream2String(inputStream, "utf8");
            return string;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /**
     * 读取本地文件中JSON字符串
     *
     * @param fileName
     * @return
     */
    public static String getJson(String fileName, Context context) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /*获取经验要求数组*/
    public static List<String> getAssetList(Context context, String fileName) {
        try {
            String jsonStr = getJson(fileName, context); //getAssetFileString(context, fileName);
            JSONArray jsongArray = new JSONArray(jsonStr);
            ArrayList<String> list = Common.toList(jsongArray);
            return list;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    static void addPrefVariables(Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        for (int i = 0; i < INDUSTRYNUM; ++i) {
            editor.putBoolean("industry" + i, false);
        }
        for (int i = 0; i < JOBTYPENUM; ++i) {
            editor.putBoolean("jobtype" + i, false);
        }
        for (int i = 0; i < MAJORTYPENUM; ++i) {
            editor.putBoolean("majortype" + i, false);
        }
        editor.putBoolean("addedPrefs", true);
        editor.commit();
    }

    public static void setUserIdToPrefs(Context context, int userId) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("user_id", userId);
        editor.commit();
    }

    public static int getUserIdFromPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        return settings.getInt("user_id", 0);
    }

    public static void setJobsDropDownPref(Context context, int position) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.getInt("dropdown", -1) == -1) {
            editor.putInt("dropdown", 0);
        }
        editor.putInt("dropdown", position);
        editor.commit();
    }

    public static int getJobsDropDownPref(Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        return settings.getInt("dropdown", 0);
    }

    public static void setNetJobsDropDownPref(Context context, int position) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.getInt("net_job_dropdown", -1) == -1) {
            editor.putInt("net_job_dropdown", 0);
        }
        editor.putInt("net_job_dropdown", position);
        editor.commit();
    }

    public static int getNetJobsDropDownPref(Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        return settings.getInt("net_job_dropdown", 1);
    }

    public static void setPreachMeetingDropDownPref(Context context, int position) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.getInt("preach_meeting_dropdown", -1) == -1) {
            editor.putInt("preach_meeting_dropdown", 0);
        }
        editor.putInt("preach_meeting_dropdown", position);
        editor.commit();
    }

    public static int getPreachMeetingDropDownPref(Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        return settings.getInt("preach_meeting_dropdown", 0);
    }

    public static void resetSettingValue(Context context) {
        FileHelper.deleteDirectory("/data/data/com.weiyitech.zhaopinzh.presentation" + "/shared_prefs/"
                + prefFile + ".xml");
    }

    public static int verEquals(String ver1, String ver2) {
        String[] ver1s = ver1.split("\\.");
        String[] ver2s = ver2.split("\\.");
        for (int i = 0; i < ver1s.length; ++i) {
            if (Integer.valueOf(ver1s[i]) > Integer.valueOf(ver2s[i])) {
                return 1;
            } else if (Integer.valueOf(ver1s[i]) < Integer.valueOf(ver2s[i])) {
                return -1;
            }
        }
        return 0;
    }

    public static String getIsUNSpecified(Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pref_key_portrait_setting", "竖屏显示");
        return settings.getString("pref_key_portrait_setting", "竖屏显示");
    }

    public static List<Integer> getHomeActivityFragmentSettingOrder(Context context) {
        ArrayList<Integer> orderList = new ArrayList<Integer>();
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        orderList.add(settings.getInt("first_fragment", 0));
        orderList.add(settings.getInt("second_fragment", 1));
        orderList.add(settings.getInt("third_fragment", 2));
        return orderList;
    }

    public static void setHomeActivityFragmentSettingOrder(Context context, List<String> stringList) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        String[] array = context.getResources().getStringArray(R.array.home_activity_fragment_name_list);
        Integer[] order = new Integer[3];
        for (int i = 0; i < stringList.size(); ++i) {
            for (int j = 0; j < array.length; ++j) {
                if (stringList.get(i).equals(array[j])) {
                    order[i] = j;
                    break;
                }
            }
        }
        editor.putInt("first_fragment", order[0]);
        editor.putInt("second_fragment", order[1]);
        editor.putInt("third_fragment", order[2]);
        editor.commit();
    }

    public static List<Integer> getCampusActivityFragmentSettingOrder(Context context) {
        ArrayList<Integer> orderList = new ArrayList<Integer>();
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        orderList.add(settings.getInt("campus_first_fragment", 0));
        orderList.add(settings.getInt("campus_second_fragment", 1));
        orderList.add(settings.getInt("campus_third_fragment", 2));
        return orderList;
    }

    public static void setCampusActivityFragmentSettingOrder(Context context, List<String> stringList) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        String[] array = context.getResources().getStringArray(R.array.campus_activity_fragment_name_list);
        Integer[] order = new Integer[3];
        for (int i = 0; i < stringList.size(); ++i) {
            for (int j = 0; j < array.length; ++j) {
                if (stringList.get(i).equals(array[j])) {
                    order[i] = j;
                    break;
                }
            }
        }
        editor.putInt("campus_first_fragment", order[0]);
        editor.putInt("campus_second_fragment", order[1]);
        editor.putInt("campus_third_fragment", order[2]);
        editor.commit();
    }

    public static List<Integer> getNetActivityFragmentSettingOrder(Context context) {
        ArrayList<Integer> orderList = new ArrayList<Integer>();
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        orderList.add(settings.getInt("net_first_fragment", 0));
        orderList.add(settings.getInt("net_second_fragment", 1));
        return orderList;
    }

    public static void setNetActivityFragmentSettingOrder(Context context, List<String> stringList) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        SharedPreferences.Editor editor = settings.edit();
        String[] array = context.getResources().getStringArray(R.array.net_job_concise_activity_fragment_name_list);
        Integer[] order = new Integer[2];
        for (int i = 0; i < stringList.size(); ++i) {
            for (int j = 0; j < array.length; ++j) {
                if (stringList.get(i).equals(array[j])) {
                    order[i] = j;
                    break;
                }
            }
        }
        editor.putInt("net_first_fragment", order[0]);
        editor.putInt("net_second_fragment", order[1]);
        editor.commit();
    }

    /*从手机中获取设置*/
    public static SearchTerm getSearchSettings(Context context) {
        SearchTerm searchTerm = new SearchTerm();
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);

        if (!settings.getBoolean("addedPrefs", false)) {
            addPrefVariables(context);
        }

        if (!settings.getBoolean("industry0", false)) {
            for (int i = 1; i <= INDUSTRYNUM - 1; ++i) {
                String key = new String("industry" + i);
                Boolean industryBool = settings.getBoolean(key, true);
                if (industryBool) searchTerm.industryTypes.add(Integer.valueOf(i));
            }
        }

        if (!settings.getBoolean("jobtype0", false)) {
            for (int i = 1; i <= JOBTYPENUM - 1; ++i) {
                String key = new String("jobtype" + i);
                Boolean jobTypeTypeBool = settings.getBoolean(key, true);
                if (jobTypeTypeBool) searchTerm.jobTypes.add(Integer.valueOf(i));
            }
        }

        if (!settings.getBoolean("majortype0", false)) {
            for (int i = 1; i <= MAJORTYPENUM - 1; ++i) {
                String key = new String("majortype" + i);
                Boolean majorTypeBool = settings.getBoolean(key, true);
                if (majorTypeBool) searchTerm.majorReqs.add(Integer.valueOf(i));
            }
        }

        List list = getAssetList(context, "runningInterval.json");
        searchTerm.runningInterval = list.indexOf(settings.getString("runninginterval", "时间不限"));

        list = getAssetList(context, "experienceyear.json");
        searchTerm.expReq = list.indexOf(settings.getString("expreqlist", "经验不限"));

        list = getAssetList(context, "educationlevel.json");
        searchTerm.eduReq = list.indexOf(settings.getString("edureqlist", "学历不限"));

        return searchTerm;
    }

    public static List getSearchSettingList(Context context) {
        SharedPreferences settings = context.getSharedPreferences(prefFile, 0);
        List<Map<String, ?>> summaryList = new ArrayList<Map<String, ?>>();

        List list = getAssetList(context, "industry.json");
        String mStr = "";
        for (int i = 0; i < INDUSTRYNUM; ++i) {
            if (settings.getBoolean("industry" + i, true)) {
                mStr += " " + list.get(i);
            }
        }
        if (mStr.contains("不限") || mStr.equals("")) {
            mStr = "所有行业";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("行业", mStr);
        summaryList.add(map);

        list = getAssetList(context, "jobtype.json");
        mStr = "";
        for (int i = 0; i < JOBTYPENUM; ++i) {
            if (settings.getBoolean("jobtype" + i, true)) {
                mStr += " " + list.get(i);
            }
        }
        if (mStr.contains("不限") || mStr.equals("")) {
            mStr = "所有职位";
        }
        map = new HashMap<String, String>();
        map.put("职位", mStr);
        summaryList.add(map);

//        list = getAssetList(context, "major.json");
//        mStr = "";
//        for (int i = 0; i < MAJORTYPENUM; ++i) {
//            if (settings.getBoolean("majortype" + i, true)) {
//                mStr += " " + list.get(i);
//            }
//        }
//        if (mStr.contains("不限")) {
//            mStr = "专业不限";
//        }
//        map = new HashMap<String, String>();
//        map.put("专业", mStr);
//        summaryList.add(map);

        map = new HashMap<String, String>();
        map.put("发布时间", settings.getString("runninginterval", "不限"));
        summaryList.add(map);

        map = new HashMap<String, String>();
        map.put("经验要求", settings.getString("expreqlist", "不限"));
        summaryList.add(map);

        map = new HashMap<String, String>();
        map.put("学历要求", settings.getString("edureqlist", "不限"));
        summaryList.add(map);
        return summaryList;
    }

    public static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    public static void checkPref(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Common.prefFile, 0);
        boolean noSelected = true;
        for (int i = 0; i < INDUSTRYNUM; ++i) {
            String key = new String("industry" + i);
            Boolean industryBool = settings.getBoolean(key, true);
            if (industryBool) {
                noSelected = false;
                break;
            }
        }
        if (noSelected) {
            Toast.makeText(context.getApplicationContext(), "您行业类别一个也没选！自动为您设置为不限",
                    Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("industry0", true);
            editor.commit();
        }

        noSelected = true;
        for (int i = 0; i < JOBTYPENUM; ++i) {
            String key = new String("jobtype" + i);
            Boolean bl = settings.getBoolean(key, true);
            if (bl) {
                noSelected = false;
                break;
            }
        }
        if (noSelected) {
            Toast.makeText(context.getApplicationContext(), "您职位类型一个也没选！自动为您设置为不限",
                    Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("jobtype0", true);
            editor.commit();
        }

        noSelected = true;
        for (int i = 0; i < JOBTYPENUM; ++i) {
            String key = new String("majortype" + i);
            Boolean bl = settings.getBoolean(key, true);
            if (bl) {
                noSelected = false;
                break;
            }
        }
        if (noSelected) {
            Toast.makeText(context.getApplicationContext(), "您专业要求一个也没选！自动为您设置为不限",
                    Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("majortype0", true);
            editor.commit();
        }
    }

    public static boolean compareIntegerList(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            return false;
        } else {
            for (int i = 0; i < a.size(); ++i) {
                if (a.get(i) != b.get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void putRunEmployer(ContentValues contentValues, RunEmployer runEmployer) {
        contentValues.put("fair_id", runEmployer.fairId);
        contentValues.put("hall_id", runEmployer.hallId);
        contentValues.put("employer_id", runEmployer.employerId);
        if (runEmployer.employerName != null) contentValues.put("employer_name", runEmployer.employerName);
        if (runEmployer.runningTime != null) contentValues.put("running_time", runEmployer.runningTime);
        if (runEmployer.fairName != null) contentValues.put("fair_name", runEmployer.fairName);
        if (runEmployer.hallName != null) contentValues.put("hall_name", runEmployer.hallName);
        if (runEmployer.logoPath != null) contentValues.put("logo_path", runEmployer.logoPath);
        contentValues.put("read_times", runEmployer.readTimes);
        contentValues.put("stand_pic_id", runEmployer.standPicId);
        contentValues.put("job_counts", runEmployer.jobCounts);
    }

    public static void putJobDetail(ContentValues contentValues, JobDetailRsp jobDetailRsp) {
        contentValues.put("job_id", jobDetailRsp.jobId);
        contentValues.put("fair_id", jobDetailRsp.fairId);
        contentValues.put("employer_id", jobDetailRsp.employerId);
        if (jobDetailRsp.jobName != null) contentValues.put("job_name", jobDetailRsp.jobName);
        if (jobDetailRsp.skillReq != null) contentValues.put("skill_req", jobDetailRsp.skillReq);
        contentValues.put("exp_req", jobDetailRsp.expReq);
        if (jobDetailRsp.majorReq != null) contentValues.put("major_req", jobDetailRsp.majorReq);
        if (jobDetailRsp.eduReq != null) contentValues.put("edu_req", jobDetailRsp.eduReq);
        if (jobDetailRsp.resp != null) contentValues.put("resp", jobDetailRsp.resp);
        if (jobDetailRsp.skillReq != null) contentValues.put("skill_req", jobDetailRsp.skillReq);
        if (jobDetailRsp.payment != null) contentValues.put("payment", jobDetailRsp.payment);
        if (jobDetailRsp.workplace != null) contentValues.put("workplace", jobDetailRsp.workplace);
        if (jobDetailRsp.sexReq != null) contentValues.put("sex_req", jobDetailRsp.sexReq);
        contentValues.put("job_type", jobDetailRsp.jobType);
    }

    public static void putEmployer(ContentValues contentValues, EmployerDetailRsp employerDetailRsp) {
        contentValues.put("employer_id", employerDetailRsp.employerId);
        if (employerDetailRsp.employerName != null) contentValues.put("employer_name", employerDetailRsp.employerName);
        contentValues.put("type", employerDetailRsp.type);
        contentValues.put("scale", employerDetailRsp.scale);
        if (employerDetailRsp.hrEmail != null) contentValues.put("hr_email", employerDetailRsp.hrEmail);
        if (employerDetailRsp.website != null) contentValues.put("website", employerDetailRsp.website);
        if (employerDetailRsp.desc != null) contentValues.put("desc", employerDetailRsp.desc);
        if (employerDetailRsp.address != null) contentValues.put("address", employerDetailRsp.address);
        contentValues.put("province", employerDetailRsp.province);
        contentValues.put("city", employerDetailRsp.city);
        if (employerDetailRsp.tel != null) contentValues.put("tel", employerDetailRsp.tel);
        if (employerDetailRsp.logoPath != null) contentValues.put("logo_path", employerDetailRsp.logoPath);
        if (employerDetailRsp.bigLogoPath != null) contentValues.put("big_logo_path", employerDetailRsp.bigLogoPath);
    }

    public static void putStand(ContentValues contentValues, Stand stand) {
        contentValues.put("employer_id", stand.employerId);
        contentValues.put("fair_id", stand.fairId);
        if (stand.standNumber != null) contentValues.put("stand_number", stand.standNumber);
    }

    public static void putPreachMeeting(ContentValues contentValues, PreachmeetingConciseRsp preachmeetingConciseRsp) {
        contentValues.put("preach_meeting_id", preachmeetingConciseRsp.preachMeetingId);
        if (preachmeetingConciseRsp.preachMeetingName != null)
            contentValues.put("preach_meeting_name", preachmeetingConciseRsp.preachMeetingName);
        if (preachmeetingConciseRsp.employerName != null)
            contentValues.put("employer_name", preachmeetingConciseRsp.employerName);
        if (preachmeetingConciseRsp.runningTimeBegin != null)
            contentValues.put("running_time_begin", preachmeetingConciseRsp.runningTimeBegin);
        if (preachmeetingConciseRsp.runningTimeEnd != null)
            contentValues.put("running_time_end", preachmeetingConciseRsp.runningTimeEnd);
        if (preachmeetingConciseRsp.runPlace != null) contentValues.put("run_place", preachmeetingConciseRsp.runPlace);
        if (preachmeetingConciseRsp.runningUniversity != null)
            contentValues.put("running_university", preachmeetingConciseRsp.runningUniversity);
        contentValues.put("read_times", preachmeetingConciseRsp.readTimes);
        if (preachmeetingConciseRsp.logoPath != null) contentValues.put("logo_path", preachmeetingConciseRsp.logoPath);
        if (preachmeetingConciseRsp.addTime != null) contentValues.put("add_time", preachmeetingConciseRsp.addTime);
        if (preachmeetingConciseRsp.updateTime != null)
            contentValues.put("update_time", preachmeetingConciseRsp.updateTime);
    }

    public static void putLocationInMap(ContentValues contentValues, LocationInMapRsp locationInMapRsp) {
        contentValues.put("location_id", locationInMapRsp.locationId);
        contentValues.put("latitude", locationInMapRsp.latitude);
        contentValues.put("longitude", locationInMapRsp.longitude);
        if (locationInMapRsp.address != null)
            contentValues.put("address", locationInMapRsp.address);
        contentValues.put("add_time", locationInMapRsp.addTime);
    }

    public static void putNetJobDetail(ContentValues contentValues, JobConciseNetRsp jobConciseNetRsp) {
        contentValues.put("job_id", jobConciseNetRsp.jobId);
        contentValues.put("employer_id", jobConciseNetRsp.employerId);
        if (jobConciseNetRsp.jobName != null) contentValues.put("job_name", jobConciseNetRsp.jobName);
        if (jobConciseNetRsp.getPayment() != null) contentValues.put("payment", jobConciseNetRsp.payment);
        if (jobConciseNetRsp.workplace != null) contentValues.put("workplace", jobConciseNetRsp.workplace);
        if (jobConciseNetRsp.employerName != null) contentValues.put("employer_name", jobConciseNetRsp.employerName);
        contentValues.put("employer_industry", jobConciseNetRsp.industry);
        contentValues.put("employer_type", jobConciseNetRsp.employerType);
        contentValues.put("employer_scale", jobConciseNetRsp.employerScale);
        contentValues.put("job51_updatetime", jobConciseNetRsp.job51UpdateTime);
        contentValues.put("zhilian_updatetime", jobConciseNetRsp.zhilianUpdateTime);
        contentValues.put("chinahr_updatetime", jobConciseNetRsp.chinahrUpdateTime);
        contentValues.put("read_times", jobConciseNetRsp.readTimes);
    }

    public static void putZhaopinzhJobDetail(ContentValues contentValues, JobDetailZhaopinzh jobDetailZhaopinzh) {
        contentValues.put("job_id", jobDetailZhaopinzh.jobId);
        if (jobDetailZhaopinzh.jobName != null) contentValues.put("job_name", jobDetailZhaopinzh.jobName);
        contentValues.put("employer_id", jobDetailZhaopinzh.employerId);
        if (jobDetailZhaopinzh.employerName != null)
            contentValues.put("employer_name", jobDetailZhaopinzh.employerName);
        contentValues.put("demand_number", jobDetailZhaopinzh.demandNumber);
        if (jobDetailZhaopinzh.jobDesc != null) contentValues.put("job_desc", jobDetailZhaopinzh.jobDesc);
        if (jobDetailZhaopinzh.skillReq != null) contentValues.put("skill_req", jobDetailZhaopinzh.skillReq);
        if (jobDetailZhaopinzh.expReq != null) contentValues.put("exp_req", jobDetailZhaopinzh.expReq);
        if (jobDetailZhaopinzh.eduReq != null) contentValues.put("edu_req", jobDetailZhaopinzh.eduReq);
        if (jobDetailZhaopinzh.majorReq != null) contentValues.put("major_req", jobDetailZhaopinzh.majorReq);
        if (jobDetailZhaopinzh.sexReq != null) contentValues.put("sex_req", jobDetailZhaopinzh.sexReq);
        if (jobDetailZhaopinzh.payment != null) contentValues.put("payment", jobDetailZhaopinzh.payment);
        if (jobDetailZhaopinzh.workplace != null) contentValues.put("workplace", jobDetailZhaopinzh.workplace);
        contentValues.put("job_type", jobDetailZhaopinzh.jobType);
        if (jobDetailZhaopinzh.workType != null) contentValues.put("work_type", jobDetailZhaopinzh.workType);
        contentValues.put("read_times", jobDetailZhaopinzh.readTimes);
        if (jobDetailZhaopinzh.logoPath != null) contentValues.put("logo_path", jobDetailZhaopinzh.logoPath);
        if (jobDetailZhaopinzh.hrEmail != null) contentValues.put("hr_email", jobDetailZhaopinzh.hrEmail);
        if (jobDetailZhaopinzh.tel != null) contentValues.put("tel", jobDetailZhaopinzh.tel);
        if (jobDetailZhaopinzh.publishTime != null) contentValues.put("publish_time", jobDetailZhaopinzh.publishTime);
        if (jobDetailZhaopinzh.deadTime != null) contentValues.put("dead_time", jobDetailZhaopinzh.deadTime);

    }

    public static String currentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date());
    }

    public static String currentTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    public static String bigTime(String date1, String date2, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            if (dateFormat.parse(date1).compareTo(dateFormat.parse(date2)) == 1) {
                return date1;
            } else {
                return date2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatTime(String date, String format, String destFormat) {
        try {
            DateFormat format1 = new SimpleDateFormat(format);
            DateFormat format2 = new SimpleDateFormat(destFormat);
            return format2.format(format1.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        }
        return false;
    }

    /*获取行业数组*/
    static public List<String> getIndustryList(Context context) {
        try {
            JSONArray jsongArray = new JSONArray(getAssetFileString(context, "industry.json"));
            ArrayList<String> industryList = Common.toList(jsongArray);
            return industryList;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /*获取职位类型数组*/
    static public List<String> getMajorList(Context context) {
        try {
            JSONArray jsonArray = new JSONArray(getAssetFileString(context, "major.json"));
            ArrayList<String> list = Common.toList(jsonArray);
            return list;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /**
     * 从view 得到图片
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }
}
