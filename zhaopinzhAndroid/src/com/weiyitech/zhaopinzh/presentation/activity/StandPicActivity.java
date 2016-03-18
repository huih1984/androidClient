package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.QueryStandPic;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.StandPicReq;
import com.weiyitech.zhaopinzh.struct.StandPicRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.MemoryCache;
import com.weiyitech.zhaopinzh.util.filemanager.CommonUtil;
import com.weiyitech.zhaopinzh.util.filemanager.FileHelper;
import com.weiyitech.zhaopinzh.util.photoview.PhotoViewAttacher;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-10
 * Time: 下午4:10
 * To change this template use File | Settings | File Templates.
 */
public class StandPicActivity extends CommonActivity implements BusinessInterface {
    String picPath = null;
    String stand;
    String fromWhere;
    final public static  int MSG_LOAD_IMG = 1;
    final public static  int MSG_ROTATE_DEFAULT = 2;
    int employerId;
    int jobFairId;
    int picId;
    ImageView imageView;
    Bitmap bitmap = null;
    private MemoryCache memoryCache = new MemoryCache();
    private PhotoViewAttacher mAttacher;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stand_activity);
        imageView = (ImageView) findViewById(R.id.stand_activity_imageView);
        handler.sendEmptyMessage(MSG_ROTATE_DEFAULT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        stand = getIntent().getStringExtra("standStr");
        TextView textView = (TextView)findViewById(R.id.stand_activity_textView);
        textView.setText("展位号" + stand);
        employerId = getIntent().getIntExtra("employerId", 0);
        jobFairId = getIntent().getIntExtra("fairId", 0);
        fromWhere = getIntent().getStringExtra("where");
        picId = getIntent().getIntExtra("standPicId", 0);
        picPath = ZhaopinzhApp.getInstance().standPicMap.get(picId);
        if(picPath != null){
            handler.sendEmptyMessage(MSG_LOAD_IMG);
        }else{
            StandPicReq standPicReq = new StandPicReq();
            standPicReq.fairId = jobFairId;
            standPicReq.employerId = employerId;
            QueryStandPic queryStandPic = new QueryStandPic();
            queryStandPic.queryStandPic(standPicReq, this);
        }
    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        if (dataType == Common.STANDPIC_DETAIL_TYPE) {
            try {
                List<StandPicRsp> standPicRspList =  (ArrayList<StandPicRsp>) t;
                for(StandPicRsp standPicRsp : standPicRspList){
                    picPath = standPicRsp.standPicPath;
                    break;
                }
                if(picPath == null || picPath.length() == 0){
                    imageView.clearAnimation();
                    imageView.setImageBitmap(bitmap);
                    imageView.setImageResource(R.drawable.standpic);
                    return;
                }
                handler.sendEmptyMessage(MSG_LOAD_IMG);
                ZhaopinzhApp.getInstance().standPicMap.put(picId, picPath);
            } catch (Exception e) {

            }
        } else if(dataType == Common.STANDPIC_GOT_PIC_TYPE) {
            handler.sendEmptyMessage(MSG_LOAD_IMG);
        }
    }

    /**
     * 句柄处理界面刷新事件
     */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_LOAD_IMG) {
                loadPic();
            } else if (msg.what == MSG_ROTATE_DEFAULT) {
                Animation operatingAnim = AnimationUtils.loadAnimation(StandPicActivity.this, R.anim.rotate);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                imageView.startAnimation(operatingAnim);
            }
        }
    };

    void loadPic() {
        String url = Common.URL + "/" + picPath;
        String filePath = CommonUtil.getRootFilePath();
        String[] subStr = url.split("/");
        if (filePath != null) {
            filePath += "com.zhaopinzh/standPic/" + subStr[subStr.length - 1];
        }
        /*Bitmap bitmap = memoryCache.get(filePath);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }else*/ if (FileHelper.fileIsExist(filePath)) {
            File f = new File(filePath);
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, opt);
                imageView.clearAnimation();
                imageView.setImageBitmap(bitmap);
                memoryCache.put(filePath, bitmap);
            } catch (Exception e) {

            }
            //mImageLoader.DisplayImage(url, imageView, false);
            // The MAGIC happens here!
            mAttacher = new PhotoViewAttacher(imageView);
            // Lets attach some listeners, not required though!
            mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
            mAttacher.setOnPhotoTapListener(new PhotoTapListener());
            mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            QueryStandPic queryStandPic = new QueryStandPic();
            queryStandPic.getStandPic(url, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
           /* float xPercentage = x * 100f;
            float yPercentage = y * 100f;

            if (null != mCurrentToast) {
                mCurrentToast.cancel();
            }

            mCurrentToast = Toast.makeText(SimpleSampleActivity.this,
                    String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage), Toast.LENGTH_SHORT);
            mCurrentToast.show();     */
        }
    }

    private class MatrixChangeListener implements PhotoViewAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {
           // mCurrMatrixTv.setText(rect.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // action bar中的应用程序图标被点击了，返回home
                if (fromWhere.equals(HomeActivity.class.getName())) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                } else if (fromWhere.equals(JobsOfFairActivity.class.getName())) {
                    Intent intent = new Intent(this, JobsOfFairActivity.class);
                    startActivity(intent);
                }
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            case R.id.search_activity_action_search:
                onSearchRequested();
                return true;
            case R.id.standpic_activity_refresh:
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                imageView.setImageResource(R.drawable.ic_menu_refresh);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                handler.sendEmptyMessage(MSG_ROTATE_DEFAULT);
                StandPicReq standPicReq = new StandPicReq();
                standPicReq.fairId = jobFairId;
                standPicReq.employerId = employerId;
                QueryStandPic queryStandPic = new QueryStandPic();
                queryStandPic.queryStandPic(standPicReq, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.standpic_activiity_actions, menu);
        return true;
    }
}
