package com.weiyitech.zhaopinzh.util;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.ListPreference;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-11-12
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
public class SearchConditionUtil {
    /**
     * *****************************************************************************
     */

    /*获取资产文件夹下文件的字符串*/
    static public String getAssetFileString(String fileName) {
        try {
            AssetManager assetManager = ZhaopinzhApp.getInstance().getAssets();
            InputStream inputStream = null;
            inputStream = assetManager.open(fileName);
            String string = Common.inputStream2String(inputStream, "utf8");
            return string;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /*获取行业数组*/
    public static List<String> getIndustryList() {
        try {
            String industryJson = getAssetFileString("industry.json");
            JSONArray jsongArray = new JSONArray(industryJson);
            ArrayList<String> industryList = Common.toList(jsongArray);
            return industryList;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /*获取发布时间数组*/
    public static List<String> getPublishTimeList() {
        try {
            JSONArray jsonArray = new JSONArray(getAssetFileString("runningInterval.json"));
            ArrayList<String> publishTimeList = Common.toList(jsonArray);
            return publishTimeList;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /*获取经验要求数组*/
    public static List<String> getExpReqList() {
        try {
            JSONArray jsonArray = new JSONArray(getAssetFileString("experienceyear.json"));
            ArrayList<String> list = Common.toList(jsonArray);
            return list;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /*获取学历要求数组*/
    public static List<String> getEduReqList() {
        try {
            JSONArray jsongArray = new JSONArray(getAssetFileString("educationlevel.json"));
            ArrayList<String> list = Common.toList(jsongArray);
            return list;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /*获取职位类型数组*/
    public static List<String> getJobTypeList() {
        try {
            String jobType = getAssetFileString("jobtype.json");
            JSONArray jsonArray = new JSONArray(jobType);
            ArrayList<String> list = Common.toList(jsonArray);
            return list;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /*获取职位类型数组*/
    public static List<String> getMajorList() {
        try {
            JSONArray jsonArray = new JSONArray(getAssetFileString("major.json"));
            ArrayList<String> list = Common.toList(jsonArray);
            return list;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }


    /*获取公司规模数组*/
    public static List<String> getCompanyScaleList() {
        try {
            JSONArray jsonArray = new JSONArray(getAssetFileString("companyscale.json"));
            ArrayList<String> list = Common.toList(jsonArray);
            return list;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public static void setPrefFileBooleanValue(String key, boolean value) {
        try {
            SharedPreferences settings = ZhaopinzhApp.getInstance().getSharedPreferences(Common.prefFile, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPrefFileStringValue(String key, String value) {
        try {
            SharedPreferences settings = ZhaopinzhApp.getInstance().getSharedPreferences(Common.prefFile, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSettings(List<String> allList, String property, List<String> selectedList) {
        boolean isSelected = false;
        if (allList != null) {
            for (String item : allList) {
                isSelected = false;
                if (selectedList != null) {
                    for (String selectedItem : selectedList) {
                        if (selectedItem.equals(item)) {
                            isSelected = true;
                        }
                    }
                }
                setPrefFileBooleanValue(property + allList.indexOf(item), isSelected);
            }
        }
    }

    public static void saveSettings(int type, List<String> selectedList) {
        if (type == Common.INDUSTRY_TYPE) {
            saveSettings(getIndustryList(), "industry", selectedList);
        } else if (type == Common.JOB_TYPE) {
            saveSettings(getJobTypeList(), "jobtype", selectedList);
        } else if (type == Common.PUBLISH_TYPE) {
            if (selectedList == null || selectedList.size() == 0) {
                setPrefFileStringValue("runninginterval", "时间不限");
            } else {
                setPrefFileStringValue("runninginterval", selectedList.get(0));
            }
        } else if (type == Common.EXPERIENCE_TYPE) {
            if (selectedList == null || selectedList.size() == 0) {
                setPrefFileStringValue("expreqlist", "经验不限");
            } else {
                setPrefFileStringValue("expreqlist", selectedList.get(0));
            }
        } else if (type == Common.EDUCATION_TYPE) {
            if (selectedList == null || selectedList.size() == 0) {
                setPrefFileStringValue("edureqlist", "学历不限");
            } else {
                setPrefFileStringValue("edureqlist", selectedList.get(0));
            }
        }
    }

    public static List<String> getSettings(List<String> allList, String property) {
        List<String> selectedList = new ArrayList<String>();
        SharedPreferences settings = ZhaopinzhApp.getInstance().getSharedPreferences(Common.prefFile, 0);
        for (int i = 0; i <= allList.size() - 1; ++i) {
            String key = new String(property + i);
            Boolean aBoolean = settings.getBoolean(key, true);
            if (aBoolean) {
                selectedList.add(allList.get(i));
            }
        }
        if (selectedList.size() == 0) {
            selectedList.add(allList.get(0));
        }
        return selectedList;
    }

    public static List<String> getStringSettings(String key, String defaultValue) {
        SharedPreferences settings = ZhaopinzhApp.getInstance().getSharedPreferences(Common.prefFile, 0);
        String val = settings.getString(key, defaultValue);
        List<String> list = new ArrayList<String>();
        list.add(val);
        return list;
    }

    public static List<String> getSettings(int type) {
        if (type == Common.INDUSTRY_TYPE) {
            return getSettings(getIndustryList(), "industry");
        } else if (type == Common.JOB_TYPE) {
            return getSettings(getJobTypeList(), "jobtype");
        } else if (type == Common.PUBLISH_TYPE) {
            return getStringSettings("runninginterval", "时间不限");
        } else if (type == Common.EDUCATION_TYPE) {
            return getStringSettings("edureqlist", "学历不限");
        } else if (type == Common.EXPERIENCE_TYPE) {
            return getStringSettings("expreqlist", "经验不限");
        }
        return null;
    }

    public static int getIntSetting(String key) {
        SharedPreferences settings = ZhaopinzhApp.getInstance().getSharedPreferences(Common.prefFile, 0);
        return settings.getInt(key, 0);
    }

    public static void setIntSetting(String key, int val) {
        try {
            SharedPreferences settings = ZhaopinzhApp.getInstance().getSharedPreferences(Common.prefFile, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(key, val);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
