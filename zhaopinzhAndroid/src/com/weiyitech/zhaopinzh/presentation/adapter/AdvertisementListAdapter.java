package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-15
 * Time: 上午9:34
 * To change this template use File | Settings | File Templates.
 */
public class AdvertisementListAdapter extends PagerAdapter {

    // 界面列表
    private List<View> views;
    private Activity activity;

    public AdvertisementListAdapter(List<View> views, Activity out_activity) {
        this.views = views;
        this.activity = out_activity;
    }

    // 销毁arg1位置的界面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }

//    @Override
//    public void finishUpdate(View arg0) {
//    }

    // 获得当前界面数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // 初始化arg1位置的界面
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(views.get(arg1), 0);
        return views.get(arg1);
    }


    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }
}


//    ArrayList<AdvertisementEmployer> advertisementConcises;
//    AdvertisementActivity activity;
//    ImageLoader mImageLoader;
//    boolean mBusy;
//
//    public static class ViewHolder {
//        public View listItem;
//
//        public ImageView logoImgView;
//        public TextView fairNameTxtView;
//        public TextView timingTxtView;
//        public TextView standNumberTxtView;
//    }
//
//    public AdvertisementListAdapter(Activity context, ArrayList<AdvertisementEmployer> advertisementConcises) {
//        super();
//        this.advertisementConcises = advertisementConcises;
//        activity = (AdvertisementActivity)context;
//        mImageLoader = new ImageLoader(context);
//    }
//
//    @Override
//    public int getCount() {
//        return advertisementConcises.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return advertisementConcises.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public void setFlagBusy(boolean busy) {
//        this.mBusy = busy;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
//        ViewHolder holder;
//        if (v == null) {
//            LayoutInflater vi =
//                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = vi.inflate(R.layout.advertisement_viewpager_item, null);
//            holder = new ViewHolder();
//            holder.logoImgView = (ImageView)v.findViewById(R.id.advertisment_gallery_item_image);
//            v.setTag(holder);
//        } else holder = (ViewHolder) v.getTag();
//
//        //异步下载logo,复用会导致没图片的地方出现错误图片，所以这里要首先设置成默认
//        holder.logoImgView.setImageResource(R.drawable.company_logo);
////        String url = Common.URL + "/" + advertisementConcises.get(position).getImagePath();
////        if (!mBusy) {
////            mImageLoader.DisplayImage(url, holder.logoImgView, false);
////        } else {
////            mImageLoader.DisplayImage(url, holder.logoImgView, false);
////        }
//        if (position == 0) {
//            holder.logoImgView.setImageResource(R.drawable.x1);
//           // holder.listItem.setBackgroundColor(0XFFCC9933);
//        } else if (position == 1) {
//            holder.logoImgView.setImageResource(R.drawable.x2);
//           // holder.listItem.setBackgroundColor(0XFF660066);
//        } else if (position == 2) {
//            holder.logoImgView.setImageResource(R.drawable.x3);
//           // holder.listItem.setBackgroundColor(0XFF006699);
//        } else if (position == 3) {
//            holder.logoImgView.setImageResource(R.drawable.x4);
//           // holder.listItem.setBackgroundColor(0XFF99CC33);
//        }  else if (position == 4) {
//            holder.logoImgView.setImageResource(R.drawable.x5);
//            // holder.listItem.setBackgroundColor(0XFF99CC33);
//        }  else if (position == 5) {
//            holder.logoImgView.setImageResource(R.drawable.x6);
//            // holder.listItem.setBackgroundColor(0XFF99CC33);
//        }
//
//
//
//        return v;
//
//    }
//}
