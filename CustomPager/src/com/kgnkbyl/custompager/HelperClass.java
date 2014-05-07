package com.kgnkbyl.custompager;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class HelperClass {
	
	private static Point screenSize;
	
	public static int getWindowWidth(Context context){
		if(screenSize == null){
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			screenSize = new Point();
			display.getSize(screenSize);
		}
		if(screenSize != null)
			return screenSize.x;
		else 
			return 0;
	}
	public static int getWindowHeight(Context context){
		if(screenSize == null){
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			screenSize = new Point();
			display.getSize(screenSize);
		}
		if(screenSize != null)
			return screenSize.y;
		else 
			return 0;
	}
	
	public static int getCurrentCardPositionX(Context context){
		float cardWidth = context.getResources().getDimension(R.dimen.card_image_width);
		return (int) (getWindowWidth(context)/2 - cardWidth/2);
	}

}
