package com.weiyitech.zhaopinzh.presentation.component;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-25
 * Time: 下午4:23
 * To change this template use File | Settings | File Templates.
 */
public class MessageProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static String AUTHORITY = "com.weiyitech.zhaopinzh.presentation.component.MessageProvider";
    public static final Uri FAVOR_RUNEMPLOYER_URI = Uri.parse("content://" + AUTHORITY + "/t_favor_run_employer");
    public static final Uri JOB_URI = Uri.parse("content://" + AUTHORITY + "/t_job_detail");
    public static final Uri EMPLOYER_URI = Uri.parse("content://" + AUTHORITY + "/t_employer");
    public static final Uri JOB_LIST_URI = Uri.parse("content://" + AUTHORITY + "/t_job_list_of_employer");
    public static final Uri JOBIDS_URI = Uri.parse("content://" + AUTHORITY + "/job_ids");
    public static final Uri STAND_URI = Uri.parse("content://" + AUTHORITY + "/t_stand");
    public static final Uri READ_RUNEMPLOYER_URI = Uri.parse("content://" + AUTHORITY + "/t_read_run_employer");
    public static final Uri FAVOR_PREACH_MEETING_URI = Uri.parse("content://" + AUTHORITY + "/t_favor_preach_meeting");
    public static final Uri READ_PREACH_INSTRUCTION_URI = Uri.parse("content://" + AUTHORITY + "/t_read_preach_instruction");
    public static final Uri READ_PREACH_MEETING_URI = Uri.parse("content://" + AUTHORITY + "/t_read_preach_meeting");
    public static final Uri PREACH_MEETING_IDS_URI = Uri.parse("content://" + AUTHORITY + "/preach_meeting_ids");
    public static final Uri LOCATION_IN_MAP_URI = Uri.parse("content://" + AUTHORITY + "/t_location_in_map");
    public static final Uri FAVOR_NET_JOB_DETAIL_URI = Uri.parse("content://" + AUTHORITY + "/t_favor_net_job_detail");
    public static final Uri READ_NET_JOB_DETAIL_URI = Uri.parse("content://" + AUTHORITY + "/t_read_net_job_detail");
    public static final Uri FAVOR_ZHAOPINZH_JOB_DETAIL_URI = Uri.parse("content://" + AUTHORITY + "/t_favor_zhaopinzh_job_detail");
    public static final Uri READ_ZHAOPINZH_JOB_DETAIL_URI = Uri.parse("content://" + AUTHORITY + "/t_read_zhaopinzh_job_detail");
    public static final Uri NEW_HINT_URI = Uri.parse("content://" + AUTHORITY + "/t_new_hint");
    public static final Uri LOCAL_EXISTS_FAIR_RECORDS_URI = Uri.parse("content://" + AUTHORITY + "/t_local_exists_fair_records");
    public static final Uri LOCAL_READ_FAIR_RECORDS_URI = Uri.parse("content://" + AUTHORITY + "/t_local_read_fair_records");
    public static final Uri LOCAL_EXISTS_PREACH_RECORDS_URI = Uri.parse("content://" + AUTHORITY + "/t_local_exists_preach_records");

    static {
        sUriMatcher.addURI(AUTHORITY, "t_favor_run_employer", 1);
        sUriMatcher.addURI(AUTHORITY, "t_job_detail", 2);
        sUriMatcher.addURI(AUTHORITY, "t_employer", 3);
        sUriMatcher.addURI(AUTHORITY, "t_job_list_of_employer", 4);
        sUriMatcher.addURI(AUTHORITY, "job_ids", 5);
        sUriMatcher.addURI(AUTHORITY, "t_stand", 6);
        sUriMatcher.addURI(AUTHORITY, "t_read_run_employer", 7);
        sUriMatcher.addURI(AUTHORITY, "t_favor_preach_meeting", 8);
        sUriMatcher.addURI(AUTHORITY, "t_read_preach_meeting", 9);
        sUriMatcher.addURI(AUTHORITY, "preach_meeting_ids", 10);
        sUriMatcher.addURI(AUTHORITY, "t_read_preach_instruction", 11);
        sUriMatcher.addURI(AUTHORITY, "t_location_in_map", 12);
        sUriMatcher.addURI(AUTHORITY, "t_favor_net_job_detail", 13);
        sUriMatcher.addURI(AUTHORITY, "t_read_net_job_detail", 14);
        sUriMatcher.addURI(AUTHORITY, "t_new_hint", 15);
        sUriMatcher.addURI(AUTHORITY, "t_local_exists_fair_records", 16);
        sUriMatcher.addURI(AUTHORITY, "t_local_exists_preach_records", 17);
        sUriMatcher.addURI(AUTHORITY, "t_favor_zhaopinzh_job_detail", 18);
        sUriMatcher.addURI(AUTHORITY, "t_read_zhaopinzh_job_detail", 19);
        sUriMatcher.addURI(AUTHORITY, "t_local_read_fair_records", 20);

    }

    private MessageDatabaseOpenHelper mOpenHelper;
    private final String DATABASE_NAME = "message";
    private final int DATABASE_VERSION = 11;
    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MessageDatabaseOpenHelper(
                getContext(),        // the application context
                DATABASE_NAME,              // the jobName of the database)
                null,                // uses the default SQLite cursor
                1                    // the version number
        );

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                return mOpenHelper.queryFavorRunEmployer(selection, selectionArgs, sortOrder);
            case 2:
                return mOpenHelper.queryJobDetail(selection, selectionArgs, sortOrder);
            case 3:
                return mOpenHelper.queryEmployerById(selection, selectionArgs, sortOrder);
            case 4:
                return mOpenHelper.queryJobConciseList(selection, selectionArgs, sortOrder);
            case 5:
                return mOpenHelper.queryJobIdList();
            case 6:
                return mOpenHelper.queryStand(selection, selectionArgs, sortOrder);
            case 7:
                return mOpenHelper.queryReadRunEmployer(selection, selectionArgs, sortOrder);
            case 8:
                return mOpenHelper.queryFavorPreachList(selection, selectionArgs, sortOrder);
            case 9:
                return mOpenHelper.queryReadPreachList(selection, selectionArgs, sortOrder);
            case 10:
                return mOpenHelper.queryFavorPreachIdList();
            case 11:
                return mOpenHelper.queryReadPreachInstruction(selection, selectionArgs, sortOrder);
            case 12:
                return mOpenHelper.queryLocationInMap(selection, selectionArgs, sortOrder);
            case 13:
                return mOpenHelper.queryFavorNetJobDetail(selection, selectionArgs, sortOrder);
            case 14:
                return mOpenHelper.queryReadNetJobDetail(selection, selectionArgs, sortOrder);
            case 15:
                return mOpenHelper.queryNewHint(selection, selectionArgs, sortOrder);
            case 16:
                return mOpenHelper.queryTable("t_local_exists_fair_records", selection, selectionArgs, sortOrder);
            case 17:
                // return mOpenHelper.queryLocalPreachTable(selection, selectionArgs, sortOrder);
                return mOpenHelper.queryTable("t_local_exists_preach_records", selection, selectionArgs, sortOrder);
            case 18:
                return mOpenHelper.queryTable("t_favor_zhaopinzh_job_detail", selection, selectionArgs, sortOrder);
            case 19:
                // return mOpenHelper.queryLocalPreachTable(selection, selectionArgs, sortOrder);
                return mOpenHelper.queryTable("t_read_zhaopinzh_job_detail", selection, selectionArgs, sortOrder);
            case 20:
                // return mOpenHelper.queryLocalPreachTable(selection, selectionArgs, sortOrder);
                return mOpenHelper.queryTable("t_local_read_fair_records", selection, selectionArgs, sortOrder);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

//    long queryNewHintCount(String selection, String[] selectionArgs){
//        return mOpenHelper.queryNewHintCount(selection, selectionArgs);
//    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 1:
                return "vnd.android.cursor.dir/favor_run_employer";
            case 2:
                return "vnd.android.cursor.dir/job_detail";
            case 3:
                return "vnd.android.cursor.dir/employer";
            case 4:
                return "vnd.android.cursor.dir/job_list_of_employer";
            case 5:
                return "vnd.android.cursor.dir/job_ids";
            case 6:
                return "vnd.android.cursor.dir/stand";
            case 7:
                return "vnd.android.cursor.dir/read_run_employer";
            case 8:
                return "vnd.android.cursor.dir/preach_meeting";
            case 9:
                return "vnd.android.cursor.dir/read_preach_meeting";
            case 10:
                return "vnd.android.cursor.dir/preach_meeting_ids";
            case 11:
                return "vnd.android.cursor.dir/read_preach_instruction";
            case 12:
                return "vnd.android.cursor.dir/location_in_map";
            case 13:
                return "vnd.android.cursor.dir/favor_net_job_detail";
            case 14:
                return "vnd.android.cursor.dir/read_net_job_detail";
            case 15:
                return "vnd.android.cursor.dir/new_hint";
            case 16:
                return "vnd.android.cursor.dir/local_exists_fair_records";
            case 17:
                return "vnd.android.cursor.dir/local_exists_preach_records";
            case 18:
                return "vnd.android.cursor.dir/favor_zhaopinzh_job_detail";
            case 19:
                return "vnd.android.cursor.dir/read_zhaopinzh_job_detail";
            case 20:
                return "vnd.android.cursor.dir/local_read_fair_records";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) == 1) {
            String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("employer_id"), "" + values.getAsInteger("fair_id")};
            Cursor cursor = mOpenHelper.queryFavorRunEmployer(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateFavorRunEmployerTable(values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertFavorRunEmployerTable(values);
            }
        } else if (sUriMatcher.match(uri) == 2) {
            String selection = "job_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("job_id")};
            Cursor cursor = mOpenHelper.queryJobDetail(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateJobDetailTable(values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertJobDetailTable(values);
            }
        } else if (sUriMatcher.match(uri) == 3) {
            String selection = "employer_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("job_id")};
            Cursor cursor = mOpenHelper.queryEmployerById(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateEmployerTable(values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertEmployerTable(values);
            }
        } else if (sUriMatcher.match(uri) == 6) {
            String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("employer_id"), "" + values.getAsInteger("fair_id")};
            Cursor cursor = mOpenHelper.queryEmployerById(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateStandTable(values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertStandTable(values);
            }
        } else if (sUriMatcher.match(uri) == 7) {
            String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("employer_id"), "" + values.getAsInteger("fair_id")};
            Cursor cursor = mOpenHelper.queryReadRunEmployer(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertReadRunEmployerTable(values);
            }
        } else if (sUriMatcher.match(uri) == 8) {
            String selection = "preach_meeting_id =  CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("preach_meeting_id")};
            Cursor cursor = mOpenHelper.queryFavorPreachList(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertFavorPreachTable(values);
            }
        } else if (sUriMatcher.match(uri) == 9) {
            String selection = "preach_meeting_id =  CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("preach_meeting_id")};
            Cursor cursor = mOpenHelper.queryReadPreachList(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertReadPreachTable(values);
            }
        } else if (sUriMatcher.match(uri) == 11) {
            String selection = "preach_meeting_id =  CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("preach_meeting_id")};
            Cursor cursor = mOpenHelper.queryReadPreachInstruction(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertReadPreachInstructionTable(values);
            }
        } else if (sUriMatcher.match(uri) == 12) {
            String selection = "add_time = ?";
            String[] selectionArgs = new String[]{values.getAsString("addTime")};
            Cursor cursor = mOpenHelper.queryLocationInMap(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertLocationInMapTable(values);
            }
        }  else if (sUriMatcher.match(uri) == 13) {
            String selection = "job_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("job_id")};
            Cursor cursor = mOpenHelper.queryFavorNetJobDetail(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateFavorNetJobDetailTable(values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertFavorNetJobDetailTable(values);
            }
        }   else if (sUriMatcher.match(uri) == 14) {
            String selection = "job_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("job_id")};
            Cursor cursor = mOpenHelper.queryReadNetJobDetail(selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateReadNetJobDetailTable(values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertReadNetJobDetailTable(values);
            }
        } else if(sUriMatcher.match(uri) == 15) {
            mOpenHelper.insertNewHint(values);
        }  else if (sUriMatcher.match(uri) == 16) {
            String selection = "fair_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("fair_id")};
            Cursor cursor = mOpenHelper.queryTable("t_local_exists_fair_records", selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateTable("t_local_exists_fair_records", values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertTable("t_local_exists_fair_records", values);
            }
        }else if (sUriMatcher.match(uri) == 17) {
            String selection = "preach_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("preach_id")};
            Cursor cursor = mOpenHelper.queryTable("t_local_exists_preach_records", selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateTable("t_local_exists_preach_records", values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertTable("t_local_exists_preach_records", values);
            }
        } else if (sUriMatcher.match(uri) == 18) {
            String selection = "job_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("job_id")};
            Cursor cursor = mOpenHelper.queryTable("t_favor_zhaopinzh_job_detail", selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateTable("t_favor_zhaopinzh_job_detail", values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertTable("t_favor_zhaopinzh_job_detail", values);
            }
        }else if (sUriMatcher.match(uri) == 19) {
            String selection = "job_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("job_id")};
            Cursor cursor = mOpenHelper.queryTable("t_read_zhaopinzh_job_detail", selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateTable("t_read_zhaopinzh_job_detail", values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertTable("t_read_zhaopinzh_job_detail", values);
            }
        }  else if (sUriMatcher.match(uri) == 20) {
            String selection = "fair_id = CAST(? AS INT)";
            String[] selectionArgs = new String[]{"" + values.getAsInteger("fair_id")};
            Cursor cursor = mOpenHelper.queryTable("t_local_read_fair_records", selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                mOpenHelper.updateTable("t_local_read_fair_records", values, selection, selectionArgs);
                Log.i("MessageProvider insert", "have inserted!");
            } else {
                mOpenHelper.insertTable("t_local_read_fair_records", values);
            }
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (sUriMatcher.match(uri) == 1) {
            return mOpenHelper.deleteFavorRunEmployerTable(selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 2) {
            return mOpenHelper.deleteJobDetailTable(selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 3) {
            return mOpenHelper.deleteEmployerTable(selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 6) {
            return mOpenHelper.deleteStandTable(selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 8) {
            return mOpenHelper.deleteFavorPreachMeetingTable(selection, selectionArgs);
        }  else if (sUriMatcher.match(uri) == 13) {
            return mOpenHelper.deleteFavorNetJobDetailTable(selection, selectionArgs);
        } else if(sUriMatcher.match(uri) == 15) {
            return mOpenHelper.deleteNewHint(selection, selectionArgs);
        } else if(sUriMatcher.match(uri) == 16) {
            return mOpenHelper.deleteTable("t_local_exists_fair_records", selection, selectionArgs);
        }else if(sUriMatcher.match(uri) == 17) {
            return mOpenHelper.deleteTable("t_local_exists_preach_records", selection, selectionArgs);
        } else if(sUriMatcher.match(uri) == 18) {
            return mOpenHelper.deleteTable("t_favor_zhaopinzh_job_detail", selection, selectionArgs);
        }  else if(sUriMatcher.match(uri) == 20) {
            return mOpenHelper.deleteTable("t_local_read_fair_records", selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (sUriMatcher.match(uri) == 1) {
            mOpenHelper.updateFavorRunEmployerTable(values, selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 2) {
            mOpenHelper.updateJobDetailTable(values, selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 3) {
            mOpenHelper.updateEmployerTable(values, selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 8) {
            mOpenHelper.updateFavorPreachMeetingTable(values, selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 11) {
            mOpenHelper.updateReadPreachInstructionTable(values, selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 13) {
            mOpenHelper.updateJobDetailTable(values, selection, selectionArgs);
        } else if (sUriMatcher.match(uri) == 16) {
            mOpenHelper.updateTable("t_local_exists_fair_records", values, selection, selectionArgs);
        }else if (sUriMatcher.match(uri) == 17) {
            mOpenHelper.updateTable("t_local_exists_preach_records", values, selection, selectionArgs);
        }  else if (sUriMatcher.match(uri) == 20) {
            mOpenHelper.updateTable("t_local_read_fair_records", values, selection, selectionArgs);
        }
        return 0;
    }

    private class MessageDatabaseOpenHelper extends SQLiteOpenHelper {

        MessageDatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        MessageDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_favor_run_employer USING fts3(fair_id INTEGER, hall_id INTEGER, employer_id INTEGER," +
                    "employer_name, fair_name, hall_name,  logo_path, industry INTEGER, running_time, read_times INTEGER, stand_pic_id INTEGER, job_counts INTEGER)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_job_detail USING fts3(job_id INTEGER, job_name, employer_id INTEGER, fair_id INTEGER, " +
                    "job_type INTEGER, demand_number INTEGER, resp, exp_req, edu_req, major_req, skill_req, sex_req,  workplace, payment)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_stand USING fts3(fair_id INTEGER, employer_id INTEGER, stand_number)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_employer USING fts3(employer_id INTEGER," +
                    "employer_name, type INTEGER, scale INTEGER, hr_email, desc, address, website, province, city, tel, logo_path, big_logo_path, industry INTEGER)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_read_run_employer USING fts3(fair_id INTEGER, employer_id INTEGER, running_time)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_favor_preach_meeting  USING fts3(\n" +
                    "  preach_meeting_id INTEGER,\n" +
                    "  preach_meeting_name ,\n" +
                    "  employer_name ,\n" +
                    "  instruction ,\n" +
                    "  running_time_begin ,\n" +
                    "  running_time_end ,\n" +
                    "  run_place ,\n" +
                    "  running_university ,\n" +
                    "  read_times INTEGER,\n" +
                    "  logo_path ,\n" +
                    "  add_time ,\n" +
                    "  update_time)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_read_preach_meeting  USING fts3(\n" +
                    "  preach_meeting_id INTEGER)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_read_preach_instruction  USING fts3(\n" +
                    "  preach_meeting_id INTEGER, instruction)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_location_in_map  USING fts3(\n" +
                    "  location_id INTEGER, longitude DOUBLE, latitude DOUBLE, address, add_time)");

            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_favor_net_job_detail  USING fts3(\n" +
                    "job_id INTEGER, job_name, employer_id INTEGER, employer_name, demand_number INTEGER, job_desc, " +
                    "skill_req, exp_req, edu_req, major_req, sex_req, payment, workplace, job_type INTEGER, employer_type INTEGER, " +
                    "employer_scale INTEGER, employer_industry INTEGER, " +
                    "workType, read_times INTEGER, logo_path, job51_updatetime, zhilian_updatetime, chinahr_updatetime, job51_url, zhilian_url, chinahr_url)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_read_net_job_detail  USING fts3(\n" +
                    " job_id INTEGER)");

            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_favor_zhaopinzh_job_detail  USING fts3(\n" +
                    "job_id INTEGER, job_name, employer_id INTEGER, employer_name, demand_number INTEGER, job_desc, " +
                    "skill_req, exp_req, edu_req, major_req, sex_req, payment, workplace, job_type INTEGER, work_type, read_times INTEGER, logo_path, hr_email, tel, publish_time, dead_time)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE  t_read_zhaopinzh_job_detail  USING fts3(\n" +
                    " job_id INTEGER)");

            mDatabase.execSQL("CREATE VIRTUAL TABLE t_new_hint USING fts3(type INTEGER)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE t_local_exists_fair_records USING fts3(fair_id INTEGER, running_time)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE t_local_read_fair_records USING fts3(fair_id INTEGER, running_time)");
            mDatabase.execSQL("CREATE VIRTUAL TABLE t_local_exists_preach_records USING fts3(preach_id INTEGER, running_university, running_begintime)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP  TABLE IF EXISTS t_job_detail");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("DROP  TABLE IF EXISTS t_favor_run_employer");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("DROP  TABLE IF EXISTS t_stand");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP  TABLE IF EXISTS t_employer");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP  TABLE IF EXISTS t_read_run_employer");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP  TABLE IF EXISTS t_favor_preach_meeting");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP  TABLE IF EXISTS t_read_preach_meeting");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP  TABLE IF EXISTS t_read_preach_instruction");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP  TABLE IF EXISTS t_location_in_map");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP TABLE IF EXISTS t_favor_net_job_detail");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP TABLE IF EXISTS t_read_net_job_detail");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP TABLE IF EXISTS t_favor_zhaopinzh_job_detail");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP TABLE IF EXISTS t_read_zhaopinzh_job_detail");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                db.execSQL("DROP TABLE IF EXISTS t_new_hint");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("DROP TABLE IF EXISTS t_local_exists_fair_records");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("DROP TABLE IF EXISTS t_local_exists_preach_records");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                db.execSQL("DROP TABLE IF EXISTS t_local_read_fair_records");
            } catch (Exception e) {
                e.printStackTrace();
            }

            onCreate(db);
        }

        public Cursor queryReadRunEmployer(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_read_run_employer");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor == null) {
                } else if (!cursor.moveToFirst()) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                cursor = null;
            }
            db.close();
            return cursor;
        }

        public Cursor queryFavorRunEmployer(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_favor_run_employer");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor == null) {
                } else if (!cursor.moveToFirst()) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                cursor = null;
            }
            db.close();
            return cursor;
        }

        public long insertReadRunEmployerTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_read_run_employer", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public long insertFavorRunEmployerTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_favor_run_employer", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public long updateFavorRunEmployerTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getReadableDatabase();
            try {
                int ret = db.update("t_favor_run_employer", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteFavorRunEmployerTable(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_favor_run_employer", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }


        public Cursor queryStand(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_stand");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertStandTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_stand", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteStandTable(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_stand", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryJobConciseList(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_job_detail");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public Cursor queryJobDetail(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_job_detail");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public Cursor queryJobIdList() {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_job_detail");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                String[] columns = new String[]{
                        "job_id"
                };
                cursor = builder.query(db,
                        columns, null, null, null, null, "job_id DESC");
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertJobDetailTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_job_detail", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteJobDetailTable(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_job_detail", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int updateJobDetailTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_job_detail", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public long insertEmployerTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_employer", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryEmployerById(String selection, String selectionArgs[], String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_employer");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor == null) {
                } else if (!cursor.moveToFirst()) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public Cursor queryFavorPreachList(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_favor_preach_meeting");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor == null) {
                } else if (!cursor.moveToFirst()) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public Cursor queryFavorPreachIdList() {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_favor_preach_meeting");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                String[] columns = new String[]{
                        "preach_meeting_id"
                };
                cursor = builder.query(db,
                        columns, null, null, null, null, "preach_meeting_id DESC");
            } catch (Exception e) {
                if (cursor == null) {
                } else if (!cursor.moveToFirst()) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public Cursor queryReadPreachList(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_read_preach_meeting");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor == null) {
                } else if (!cursor.moveToFirst()) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertReadPreachTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_read_preach_meeting", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public long insertFavorPreachTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_favor_preach_meeting", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteEmployerTable(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_employer", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteFavorPreachMeetingTable(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_favor_preach_meeting", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int updateEmployerTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_employer", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int updateStandTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_stand", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int updateFavorPreachMeetingTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_favor_preach_meeting", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryReadPreachInstruction(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_read_preach_instruction");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor == null) {
                } else if (!cursor.moveToFirst()) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertReadPreachInstructionTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_read_preach_instruction", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int updateReadPreachInstructionTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_read_preach_instruction", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryLocationInMap(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_location_in_map");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertLocationInMapTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_location_in_map", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int updateFavorNetJobDetailTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_favor_net_job_detail", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryFavorNetJobDetail(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_favor_net_job_detail");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertFavorNetJobDetailTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_favor_net_job_detail", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteFavorNetJobDetailTable(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_favor_net_job_detail", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }


        public int updateReadNetJobDetailTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_read_net_job_detail", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryReadNetJobDetail(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_read_net_job_detail");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertReadNetJobDetailTable(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_read_net_job_detail", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteReadNetJobDetailTable(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_read_net_job_detail", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int updateNewHintTable(ContentValues initialValues, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update("t_new_hint", initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryNewHint(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_new_hint");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertNewHint(ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert("t_new_hint", null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteNewHint(String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete("t_new_hint", whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }


        public Cursor queryLocalPreachTable(String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("t_local_exists_preach_records");
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                String[] projection = {"count(1)", "preach_id", "running_university", "running_begintime"};
                cursor = builder.query(db,
                        projection, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public int updateTable(String tableName, ContentValues initialValues, String whereClause, String[] whereArgs){
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.update(tableName, initialValues, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public Cursor queryTable(String tableName, String selection, String[] selectionArgs, String orderBy) {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(tableName);
            Cursor cursor = null;
            SQLiteDatabase db = getReadableDatabase();
            try {
                cursor = builder.query(db,
                        null, selection, selectionArgs, null, null, orderBy);
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                db.close();
                Log.e("cursor exception", e.getMessage());
            }
            if (cursor == null) {
                return null;
            } else if (!cursor.moveToFirst()) {
                cursor.close();
                return null;
            }
            return cursor;
        }

        public long insertTable(String tableName, ContentValues initialValues) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                long ret = db.insert(tableName, null, initialValues);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }

        public int deleteTable(String tableName, String whereClause, String[] whereArgs) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                int ret = db.delete(tableName, whereClause, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
            return 0;
        }
    }


}
