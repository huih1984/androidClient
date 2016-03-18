package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-2
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
public class RunEmployerPager implements Parcelable {
    public Pager pager;
    public List<RunEmployer> list;

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<RunEmployerPager> CREATOR
            = new Parcelable.Creator<RunEmployerPager>() {
        public RunEmployerPager createFromParcel(Parcel in) {
            RunEmployerPager runEmployerPager = new RunEmployerPager();
            runEmployerPager.pager = in.readParcelable(Pager.class.getClassLoader());
            Parcelable[] runEmployerArray = in.readParcelableArray(RunEmployer.class.getClassLoader());
            runEmployerPager.list =  new ArrayList<RunEmployer>();
            for(Parcelable obj : runEmployerArray){
                runEmployerPager.list.add((RunEmployer)obj);
            }
            return runEmployerPager;
        }

        public RunEmployerPager[] newArray(int size) {
            return new RunEmployerPager[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(pager, i);
        parcel.writeArray(list.toArray());
    }
}
