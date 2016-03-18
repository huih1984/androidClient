package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-19
 * Time: 上午8:41
 * To change this template use File | Settings | File Templates.
 */
public class RunEmployerComponent implements Parcelable {
    public RunEmployer runEmployer;
    public ArrayList<JobConciseRsp> jobConciseRspList;
    public boolean isFavorite;
    public boolean expanded;
    public boolean favoriteValidMessage;
    // 记录当前选中位置
    public int currentIndex;
    public int position;
    public int commentCnt;
    public RunEmployerComponent() {
        currentIndex = 0;
        position = -1;
        expanded = false;
        favoriteValidMessage = false;
        isFavorite = false;
    }

    public static final Parcelable.Creator<RunEmployerComponent> CREATOR
            = new Parcelable.Creator<RunEmployerComponent>() {
        public RunEmployerComponent createFromParcel(Parcel in) {
            try {
                RunEmployerComponent runEmployerComponent = new RunEmployerComponent();
                runEmployerComponent.runEmployer = in.readParcelable(RunEmployer.class.getClassLoader());
                Parcelable[] jobConciseRsps = in.readParcelableArray(JobConciseRsp.class.getClassLoader());
                runEmployerComponent.jobConciseRspList = new ArrayList<JobConciseRsp>();
                for (Parcelable obj : jobConciseRsps) {
                    runEmployerComponent.jobConciseRspList.add((JobConciseRsp) obj);
                }
                runEmployerComponent.isFavorite = in.readByte() != 0;
                runEmployerComponent.expanded = in.readByte() != 0;
                runEmployerComponent.favoriteValidMessage = in.readByte() != 0;
                runEmployerComponent.currentIndex = in.readInt();
                runEmployerComponent.position = in.readInt();
                runEmployerComponent.commentCnt = in.readInt();
                return runEmployerComponent;
            } catch (Exception e) {
                Log.e("zhaopinzhException createFromParcel", e.toString());
            }
            return null;
        }

        public RunEmployerComponent[] newArray(int size) {
            return new RunEmployerComponent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        try {
            parcel.writeParcelable(runEmployer, flags);
            parcel.writeParcelableArray((jobConciseRspList.toArray(new JobConciseRsp[jobConciseRspList.size()])), flags);
            parcel.writeByte((byte) (isFavorite ? 1 : 0));
            parcel.writeByte((byte) (expanded ? 1 : 0));
            parcel.writeByte((byte) (favoriteValidMessage ? 1 : 0));
            parcel.writeInt(currentIndex);
            parcel.writeInt(position);
            parcel.writeInt(commentCnt);
        } catch (Exception e) {
            Log.e("zhaopinzhException writeToParcel", e.toString());
        }
    }
}
