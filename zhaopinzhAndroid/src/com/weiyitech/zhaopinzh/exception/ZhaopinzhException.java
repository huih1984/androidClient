package com.weiyitech.zhaopinzh.exception;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-2
 * Time: 下午1:47
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhException extends RuntimeException {
    public ZhaopinzhException() {
        super();
    }

    public ZhaopinzhException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        Log.e("ZhaopinzhException", detailMessage + " " + throwable.toString());
    }

    public ZhaopinzhException(Throwable throwable) {
        super(throwable);
        Log.e("ZhaopinzhException", throwable.toString());
    }

    public ZhaopinzhException(String detailMessage) {
        super(detailMessage);
        Log.e("ZhaopinzhException", detailMessage);
    }

}
