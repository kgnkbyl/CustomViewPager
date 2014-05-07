package com.kgnkbyl.custompager;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends ActionBarActivity {

    private CustomPagerBase pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		RelativeLayout cardsLayout = (RelativeLayout)findViewById(R.id.cardsLayout);
		CustomAdapter adapter = new CustomAdapter(this);
		pager = new CustomPagerBase(this, cardsLayout, adapter);
		pager.preparePager(2);
		
		Button next = (Button)findViewById(R.id.next);
		Button prev = (Button)findViewById(R.id.previous);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pager.performNextPage();
			}
		});
		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pager.performPreviousPage();
			}
		});
	}
	
	public class CustomAdapter extends CustomPagerAdapter{

		public CustomAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView) {
			View tempView = convertView;
			if(tempView == null){
				LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					tempView = inflater.inflate(R.layout.single_card_fragment, null, false);
			}
			ImageView cardImage = (ImageView)tempView.findViewById(R.id.cardImage);
            cardImage.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.card_image));
			return tempView;
		}

		@Override
		public int dataCount() {
			return 5;
		}
		
	}
}
