package com.weiyitech.zhaopinzh.util.slideexpandablelistview;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.*;

/**
 * Wraps a ListAdapter to give it expandable list view functionality.
 * The main thing it does is add a listener to the getToggleButton
 * which expands the getExpandableView for each list item.
 *
 * @author tjerk
 * @date 6/9/12 4:41 PM
 */
public abstract class AbstractSlideExpandableListAdapter extends WrapperListAdapterImpl {
    /**
     * Reference to the last expanded list item.
     * Since lists are recycled this might be null if
     * though there is an expanded list item
     */
    private View lastOpen = null;
    /**
     * The position of the last expanded list item.
     * If -1 there is no list item expanded.
     * Otherwise it points to the position of the last expanded list item
     */
    private int lastOpenPosition = -1;

    /**
     * Default Animation duration
     * Set animation duration with @see setAnimationDuration
     */
    private int animationDuration = 0;

    public BitSet getOpenItems() {
        return openItems;
    }

    public void setOpenItems(BitSet openItems) {
        this.openItems = openItems;
    }

    /**
     * A list of positions of all list items that are expanded.
     * Normally only one is expanded. But a mode to expand
     * multiple will be added soon.
     * <p/>
     * If an item onj position x is open, its bit is set
     */
    private BitSet openItems = new BitSet();

    /**
     * 增加一个标志是否发生了需要展开的点击事件的变量
     */
    private ArrayList<Map<Integer, Integer>> clickedtoExpendItems = new ArrayList<Map<Integer, Integer>>();

    /**
     * We remember, for each collapsable view its height.
     * So we dont need to recalculate.
     * The height is calculated just before the view is drawn.
     */
    private final SparseIntArray viewHeights = new SparseIntArray(10);

    public AbstractSlideExpandableListAdapter(ListAdapter wrapped) {
        super(wrapped);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = wrapped.getView(position, view, viewGroup);
        enableFor(view, position);
        return view;
    }

    /**
     * This method is used to get the Button view that should
     * expand or collapse the Expandable View.
     * <br/>
     * Normally it will be implemented as:
     * <pre>
     * return parent.findViewById(R.id.expand_toggle_button)
     * </pre>
     * <p/>
     * A listener will be attached to the button which will
     * either expand or collapse the expandable view
     *
     * @param parent the list view item
     * @return a child of parent which is a button
     * @ensure return!=null
     * @see #getExpandableView(android.view.View)
     */
    public abstract View getExpandToggleButton(View parent);

    /**
     * This method is used to get the view that will be hidden
     * initially and expands or collapse when the ExpandToggleButton
     * is pressed @see getExpandToggleButton
     * <br/>
     * Normally it will be implemented as:
     * <pre>
     * return parent.findViewById(R.id.expandable)
     * </pre>
     *
     * @param parent the list view item
     * @return a child of parent which is a view (or often ViewGroup)
     *         that can be collapsed and expanded
     * @ensure return!=null
     * @see #getExpandToggleButton(android.view.View)
     */
    public abstract View getExpandableView(View parent);

    /**
     * Gets the duration of the collapse animation in ms.
     * Default is 330ms. Override this method to change the default.
     *
     * @return the duration of the anim in ms
     */
    public int getAnimationDuration() {
        return animationDuration;
    }

    /**
     * Set's the Animation duration for the Expandable animation
     *
     * @param duration The duration as an integer in MS (duration > 0)
     * @throws IllegalArgumentException if parameter is less than zero
     */
    public void setAnimationDuration(int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration is less than zero");
        }

        animationDuration = duration;
    }

    /**
     * Check's if any position is currently Expanded
     * To collapse the open item @see collapseLastOpen
     *
     * @return boolean True if there is currently an item expanded, otherwise false
     */
    public boolean isAnyItemExpanded() {
        return (lastOpenPosition != -1) ? true : false;
    }

    public void enableFor(View parent, int position) {
        View more = getExpandToggleButton(parent);
        View itemToolbar = getExpandableView(parent);
        itemToolbar.measure(parent.getWidth(), parent.getHeight());

        enableFor(more, itemToolbar, position);
    }


    private void enableFor(final View button, final View target, final int position) {
        if (target == lastOpen && position != lastOpenPosition) {
            // lastOpen is recycled, so its reference is false
            lastOpen = null;
        }
        if (position == lastOpenPosition) {
            // re reference to the last view
            // so when can animate it when collapsed
            lastOpen = target;
        }
        int height = viewHeights.get(position, -1);

        if (height == -1) {
            viewHeights.put(position, target.getMeasuredHeight());
            updateExpandable(target, position);
        } else {
            updateExpandable(target, position);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Animation a = target.getAnimation();

                if (a != null && a.hasStarted() && !a.hasEnded()) {

                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.performClick();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                } else {

                    target.setAnimation(null);

                    int type = target.getVisibility() == View.VISIBLE
                            ? ExpandCollapseAnimation.COLLAPSE
                            : ExpandCollapseAnimation.EXPAND;

                    // remember the state
                    if (type == ExpandCollapseAnimation.EXPAND) {
                        openItems.set(position, true);
                        Map map = new HashMap<Integer, Integer>();
                        map.put(position, 2);
                        clickedtoExpendItems.add(map);
                    } else {
                        openItems.set(position, false);
                    }
                    // check if we need to collapse a different view

                    if (type == ExpandCollapseAnimation.EXPAND) {
                        if (lastOpenPosition != -1 && lastOpenPosition != position) {
                            if (lastOpen != null) {
                                animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
                            }
                            openItems.set(lastOpenPosition, false);
                        }
                        lastOpen = target;
                        lastOpenPosition = position;
                    } else if (lastOpenPosition == position) {
                        lastOpenPosition = -1;
                    }
                    animateView(target, type);
                    /*添加展开的额外操作*/
                    expandViewExtraAction(button, target, position, type);
                }
            }
        });
    }

    abstract void expandViewExtraAction(View button, View target, int position, int action);

    private void updateExpandable(View target, int position) {

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) target.getLayoutParams();
        if (openItems.get(position)) {
            target.setVisibility(View.VISIBLE);
            params.bottomMargin = 0;
        } else {
            target.setVisibility(View.GONE);
            params.bottomMargin = 0 - viewHeights.get(position);
        }
    }

    /**
     * Performs either COLLAPSE or EXPAND animation on the target view
     *
     * @param target the view to animate
     * @param type   the animation type, either ExpandCollapseAnimation.COLLAPSE
     *               or ExpandCollapseAnimation.EXPAND
     */
    private void animateView(final View target, final int type) {
        Animation anim = new ExpandCollapseAnimation(
                target,
                type
        );
        anim.setDuration(getAnimationDuration());
        target.startAnimation(anim);
    }


    /**
     * Closes the current open item.
     * If it is current visible it will be closed with an animation.
     *
     * @return true if an item was closed, false otherwise
     */
    public boolean collapseLastOpen() {
        if (isAnyItemExpanded()) {
            // if visible animate it out
            if (lastOpen != null) {
                //          animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
            }
            openItems.set(lastOpenPosition, false);
            lastOpenPosition = -1;
            return true;
        }
        return false;
    }

    public Parcelable onSaveInstanceState(Parcelable parcelable) {

        SavedState ss = new SavedState(parcelable);
        ss.lastOpenPosition = this.lastOpenPosition;
        ss.openItems = this.openItems;
        return ss;
    }

    public void onRestoreInstanceState(SavedState state) {

        this.lastOpenPosition = state.lastOpenPosition;
        this.openItems = state.openItems;
    }

    /**
     * Utility methods to read and write a bitset from and to a Parcel
     */
    private static BitSet readBitSet(Parcel src) {
        int cardinality = src.readInt();

        BitSet set = new BitSet();
        for (int i = 0; i < cardinality; i++) {
            set.set(src.readInt());
        }

        return set;
    }

    private static void writeBitSet(Parcel dest, BitSet set) {
        int nextSetBit = -1;
        if (set != null) {
            dest.writeInt(set.cardinality());

            while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
                dest.writeInt(nextSetBit);
            }
        }
    }

    /**
     * The actual state class
     */
    static class SavedState extends View.BaseSavedState {
        public BitSet openItems = null;
        public int lastOpenPosition = -1;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            in.writeInt(lastOpenPosition);
            writeBitSet(in, openItems);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            lastOpenPosition = out.readInt();
            openItems = readBitSet(out);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
