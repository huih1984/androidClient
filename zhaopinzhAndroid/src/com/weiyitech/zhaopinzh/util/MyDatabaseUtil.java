package com.weiyitech.zhaopinzh.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-12
 * Time: 下午2:45
 * To change this template use File | Settings | File Templates.
 */
public class MyDatabaseUtil {
    public static void putRunEmployerFromDataBase(ArrayList<RunEmployerComponent> runEmployerComponentArrayList, Context context) {
        ContentResolver resolver = context.getContentResolver();
        String runningSelection = "running_time >= ? ";
        String[] runningSelectionArgs = {Common.currentTime()};
        String orderBy = "running_time asc";
        Cursor validMessageCursor = resolver.query(MessageProvider.FAVOR_RUNEMPLOYER_URI, null, runningSelection, runningSelectionArgs, orderBy);
        try {
            if (validMessageCursor != null) {
                for (validMessageCursor.moveToFirst(); !validMessageCursor.isAfterLast(); validMessageCursor.moveToNext()) {
                    RunEmployerComponent runEmployerComponent = new RunEmployerComponent();
                    runEmployerComponent.favoriteValidMessage = true;
                    runEmployerComponent.runEmployer = new RunEmployer();
                    runEmployerComponent.runEmployer.fairId = validMessageCursor.getInt(validMessageCursor.getColumnIndex("fair_id"));
                    runEmployerComponent.runEmployer.hallId = validMessageCursor.getInt(validMessageCursor.getColumnIndex("hall_id"));
                    runEmployerComponent.runEmployer.employerId = validMessageCursor.getInt(validMessageCursor.getColumnIndex("employer_id"));
                    runEmployerComponent.runEmployer.employerName = validMessageCursor.getString(validMessageCursor.getColumnIndex("employer_name"));
                    runEmployerComponent.runEmployer.runningTime = validMessageCursor.getString(validMessageCursor.getColumnIndex("running_time"));
                    runEmployerComponent.runEmployer.fairName = validMessageCursor.getString(validMessageCursor.getColumnIndex("fair_name"));
                    runEmployerComponent.runEmployer.hallName = validMessageCursor.getString(validMessageCursor.getColumnIndex("hall_name"));
                    runEmployerComponent.runEmployer.logoPath = validMessageCursor.getString(validMessageCursor.getColumnIndex("logo_path"));
                    runEmployerComponent.runEmployer.readTimes = validMessageCursor.getInt(validMessageCursor.getColumnIndex("read_times"));
                    runEmployerComponent.runEmployer.standPicId = validMessageCursor.getInt(validMessageCursor.getColumnIndex("stand_pic_id"));
                    runEmployerComponent.runEmployer.jobCounts = validMessageCursor.getInt(validMessageCursor.getColumnIndex("job_counts"));

                    runEmployerComponentArrayList.add(runEmployerComponent);
                }
                validMessageCursor.close();
                for (RunEmployerComponent runEmployerComponent : runEmployerComponentArrayList) {
                    if (runEmployerComponent.runEmployer.employerId > 0 && runEmployerComponent.runEmployer.fairId > 0) {
                        String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
                        String[] mSelectionArgs = {"" + runEmployerComponent.runEmployer.employerId,
                                "" + runEmployerComponent.runEmployer.fairId};
                        Cursor cursorStand = resolver.query(MessageProvider.STAND_URI, null, selection, mSelectionArgs, null);
                        if (cursorStand != null) {
                            runEmployerComponent.runEmployer.stands = new ArrayList<Stand>();
                            for (cursorStand.moveToFirst(); !cursorStand.isAfterLast(); cursorStand.moveToNext()) {
                                Stand stand = new Stand();
                                stand.fairId = runEmployerComponent.runEmployer.fairId;
                                stand.employerId = runEmployerComponent.runEmployer.employerId;
                                stand.standNumber = cursorStand.getString(cursorStand.getColumnIndex("stand_number"));
                                runEmployerComponent.runEmployer.stands.add(stand);
                            }
                            cursorStand.close();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        runningSelection = "running_time < ? ";
        Cursor invalidMessageCursor = resolver.query(MessageProvider.FAVOR_RUNEMPLOYER_URI, null, runningSelection, runningSelectionArgs, orderBy);
        try {
            if (invalidMessageCursor != null) {
                for (invalidMessageCursor.moveToFirst(); !invalidMessageCursor.isAfterLast(); invalidMessageCursor.moveToNext()) {
                    RunEmployerComponent runEmployerComponent = new RunEmployerComponent();
                    runEmployerComponent.runEmployer = new RunEmployer();
                    runEmployerComponent.runEmployer.fairId = invalidMessageCursor.getInt(invalidMessageCursor.getColumnIndex("fair_id"));
                    runEmployerComponent.runEmployer.hallId = invalidMessageCursor.getInt(invalidMessageCursor.getColumnIndex("hall_id"));
                    runEmployerComponent.runEmployer.employerId = invalidMessageCursor.getInt(invalidMessageCursor.getColumnIndex("employer_id"));
                    runEmployerComponent.runEmployer.employerName = invalidMessageCursor.getString(invalidMessageCursor.getColumnIndex("employer_name"));
                    runEmployerComponent.runEmployer.runningTime = invalidMessageCursor.getString(invalidMessageCursor.getColumnIndex("running_time"));
                    runEmployerComponent.runEmployer.fairName = invalidMessageCursor.getString(invalidMessageCursor.getColumnIndex("fair_name"));
                    runEmployerComponent.runEmployer.hallName = invalidMessageCursor.getString(invalidMessageCursor.getColumnIndex("hall_name"));
                    runEmployerComponent.runEmployer.logoPath = invalidMessageCursor.getString(invalidMessageCursor.getColumnIndex("logo_path"));
                    runEmployerComponent.runEmployer.readTimes = invalidMessageCursor.getInt(invalidMessageCursor.getColumnIndex("read_times"));
                    runEmployerComponent.runEmployer.standPicId = invalidMessageCursor.getInt(invalidMessageCursor.getColumnIndex("stand_pic_id"));
                    runEmployerComponent.runEmployer.jobCounts = validMessageCursor.getInt(validMessageCursor.getColumnIndex("job_counts"));
                    runEmployerComponentArrayList.add(runEmployerComponent);
                }
                invalidMessageCursor.close();
                for (RunEmployerComponent runEmployerComponent : runEmployerComponentArrayList) {
                    if (runEmployerComponent.runEmployer.employerId > 0 && runEmployerComponent.runEmployer.fairId > 0) {
                        String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
                        String[] mSelectionArgs = {"" + runEmployerComponent.runEmployer.employerId,
                                "" + runEmployerComponent.runEmployer.fairId};
                        Cursor cursorStand = resolver.query(MessageProvider.STAND_URI, null, selection, mSelectionArgs, null);
                        if (cursorStand != null) {
                            runEmployerComponent.runEmployer.stands = new ArrayList<Stand>();
                            for (cursorStand.moveToFirst(); !cursorStand.isAfterLast(); cursorStand.moveToNext()) {
                                Stand stand = new Stand();
                                stand.fairId = runEmployerComponent.runEmployer.fairId;
                                stand.employerId = runEmployerComponent.runEmployer.employerId;
                                stand.standNumber = cursorStand.getString(cursorStand.getColumnIndex("stand_number"));
                                runEmployerComponent.runEmployer.stands.add(stand);
                            }
                            cursorStand.close();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putPreachMeetingFromDataBase(ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList, Context context) {
        ContentResolver resolver = context.getContentResolver();
        String runningSelection = "running_time_begin >= ? ";
        String[] runningSelectionArgs = {Common.currentTime()};
        String orderBy = "running_time_begin asc";
        Cursor validMessageCursor = resolver.query(MessageProvider.FAVOR_PREACH_MEETING_URI, null, runningSelection, runningSelectionArgs, orderBy);
        try {
            if (validMessageCursor != null) {
                for (validMessageCursor.moveToFirst(); !validMessageCursor.isAfterLast(); validMessageCursor.moveToNext()) {
                    PreachmeetingConciseRsp preachmeetingConciseRsp = new PreachmeetingConciseRsp();
                    preachmeetingConciseRsp.favoriteValidMessage = true;
                    preachmeetingConciseRsp.preachMeetingId = validMessageCursor.getInt(validMessageCursor.getColumnIndex("preach_meeting_id"));
                    preachmeetingConciseRsp.preachMeetingName = validMessageCursor.getString(validMessageCursor.getColumnIndex("preach_meeting_name"));
                    preachmeetingConciseRsp.employerName = validMessageCursor.getString(validMessageCursor.getColumnIndex("employer_name"));
                    preachmeetingConciseRsp.runningTimeBegin = validMessageCursor.getString(validMessageCursor.getColumnIndex("running_time_begin"));
                    preachmeetingConciseRsp.runningTimeEnd = validMessageCursor.getString(validMessageCursor.getColumnIndex("running_time_end"));
                    preachmeetingConciseRsp.runPlace = validMessageCursor.getString(validMessageCursor.getColumnIndex("run_place"));
                    preachmeetingConciseRsp.runningUniversity = validMessageCursor.getString(validMessageCursor.getColumnIndex("running_university"));
                    preachmeetingConciseRsp.readTimes = validMessageCursor.getInt(validMessageCursor.getColumnIndex("read_times"));
                    preachmeetingConciseRsp.logoPath = validMessageCursor.getString(validMessageCursor.getColumnIndex("logo_path"));
                    preachmeetingConciseRsp.addTime = validMessageCursor.getString(validMessageCursor.getColumnIndex("add_time"));
                    preachmeetingConciseRsp.updateTime = validMessageCursor.getString(validMessageCursor.getColumnIndex("update_time"));
                    preachmeetingConciseRspArrayList.add(preachmeetingConciseRsp);
                }
                validMessageCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        runningSelection = "running_time_begin < ? ";
        Cursor invalidMessageCursor = resolver.query(MessageProvider.FAVOR_PREACH_MEETING_URI, null, runningSelection, runningSelectionArgs, orderBy);
        try {
            if (invalidMessageCursor != null) {
                for (invalidMessageCursor.moveToFirst(); !invalidMessageCursor.isAfterLast(); invalidMessageCursor.moveToNext()) {
                    PreachmeetingConciseRsp preachmeetingConciseRsp = new PreachmeetingConciseRsp();
                    preachmeetingConciseRsp.favoriteValidMessage = false;
                    preachmeetingConciseRsp.preachMeetingId = validMessageCursor.getInt(validMessageCursor.getColumnIndex("preach_meeting_id"));
                    preachmeetingConciseRsp.preachMeetingName = validMessageCursor.getString(validMessageCursor.getColumnIndex("preach_meeting_name"));
                    preachmeetingConciseRsp.employerName = validMessageCursor.getString(validMessageCursor.getColumnIndex("employer_name"));
                    preachmeetingConciseRsp.runningTimeBegin = validMessageCursor.getString(validMessageCursor.getColumnIndex("running_time_begin"));
                    preachmeetingConciseRsp.runningTimeEnd = validMessageCursor.getString(validMessageCursor.getColumnIndex("running_time_end"));
                    preachmeetingConciseRsp.runPlace = validMessageCursor.getString(validMessageCursor.getColumnIndex("run_place"));
                    preachmeetingConciseRsp.runningUniversity = validMessageCursor.getString(validMessageCursor.getColumnIndex("running_university"));
                    preachmeetingConciseRsp.readTimes = validMessageCursor.getInt(validMessageCursor.getColumnIndex("read_times"));
                    preachmeetingConciseRsp.logoPath = validMessageCursor.getString(validMessageCursor.getColumnIndex("logo_path"));
                    preachmeetingConciseRsp.addTime = validMessageCursor.getString(validMessageCursor.getColumnIndex("add_time"));
                    preachmeetingConciseRsp.updateTime = validMessageCursor.getString(validMessageCursor.getColumnIndex("update_time"));
                    preachmeetingConciseRspArrayList.add(preachmeetingConciseRsp);
                }
                invalidMessageCursor.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putNetJobFromDataBase(ArrayList<JobConciseNetRsp> jobConciseNetRspArrayList, Context context) {
        ContentResolver resolver = context.getContentResolver();
        try {
            Cursor cursor = resolver.query(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                JobConciseNetRsp jobConciseNetRsp = new JobConciseNetRsp();
                jobConciseNetRsp.jobId = cursor.getInt(cursor.getColumnIndex("job_id"));
                jobConciseNetRsp.jobName = cursor.getString(cursor.getColumnIndex("job_name"));
                jobConciseNetRsp.employerId = cursor.getInt(cursor.getColumnIndex("employer_id"));
                jobConciseNetRsp.employerName = cursor.getString(cursor.getColumnIndex("employer_name"));
                jobConciseNetRsp.payment = cursor.getString(cursor.getColumnIndex("payment"));
                jobConciseNetRsp.workplace = cursor.getString(cursor.getColumnIndex("workplace"));
                jobConciseNetRsp.employerType = cursor.getInt(cursor.getColumnIndex("employer_type"));
                jobConciseNetRsp.employerScale = cursor.getInt(cursor.getColumnIndex("employer_scale"));
                jobConciseNetRsp.industry = cursor.getInt(cursor.getColumnIndex("employer_industry"));
                jobConciseNetRsp.job51UpdateTime = cursor.getString(cursor.getColumnIndex("job51_updatetime"));
                jobConciseNetRsp.zhilianUpdateTime = cursor.getString(cursor.getColumnIndex("zhilian_updatetime"));
                jobConciseNetRsp.chinahrUpdateTime = cursor.getString(cursor.getColumnIndex("chinahr_updatetime"));
                jobConciseNetRsp.readTimes = cursor.getInt(cursor.getColumnIndex("read_times"));
                jobConciseNetRsp.logoPath = cursor.getString(cursor.getColumnIndex("logo_path"));
                jobConciseNetRspArrayList.add(jobConciseNetRsp);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putZhaopinzhJobFromDataBase(ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhArrayList, Context context) {
        ContentResolver resolver = context.getContentResolver();
        try {
            Cursor cursor = resolver.query(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                JobDetailZhaopinzh jobDetailZhaopinzh = new JobDetailZhaopinzh();
                jobDetailZhaopinzh.jobId = cursor.getInt(cursor.getColumnIndex("job_id"));
                jobDetailZhaopinzh.jobName = cursor.getString(cursor.getColumnIndex("job_name"));
                jobDetailZhaopinzh.employerId = cursor.getInt(cursor.getColumnIndex("employer_id"));
                jobDetailZhaopinzh.employerName = cursor.getString(cursor.getColumnIndex("employer_name"));
                jobDetailZhaopinzh.jobDesc = cursor.getString(cursor.getColumnIndex("job_desc"));
                jobDetailZhaopinzh.skillReq = cursor.getString(cursor.getColumnIndex("skill_req"));
                jobDetailZhaopinzh.expReq = cursor.getString(cursor.getColumnIndex("exp_req"));
                jobDetailZhaopinzh.eduReq = cursor.getString(cursor.getColumnIndex("edu_req"));
                jobDetailZhaopinzh.majorReq = cursor.getString(cursor.getColumnIndex("major_req"));
                jobDetailZhaopinzh.sexReq = cursor.getString(cursor.getColumnIndex("sex_req"));
                jobDetailZhaopinzh.payment = cursor.getString(cursor.getColumnIndex("payment"));
                jobDetailZhaopinzh.workplace = cursor.getString(cursor.getColumnIndex("workplace"));
                jobDetailZhaopinzh.jobType = cursor.getInt(cursor.getColumnIndex("job_type"));
                jobDetailZhaopinzh.workType = cursor.getString(cursor.getColumnIndex("work_type"));
                jobDetailZhaopinzh.readTimes = cursor.getInt(cursor.getColumnIndex("read_times"));
                jobDetailZhaopinzh.logoPath = cursor.getString(cursor.getColumnIndex("logo_path"));
                jobDetailZhaopinzh.hrEmail = cursor.getString(cursor.getColumnIndex("hr_email"));
                jobDetailZhaopinzh.tel = cursor.getString(cursor.getColumnIndex("tel"));
                jobDetailZhaopinzh.publishTime = cursor.getString(cursor.getColumnIndex("publish_time"));
                jobDetailZhaopinzh.deadTime = cursor.getString(cursor.getColumnIndex("dead_time"));
                jobDetailZhaopinzhArrayList.add(jobDetailZhaopinzh);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<Integer> queryFairDatabase(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<Integer> ids = new ArrayList<Integer>();
        try {
            Cursor cursor = resolver.query(MessageProvider.LOCAL_EXISTS_FAIR_RECORDS_URI, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ids.add(cursor.getInt(cursor.getColumnIndex("fair_id")));
            }
            cursor.close();
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Integer> queryFairReadDatabase(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<Integer> ids = new ArrayList<Integer>();
        try {
            Cursor cursor = resolver.query(MessageProvider.LOCAL_READ_FAIR_RECORDS_URI, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ids.add(cursor.getInt(cursor.getColumnIndex("fair_id")));
            }
            cursor.close();
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void putLocalFairIdsToDataBase(Integer fair_id, String runningTime, Context context) {
        ContentResolver resolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("fair_id", fair_id);
            contentValues.put("running_time", runningTime);
            resolver.insert(MessageProvider.LOCAL_EXISTS_FAIR_RECORDS_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putLocalFairIdsToReadDataBase(Integer fair_id, String runningTime, Context context) {
        ContentResolver resolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("fair_id", fair_id);
            contentValues.put("running_time", runningTime);
            resolver.insert(MessageProvider.LOCAL_READ_FAIR_RECORDS_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> queryPreachDatabase(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<Integer> ids = new ArrayList<Integer>();
        try {
            Cursor cursor = resolver.query(MessageProvider.LOCAL_EXISTS_PREACH_RECORDS_URI, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ids.add(cursor.getInt(cursor.getColumnIndex("preach_id")));
            }
            cursor.close();
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int queryCountPreachDatabase(Context context, String university) {
        ContentResolver resolver = context.getContentResolver();
        try {
            String[] projection = {"count(1)"};
            String selection = "running_university = ?";
            String[] selectionArgs = {university};
            Cursor cursor = resolver.query(MessageProvider.LOCAL_EXISTS_PREACH_RECORDS_URI, projection, selection, selectionArgs, null);
            if(cursor != null){
                int cnt = cursor.getInt(cursor.getColumnIndex("count(1)"));
                cursor.close();
                return cnt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void putLocalPreachIdsToDataBase(Integer preach_id, String runningBeginTime, String runningUniversity, Context context) {
        ContentResolver resolver = context.getContentResolver();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("preach_id", preach_id);
            contentValues.put("running_begintime", runningBeginTime);
            contentValues.put("running_university", runningUniversity);
            resolver.insert(MessageProvider.LOCAL_EXISTS_PREACH_RECORDS_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteObsoleteIds(Context context) {
        ContentResolver resolver = context.getContentResolver();
        try {
            String where = "date(running_begintime) < date('now')";
            resolver.delete(MessageProvider.LOCAL_EXISTS_PREACH_RECORDS_URI, where, null);
            where = "date(running_time) < date('now')";
            resolver.delete(MessageProvider.LOCAL_EXISTS_FAIR_RECORDS_URI, where, null);
            resolver.delete(MessageProvider.LOCAL_READ_FAIR_RECORDS_URI, where, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
