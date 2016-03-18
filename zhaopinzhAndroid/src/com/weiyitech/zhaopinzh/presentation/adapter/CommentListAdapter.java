package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.CommentActivity;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.struct.Comment;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-11
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public class CommentListAdapter extends BaseAdapter {
    public Context context;
    ArrayList<Comment> commentArrayList;
    CommentActivity activity;
    public Boolean footViewExist = false;
    public int current;
    public int total;
    boolean mBusy;

    public static class ViewHolder {
        public View listItem;

        public TextView commentTextView;
        public RatingBar ratingBar;
        public TextView commentTimeTextView;
        public ImageView avatarImageView;
        public TextView nameTextView;
    }

    public CommentListAdapter(Activity context, ArrayList<Comment> commentArrayList) {
        super();
        this.context = context;
        this.commentArrayList = commentArrayList;
        activity = (CommentActivity) context;
    }

    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
    }

    @Override
    public int getCount() {
        if (footViewExist) {
            return commentArrayList.size() + 1;
        } else {
            return commentArrayList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (position < commentArrayList.size()) {
            return commentArrayList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (position == commentArrayList.size()) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.footer_view, null);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v.findViewById(R.id.footer_view_image_view);
                    Animation operatingAnim = AnimationUtils.loadAnimation(activity, R.anim.rotate);
                    LinearInterpolator lin = new LinearInterpolator();
                    operatingAnim.setInterpolator(lin);
                    imageView.setAnimation(operatingAnim);
                    imageView.startAnimation(operatingAnim);
                    CommentBusiness commentBusiness = new CommentBusiness();
                    Pager pager = new Pager();
                    pager.current = current + 20;
                    pager.length = 20;
                    Comment comment = new Comment();
                    comment.setEmployerId(commentArrayList.get(0).getEmployerId());
                    comment.setPager(pager);
                    commentBusiness.getEmployerComment(comment, CommentListAdapter.this.context);
                }
            });
            return v;
        }
        ViewHolder holder = null;
        if (v == null || v.getTag() == null) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.comment_list_item, null);
            holder = new ViewHolder();
            holder.listItem = v;
            holder.avatarImageView = (ImageView) v.findViewById(R.id.avatar_image_view);
            holder.commentTextView = (TextView) v.findViewById(R.id.comment_text_view);
            holder.nameTextView = (TextView) v.findViewById(R.id.name_text_view);
            holder.commentTimeTextView = (TextView) v.findViewById(R.id.time_text_view);
            holder.ratingBar = (RatingBar) v.findViewById(R.id.company_rating_bar);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.commentTimeTextView.setText(commentArrayList.get(position).getCommentTime());
        holder.commentTextView.setText(commentArrayList.get(position).getComment());
        holder.nameTextView.setText(commentArrayList.get(position).getUserName());
        holder.ratingBar.setRating(CommentUtil.getDiamondRating(commentArrayList.get(position)));
        holder.avatarImageView.setImageResource(R.drawable.default_avatar);
        String url = commentArrayList.get(position).getAvatarPath();
        if (url != null && !"".equals(url)) {
            ImageLoader mImageLoader = new ImageLoader(activity);
            if (!mBusy) {

                mImageLoader.DisplayImage(url, holder.avatarImageView, true, false);
            } else {
                //仅从cache下载
                mImageLoader.DisplayImage(url, holder.avatarImageView, true, true);
            }
            Log.i("smooth", "--" + position
                    + url) ;
        }

        return v;

    }
}
