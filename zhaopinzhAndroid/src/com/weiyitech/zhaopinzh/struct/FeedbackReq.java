package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-1-25
 * Time: 下午7:17
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackReq  implements Parcelable {
    public int onFst;
    public int onScd;
    public int onThd;
    public int onFur;
    public int onFth;
    public String contentFst;
    public String  contentScd;
    public String  contentThd;
    public String  contentFur;
    public String  contentFth;
    public String  contentSix;
    public String  email;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(onFst);
        parcel.writeInt(onScd);
        parcel.writeInt(onThd);
        parcel.writeInt(onFur);
        parcel.writeInt(onFth);
        parcel.writeString(contentFst);
        parcel.writeString(contentScd);
        parcel.writeString(contentThd);
        parcel.writeString(contentFur);
        parcel.writeString(contentFth);
        parcel.writeString(contentSix);
        parcel.writeString(email);
    }
}
