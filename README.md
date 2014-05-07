CustomViewPager
===============

This is a Custom ViewPager that provides custom pager animation.
Current native pager does not provide various animations while paging event. So i have made my own pager without using the native pager.

Link: http://youtu.be/SuPari76_wM

Implementation
===============

Create an adapter extend from CustomPagerAdapter: 
Implement just like any other adapters.

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
			return count;
		}
		
	}
	
Then create an instance of CustomPagerBase and add expected parameters like below;

	// This is the view that pager will be in it
	RelativeLayout cardsLayout = (RelativeLayout)findViewById(R.id.cardsLayout);
	CustomAdapter adapter = new CustomAdapter(this);
	pager = new CustomPagerBase(this, cardsLayout, adapter);
	// This method prepares the pager. The parameter 2 is the initial page when the pager shows.
	pager.preparePager(2);
	
	
Tips
===============

I created the pager just for a xhdpi device. In dimens folder you can resize the pager for other resolutions. 


Last Words
===============

In this project a third party library called "nineoldandroids" is used to make smooth animations.

You can also see the video to know how the pager works.
Link: http://youtu.be/SuPari76_wM




		