package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-3-25
 * Time: 下午8:19
 * To change this template use File | Settings | File Templates.
 */
public class RunEmployer implements Parcelable {
    public int fairId;
    public int hallId;
    public int employerId;
    public String employerName;
    public String runningTime;
    public String fairName;
    public String hallName;
    public String logoPath;
    public int readTimes;
    public int standPicId;
    public int jobCounts;
    public int recruitTimes;
    public List<Stand> stands = new ArrayList<Stand>();

    public static final Parcelable.Creator<RunEmployer> CREATOR
            = new Parcelable.Creator<RunEmployer>() {
        public RunEmployer createFromParcel(Parcel in) {
            try {
                RunEmployer runEmployer = new RunEmployer();
                runEmployer.fairId = in.readInt();
                runEmployer.hallId = in.readInt();
                runEmployer.employerId = in.readInt();
                runEmployer.employerName = in.readString();
                runEmployer.runningTime = in.readString();
                runEmployer.fairName = in.readString();
                runEmployer.hallName = in.readString();
                runEmployer.logoPath = in.readString();
                runEmployer.readTimes = in.readInt();
                runEmployer.standPicId = in.readInt();
                runEmployer.jobCounts = in.readInt();
                runEmployer.recruitTimes = in.readInt();
                Parcelable[] stands1 = in.readParcelableArray(Stand.class.getClassLoader());
                runEmployer.stands = Arrays.asList(Arrays.asList(stands1).toArray(new Stand[stands1.length])); //in.readArrayList(Stand.class.getClassLoader());
                return runEmployer;
            } catch (Exception e) {
                Log.e("zhaopinzhException RunEmployer createFromParcel", e.toString());
            }
            return null;
        }

        public RunEmployer[] newArray(int size) {
            return new RunEmployer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try{
        parcel.writeInt(fairId);
        parcel.writeInt(hallId);
        parcel.writeInt(employerId);
        parcel.writeString(employerName);
        parcel.writeString(runningTime);
        parcel.writeString(fairName);
        parcel.writeString(hallName);
        parcel.writeString(logoPath);
        parcel.writeInt(readTimes);
        parcel.writeInt(standPicId);
        parcel.writeInt(jobCounts);
        parcel.writeInt(recruitTimes);
        parcel.writeParcelableArray(stands.toArray(new Stand[stands.size()]), i);
        } catch (Exception e) {
            Log.e("zhaopinzhException RunEmployer writeToParcel", e.toString());
        }
        //parcel.writeList(stands);
    }
}
