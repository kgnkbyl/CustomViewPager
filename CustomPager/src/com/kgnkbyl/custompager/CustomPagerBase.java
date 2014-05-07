package com.kgnkbyl.custompager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class CustomPagerBase {

    private static final int NEXT_PAGE = 1;
    private static final int PREVIOUS_PAGE = 2;
    private Activity context;
    private RelativeLayout rootView;
    private LayoutInflater inflater;
    private View currentItem, nextItem, previousItem, removedItem;
    private View[] viewList;
    private int currentPosition = 0;
    private int nextItemXPosition;
    private int previousItemXPosition;
    private boolean isTouchEnabled = true;
    private CustomPagerAdapter pagerAdapter;
    
    private static CustomPagerBase me;
    public static CustomPagerBase getInstance()
    {
        return me;
    }

    public CustomPagerBase(Activity context, RelativeLayout rootView, CustomPagerAdapter pagerAdapter) {
        this.context      = context;
        this.rootView     = rootView;
        this.pagerAdapter = pagerAdapter;
        viewList          = new View[pagerAdapter.dataCount()];
        
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }
        me = this;
    }

    public void preparePager(int position) {
        nextItemXPosition     = HelperClass.getCurrentCardPositionX(context) + Integer.parseInt(context.getResources().getString(R.string.card_position_x));
        previousItemXPosition = HelperClass.getCurrentCardPositionX(context) - Integer.parseInt(context.getResources().getString(R.string.card_position_x));
        if (pagerAdapter != null && pagerAdapter.dataCount() > 0) {
            if (position != 0 && pagerAdapter.dataCount() > 1) {
                // Create previous view
                previousItem = createCardLayout(position - 1);
                setTouchListenerToView(previousItem, false);
                ViewHelper.setScaleX(previousItem, 0.7f);
                ViewHelper.setScaleY(previousItem, 0.7f);
                ObjectAnimator.ofFloat(previousItem, "translationX", 0, previousItemXPosition).setDuration(1).start();
            }
            if (pagerAdapter.dataCount() - 2 >= position) {
                // Create next view
                nextItem = createCardLayout(position + 1);
                setTouchListenerToView(nextItem, false);
                ViewHelper.setScaleX(nextItem, 0.7f);
                ViewHelper.setScaleY(nextItem, 0.7f);
                ObjectAnimator.ofFloat(nextItem, "translationX", 0, nextItemXPosition).setDuration(1).start();
            }
            if (pagerAdapter.dataCount() >= 1) {
            	// Create the view for the selected position
                currentItem = createCardLayout(position);
                setTouchListenerToView(currentItem, true);
                ObjectAnimator.ofFloat(currentItem, "translationX", 0, HelperClass.getCurrentCardPositionX(context)).setDuration(1).start();
            }
        }
        currentPosition = position;
    }
    
    private View createCardLayout(int position) {
    	View itemView;
    	try{
    		View convertView = viewList[position];
    		if(convertView == null)
    			itemView = pagerAdapter.getView(position, null);
    		else
    			itemView = pagerAdapter.getView(position, viewList[position]);
    	}catch(NullPointerException e){
    		itemView = pagerAdapter.getView(position, null);
    	}
    	viewList[position] = itemView;
        rootView.addView(itemView);
        return itemView;
    }

    private void setTouchListenerToView(final View itemView, boolean state) {
        if (state) {
            itemView.setOnTouchListener(touchListener(itemView));
        } else {
            itemView.setOnTouchListener(null);
        }
    }

    public void setIsTouchEnabled(boolean isEnabled)
    {
        isTouchEnabled=isEnabled;
    }
    
    private View.OnTouchListener touchListener(final View itemView) {
        return new View.OnTouchListener() {
            int lastX;
            int firstTouchX;

            public boolean onTouch(View v, MotionEvent event) {
            	
            	if(isTouchEnabled){
	                final int X = (int) event.getRawX();
	                float viewXPosition = ViewHelper.getX(itemView);
	                switch (event.getAction() & MotionEvent.ACTION_MASK) {
	
	                    case MotionEvent.ACTION_DOWN:
	                    	lastX = X;
	                        firstTouchX = X;
	                        break;
	                    case MotionEvent.ACTION_MOVE:
                            ViewHelper.setX(itemView, viewXPosition + (X - lastX));
                            lastX = X;
                            ViewHelper.setScaleY(itemView, getScaleValue(viewXPosition));
                            ViewHelper.setScaleX(itemView, getScaleValue(viewXPosition));
	                        break;
	                    case MotionEvent.ACTION_UP:
	                    	if (Math.abs(lastX - firstTouchX) < 5) {
	                    		// Click state
	                        } else if (lastX - firstTouchX > 100) {
	                            if (previousItem != null)
	                                changePageTo(PREVIOUS_PAGE);
	                            else {
	                                AnimatorSet set = new AnimatorSet();
	                                set.playTogether(
	                                        ObjectAnimator.ofFloat(itemView, "translationX", viewXPosition, HelperClass.getCurrentCardPositionX(context)),
	                                        ObjectAnimator.ofFloat(itemView, "scaleX", ViewHelper.getScaleX(itemView), 1f),
	                                        ObjectAnimator.ofFloat(itemView, "scaleY", ViewHelper.getScaleY(itemView), 1f),
	                                        ObjectAnimator.ofFloat(itemView, "alpha", ViewHelper.getAlpha(itemView), 1f)
	                                );
	                                set.setDuration(300);
	                                set.start();
	                            }
	                        } else if (firstTouchX - lastX > 100) {
	                            if (nextItem != null)
	                                changePageTo(NEXT_PAGE);
	                            else {
	                                AnimatorSet set = new AnimatorSet();
	                                set.playTogether(
	                                        ObjectAnimator.ofFloat(itemView, "translationX", viewXPosition, HelperClass.getCurrentCardPositionX(context)),
	                                        ObjectAnimator.ofFloat(itemView, "scaleX", ViewHelper.getScaleX(itemView), 1f),
	                                        ObjectAnimator.ofFloat(itemView, "scaleY", ViewHelper.getScaleY(itemView), 1f),
	                                        ObjectAnimator.ofFloat(itemView, "alpha", ViewHelper.getAlpha(itemView), 1f)
	                                );
	                                set.setDuration(300);
	                                set.start();
	                            }
	                        } else {
	                            AnimatorSet set = new AnimatorSet();
	                            set.playTogether(
	                                    ObjectAnimator.ofFloat(itemView, "translationX", viewXPosition, HelperClass.getCurrentCardPositionX(context)),
	                                    ObjectAnimator.ofFloat(itemView, "scaleX", ViewHelper.getScaleX(itemView), 1f),
	                                    ObjectAnimator.ofFloat(itemView, "scaleY", ViewHelper.getScaleY(itemView), 1f),
	                                    ObjectAnimator.ofFloat(itemView, "alpha", ViewHelper.getAlpha(itemView), 1f)
	                            );
	                            set.setDuration(300);
	                            set.start();
	                        }
	                        break;
	                }
            	}
                return true;
            }
        };
    }

    private void changePageTo(int direction) {
        if (direction == NEXT_PAGE) {
            setTouchListenerToView(nextItem, true);
            setTouchListenerToView(currentItem, false);
            if (nextItem != null) {
                nextItem.bringToFront();
                rootView.invalidate();
            }
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(currentItem, "translationX", ViewHelper.getX(currentItem), previousItemXPosition),
                    ObjectAnimator.ofFloat(currentItem, "scaleX", ViewHelper.getScaleX(currentItem), 0.7f),
                    ObjectAnimator.ofFloat(currentItem, "scaleY", ViewHelper.getScaleY(currentItem), 0.7f),
                    ObjectAnimator.ofFloat(currentItem, "alpha", ViewHelper.getAlpha(currentItem), 0.7f),

                    ObjectAnimator.ofFloat(nextItem, "translationX", nextItemXPosition, HelperClass.getCurrentCardPositionX(context)),
                    ObjectAnimator.ofFloat(nextItem, "scaleX", ViewHelper.getScaleX(nextItem), 1f),
                    ObjectAnimator.ofFloat(nextItem, "scaleY", ViewHelper.getScaleY(nextItem), 1f),
                    ObjectAnimator.ofFloat(nextItem, "alpha", ViewHelper.getAlpha(nextItem), 1f)
            );
            set.setDuration(300);
            set.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator arg0) {
					isTouchEnabled = false;
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0) {}
				
				@Override
				public void onAnimationEnd(Animator arg0) {
					isTouchEnabled = true;
					removedItem = previousItem;
		            previousItem = currentItem;
		            currentItem = nextItem;
		            if (currentPosition + 2 == pagerAdapter.dataCount()) {
		                nextItem = null;
		            } else {
		                View nextNext = createCardLayout(currentPosition + 2);
		                ViewHelper.setX(nextNext, nextItemXPosition);
		                ViewHelper.setScaleX(nextNext, 0.7f);
		                ViewHelper.setScaleY(nextNext, 0.7f);
		                ViewHelper.setAlpha(nextNext, 0.7f);
		                currentItem.bringToFront();
		                rootView.invalidate();
		            	ObjectAnimator anim = ObjectAnimator.ofFloat(nextNext, "translationX", ViewHelper.getX(nextNext)+500, ViewHelper.getX(nextNext));
        		    	anim.setDuration(200);
        		    	anim.start();
		                nextItem = nextNext;
		            }

		            if (removedItem != null) {
		            	rootView.post( new Runnable() {
							public void run() {
								context.runOnUiThread(new Runnable() {
									@Override
									public void run() {
						                rootView.removeView(removedItem);
									}
								});
							}
						});
		            }
		            currentPosition++;

				}
				
				@Override
				public void onAnimationCancel(Animator arg0) {}
			});
            set.start();
            
        } else if (direction == PREVIOUS_PAGE) {
            setTouchListenerToView(previousItem, true);
            setTouchListenerToView(currentItem, false);

            if (previousItem != null) {
                previousItem.bringToFront();
                rootView.invalidate();
            }
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(currentItem, "translationX", ViewHelper.getX(currentItem), nextItemXPosition),
                    ObjectAnimator.ofFloat(currentItem, "scaleX", ViewHelper.getScaleX(currentItem), 0.7f),
                    ObjectAnimator.ofFloat(currentItem, "scaleY", ViewHelper.getScaleY(currentItem), 0.7f),
                    ObjectAnimator.ofFloat(currentItem, "alpha", ViewHelper.getAlpha(currentItem), 0.7f),

                    ObjectAnimator.ofFloat(previousItem, "translationX", previousItemXPosition, HelperClass.getCurrentCardPositionX(context)),
                    ObjectAnimator.ofFloat(previousItem, "scaleX", ViewHelper.getScaleX(previousItem), 1f),
                    ObjectAnimator.ofFloat(previousItem, "scaleY", ViewHelper.getScaleY(previousItem), 1f),
                    ObjectAnimator.ofFloat(previousItem, "alpha", ViewHelper.getAlpha(previousItem), 1f)
            );
            set.setDuration(300);
            set.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator arg0) {
					isTouchEnabled = false;
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0) {}
				
				@Override
				public void onAnimationEnd(Animator arg0) {
					isTouchEnabled = true;
					removedItem = nextItem;
		            nextItem = currentItem;
		            currentItem = previousItem;

		            if (currentPosition - 1 == 0) {
		                previousItem = null;
		            } else {
		                View prevPrev = createCardLayout(currentPosition - 2);
		                ViewHelper.setX(prevPrev, previousItemXPosition);
		                ViewHelper.setScaleX(prevPrev, 0.7f);
		                ViewHelper.setScaleY(prevPrev, 0.7f);
		                ViewHelper.setAlpha(prevPrev, 0.7f);
		                ObjectAnimator anim = ObjectAnimator.ofFloat(prevPrev, "translationX", ViewHelper.getX(prevPrev)-500, ViewHelper.getX(prevPrev));
        		    	anim.setDuration(200);
        		    	anim.start();
        		    	currentItem.bringToFront();
		                rootView.invalidate();
		                previousItem = prevPrev;
		            }

		            if (removedItem != null) {
		            	context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
				                rootView.removeView(removedItem);
							}
						});
		            }
		            currentPosition--;
				}
				
				@Override
				public void onAnimationCancel(Animator arg0) {}
			});
            set.start();
            previousItem.bringToFront();
            rootView.invalidate();
        }
    }

    private float getScaleValue(float currentPoint) {
        float value = (float) (((HelperClass.getCurrentCardPositionX(context) - currentPoint) * 0.3) / HelperClass.getCurrentCardPositionX(context));
        if (1 - value * value < 0.7f)
            return 0.7f;
        return 1 - value * value;
    }

    public void performNextPage() {
        if (nextItem != null)
            changePageTo(NEXT_PAGE);
    }

    public void performPreviousPage() {
        if (previousItem != null)
            changePageTo(PREVIOUS_PAGE);
    }

    public View getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(View currentItem) {
        this.currentItem = currentItem;
    }

    public View getNextItem() {
        return nextItem;
    }

    public void setNextItem(View nextItem) {
        this.nextItem = nextItem;
    }

    public View getPreviousItem() {
        return previousItem;
    }

    public void setPreviousItem(View previousItem) {
        this.previousItem = previousItem;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void cleanPager() {
        rootView.removeAllViews();
        rootView.clearAnimation();
        currentItem = null;
        nextItem = null;
        previousItem = null;
        removedItem = null;
        currentPosition = 0;
        viewList = null;
    }
}
