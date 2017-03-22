package com.srinnix.kindergarten.custom;

import android.support.annotation.NonNull;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 3/14/2017.
 */

public class ItemChatAnimator extends SimpleItemAnimator {
    private static final boolean DEBUG = false;

    private ArrayList<ViewHolder> mPendingRemovals = new ArrayList<>();
    private ArrayList<ViewHolder> mPendingAdditions = new ArrayList<>();

    ArrayList<ArrayList<ViewHolder>> mAdditionsList = new ArrayList<>();

    ArrayList<ViewHolder> mAddAnimations = new ArrayList<>();
    ArrayList<ViewHolder> mRemoveAnimations = new ArrayList<>();

    @Override
    public void runPendingAnimations() {
        boolean removalsPending = !mPendingRemovals.isEmpty();
        boolean additionsPending = !mPendingAdditions.isEmpty();
        if (!removalsPending) {
            // nothing to animate
            return;
        }
        // First, remove stuff
        for (ViewHolder holder : mPendingRemovals) {
            animateRemoveImpl(holder);
        }
        mPendingRemovals.clear();

        // Next, add stuff
        if (additionsPending) {
            final ArrayList<ViewHolder> additions = new ArrayList<>();
            additions.addAll(mPendingAdditions);
            mAdditionsList.add(additions);
            mPendingAdditions.clear();
            Runnable adder = () -> {
                for (ViewHolder holder : additions) {
                    animateAddImpl(holder);
                }
                additions.clear();
                mAdditionsList.remove(additions);
            };
            if (removalsPending) {
                long removeDuration = removalsPending ? getRemoveDuration() : 0;
                long totalDelay = removeDuration;
                View view = additions.get(0).itemView;
                ViewCompat.postOnAnimationDelayed(view, adder, totalDelay);
            } else {
                adder.run();
            }
        }
    }

    @Override
    public boolean animateRemove(final ViewHolder holder) {
        resetAnimation(holder);
        mPendingRemovals.add(holder);
        return true;
    }

    private void animateRemoveImpl(final ViewHolder holder) {
        final View view = holder.itemView;

        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mRemoveAnimations.add(holder);

        animation.setDuration(getRemoveDuration())
                .translationY(view.getHeight()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchRemoveStarting(holder);
            }

            @Override
            public void onAnimationEnd(View view) {
                animation.setListener(null);
//                ViewCompat.setY(view, view.getHeight());
                ViewCompat.setAlpha(view, 1);
                dispatchRemoveFinished(holder);
                mRemoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    @Override
    public boolean animateAdd(final ViewHolder holder) {
        resetAnimation(holder);
//        ViewCompat.setY(holder.itemView, holder.itemView.getHeight());
        mPendingAdditions.add(holder);
        return true;
    }

    void animateAddImpl(final ViewHolder holder) {
        final View view = holder.itemView;

        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mAddAnimations.add(holder);
        animation.translationY(0)
                .setDuration(getAddDuration()).
                setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
//                        ViewCompat.setY(view, 0);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        dispatchAddFinished(holder);
                        mAddAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                    }
                }).start();
    }

    @Override
    public boolean animateMove(final ViewHolder holder, int fromX, int fromY,
                               int toX, int toY) {
        return false;
    }

    @Override
    public boolean animateChange(ViewHolder oldHolder, ViewHolder newHolder,
                                 int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public void endAnimation(ViewHolder item) {
        final View view = item.itemView;
        // this will trigger end callback which should set properties to their target values.
        ViewCompat.animate(view).cancel();
        // TODO if some other animations are chained to end, how do we cancel them as well?
        if (mPendingRemovals.remove(item)) {
//            ViewCompat.setY(view, view.getHeight());
            ViewCompat.setAlpha(view, 1);
            dispatchRemoveFinished(item);
        }
        if (mPendingAdditions.remove(item)) {
//            ViewCompat.setY(view, 0);
            ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(item);
        }

        for (int i = mAdditionsList.size() - 1; i >= 0; i--) {
            ArrayList<ViewHolder> additions = mAdditionsList.get(i);
            if (additions.remove(item)) {
//                ViewCompat.setY(view, 0);
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(i);
                }
            }
        }

        // animations should be ended by the cancel above.
        //noinspection PointlessBooleanExpression,ConstantConditions
        if (mRemoveAnimations.remove(item) && DEBUG) {
            throw new IllegalStateException("after animation is cancelled, item should not be in "
                    + "mRemoveAnimations list");
        }

        //noinspection PointlessBooleanExpression,ConstantConditions
        if (mAddAnimations.remove(item) && DEBUG) {
            throw new IllegalStateException("after animation is cancelled, item should not be in "
                    + "mAddAnimations list");
        }

        dispatchFinishedWhenDone();
    }

    private void resetAnimation(ViewHolder holder) {
        AnimatorCompatHelper.clearInterpolator(holder.itemView);
        endAnimation(holder);
    }

    @Override
    public boolean isRunning() {
        return (!mPendingAdditions.isEmpty() ||
                !mPendingRemovals.isEmpty() ||
                !mRemoveAnimations.isEmpty() ||
                !mAddAnimations.isEmpty() ||
                !mAdditionsList.isEmpty());
    }

    /**
     * Check the state of currently pending and running animations. If there are none
     * pending/running, call {@link #dispatchAnimationsFinished()} to notify any
     * listeners.
     */
    void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    @Override
    public void endAnimations() {
        int count = mPendingRemovals.size();
        for (int i = count - 1; i >= 0; i--) {
            ViewHolder item = mPendingRemovals.get(i);
            dispatchRemoveFinished(item);
            mPendingRemovals.remove(i);
        }
        count = mPendingAdditions.size();
        for (int i = count - 1; i >= 0; i--) {
            ViewHolder item = mPendingAdditions.get(i);
            View view = item.itemView;
//            ViewCompat.setY(view, 0);
            ViewCompat.setAlpha(view, 1);
            dispatchAddFinished(item);
            mPendingAdditions.remove(i);
        }
        if (!isRunning()) {
            return;
        }

        int listCount = mAdditionsList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            ArrayList<ViewHolder> additions = mAdditionsList.get(i);
            count = additions.size();
            for (int j = count - 1; j >= 0; j--) {
                ViewHolder item = additions.get(j);
                View view = item.itemView;
//                ViewCompat.setY(view, 0);
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                additions.remove(j);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(additions);
                }
            }
        }

        cancelAll(mRemoveAnimations);
        cancelAll(mAddAnimations);

        dispatchAnimationsFinished();
    }

    void cancelAll(List<ViewHolder> viewHolders) {
        for (int i = viewHolders.size() - 1; i >= 0; i--) {
            ViewCompat.animate(viewHolders.get(i).itemView).cancel();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the payload list is not empty, DefaultItemAnimator returns <code>true</code>.
     * When this is the case:
     * <ul>
     * <li>If you override {@link #animateChange(ViewHolder, ViewHolder, int, int, int, int)}, both
     * ViewHolder arguments will be the same instance.
     * </li>
     * <li>
     * If you are not overriding {@link #animateChange(ViewHolder, ViewHolder, int, int, int, int)},
     * then DefaultItemAnimator will call {@link #animateMove(ViewHolder, int, int, int, int)} and
     * run a move animation instead.
     * </li>
     * </ul>
     */
    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull ViewHolder viewHolder,
                                             @NonNull List<Object> payloads) {
        return !payloads.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, payloads);
    }

    private static class VpaListenerAdapter implements ViewPropertyAnimatorListener {
        VpaListenerAdapter() {
        }

        @Override
        public void onAnimationStart(View view) {
        }

        @Override
        public void onAnimationEnd(View view) {
        }

        @Override
        public void onAnimationCancel(View view) {
        }
    }
}
