package com.ml.module_shouhuan.widget;

import java.util.ArrayList;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ml.module_shouhuan.R;

public class CustomCalendarView extends RelativeLayout{
	private Context mContext;
	private Resources mResources;
	private Calendar mCalendar;
	private Calendar mPreCalendar;
	private Calendar mExpandedCalendar;
	
	private boolean mExpandMode = false;
	
	private LinearLayout mMiddleLayout;
	private ArrayList<TextView> mDayList = new ArrayList<TextView>();
	private ArrayList<Integer> mDayNumList = new ArrayList<Integer>();
	private ImageView mArrowsView;
	private TextView mPreTextView;
	private TextView mNextTextView;
	
	private final String[] WeekString = {"日","一","二","三","四","五","六"};
	private LinearLayout.LayoutParams mMiddleLayoutParms;
	private LinearLayout.LayoutParams mDayTextParms;
	
	private int mDayOfMonth;
	private int mDayOfWeek;
	private int mMonth;
	private int mDayCount;
	private int mPreDayCount;
	private int mSelectedTextIndex;
	
	private final int MiddleLayoutId = 1000;
	private final int ButtomArrowsId = 1001;
	private final int PreMonthId = 1002;
	private final int NextMonthId = 1003;
	private int mWeekTextSize;
	private int mDayTextSize;
	
	private UpdateDateListener mUpdateDataListener;
	
	public interface UpdateDateListener{
		public void update(Calendar calendar, boolean needDate);
	}
	
	public CustomCalendarView(Context context) {
		this(context, null);
	}
	
	public CustomCalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mResources = context.getResources();
		mMiddleLayoutParms = new LinearLayout.LayoutParams(mResources.getDimensionPixelSize(R.dimen.margin_25x), LayoutParams.WRAP_CONTENT);
		mMiddleLayoutParms.gravity = CENTER_HORIZONTAL;
		mDayTextParms = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		int margin = mResources.getDimensionPixelSize(R.dimen.margin_5d);
		mDayTextParms.setMargins(0, margin, 0, margin);
		mWeekTextSize = mResources.getDimensionPixelSize(R.dimen.txt_size_13);
		mDayTextSize = mResources.getDimensionPixelSize(R.dimen.txt_size_14);
		
		mMiddleLayout = new LinearLayout(mContext);
		mMiddleLayout.setId(MiddleLayoutId);
		mMiddleLayout.setLayoutParams(mMiddleLayoutParms);
		mMiddleLayout.setOrientation(LinearLayout.VERTICAL);
		
		initCalendarData();
		initLeftRightView();
		getSingleDayLayout();
		
		mArrowsView = new ImageView(mContext);
		mArrowsView.setId(ButtomArrowsId);
		mArrowsView.setImageResource(R.drawable.icon_arrows_down);
		addView(mMiddleLayout);
		addView(mArrowsView);
		
		resetPosition();
	}

	/**
	 * 设置中间日期及箭头位置
	 * @param 
	 * @return
	 */
	private void resetPosition() {
		LayoutParams mMiddleRule = (LayoutParams) mMiddleLayout.getLayoutParams();
		mMiddleRule.addRule(RelativeLayout.CENTER_IN_PARENT);
		mMiddleLayout.setLayoutParams(mMiddleRule);
		LayoutParams mButtonRule = (LayoutParams) mArrowsView.getLayoutParams();
		mButtonRule.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mButtonRule.addRule(RelativeLayout.BELOW, MiddleLayoutId);
		mArrowsView.setLayoutParams(mButtonRule);
		mArrowsView.setOnClickListener(mClickListener);
	}

	/**
	 * 初始化日期
	 * @param 
	 * @return
	 */
	private void initCalendarData() {
		mCalendar = Calendar.getInstance();
		mCalendar.add(Calendar.DAY_OF_MONTH, -1);
		mDayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
		mDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDayCount = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		mPreCalendar = Calendar.getInstance();
		mExpandedCalendar = Calendar.getInstance();
		mExpandedCalendar.add(Calendar.DAY_OF_MONTH, -1);
		mPreCalendar.add(Calendar.MONTH, -1);
		mPreDayCount = mPreCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取中间的内容
	 * @param i每行的所在位置，isWeek是日期还是星期
	 * @return
	 */
	private TextView getText(int i , boolean isWeek){
		TextView mTextView = new TextView(mContext);
		mTextView.setLayoutParams(mDayTextParms);
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setTextColor(getTextColor(i));
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,isWeek ? mWeekTextSize : mDayTextSize);
		if(!isWeek && mDayClickListener != null)
			mTextView.setOnClickListener(mDayClickListener);
		return mTextView;
	}
	
	private int getTextColor(int i) {
		return mResources.getColor(i == 0 || i == 6 ? R.color.calendar_gray : R.color.txt_black_tab);
	}
	
	//获取单行月历内容
	private int getDayTextNum(int i){
		int dayNum = mDayOfMonth - mDayOfWeek + 1 + i;
		if(dayNum > 0 && dayNum <= mDayCount)
			return dayNum;
		if(dayNum < 1){
			return mPreDayCount - mDayOfWeek + 2 + i;
		}else {
			return dayNum - mDayCount;
		}
	}
	
	//获取星期行
	private void getWeekLayout(){
		LinearLayout mWeekLayout = new LinearLayout(mContext);
		mWeekLayout.setLayoutParams(mMiddleLayoutParms);
		TextView mTempWeekText;
		for(int i = 0 ; i < 7 ; i++){
			mTempWeekText = getText(i, true);
			mTempWeekText.setText(WeekString[i]);
			mWeekLayout.addView(mTempWeekText);
		}
		mMiddleLayout.addView(mWeekLayout);
	}
	
	//获取单行Layout
	private void getSingleDayLayout(){
		mExpandMode = false;
		mMiddleLayout.removeAllViews();
		mDayList.removeAll(mDayList);
		mDayNumList.removeAll(mDayNumList);
		removeView(mPreTextView);
		removeView(mNextTextView);
		mSelectedTextIndex = mDayOfMonth;
		getWeekLayout();
		LinearLayout mCurrentDayLayout = new LinearLayout(mContext);
		int index;
		TextView tempTextView;
		for(int i = 0;i < 7; i++){
			index = getDayTextNum(i);
			tempTextView = getText(i, false);
			tempTextView.setText(String.valueOf(index));
			if(index == mSelectedTextIndex)
				tempTextView.setBackgroundResource(R.drawable.icon_day_select);
			mCurrentDayLayout.addView(tempTextView);
			mDayList.add(tempTextView);
			mDayNumList.add(index);
		}
		mMiddleLayout.addView(mCurrentDayLayout);
	}
	
	//获取整页日历Layout
	private void getExpandDayLayout(Calendar calendar){
		mExpandMode = true;
		mArrowsView.setImageResource(R.drawable.icon_arrows_up);
		mMiddleLayout.removeAllViews();
		mDayList.removeAll(mDayList);
		mDayNumList.removeAll(mDayNumList);
		getWeekLayout();
		int dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int beforeNums = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		calendar.set(Calendar.DAY_OF_MONTH, dayCount);
		int afterNums = 7 - calendar.get(Calendar.DAY_OF_WEEK);
		int rowNum = (beforeNums + dayCount + afterNums)/7;
		LinearLayout dayLayout;
		int index;
		TextView tempTextView;
		for(int i = 0 ; i < rowNum ; i ++){
			dayLayout = new LinearLayout(mContext);
			for(int j = 0 ; j < 7 ; j++){
				tempTextView = getText(j, false);
				index = i*7 + j - beforeNums + 1;
				if(index > 0 && index <= dayCount){
					tempTextView.setText(String.valueOf(index));
					mDayList.add(tempTextView);
					mDayNumList.add(index);
					if(index == mSelectedTextIndex)
						tempTextView.setBackgroundResource(R.drawable.icon_day_select);
				}
				dayLayout.addView(tempTextView);
			}
			mMiddleLayout.addView(dayLayout);
		}
		if(mSelectedTextIndex > mDayList.size())
			mDayList.get(mDayList.size() - 1).setBackgroundResource(R.drawable.icon_day_select);
	}

	//初始化上月下月按钮
	private void initLeftRightView() {
		mPreTextView = new TextView(mContext);
		LayoutParams mPreLayoutParms = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPreLayoutParms.addRule(RelativeLayout.CENTER_VERTICAL);
		mPreTextView.setLayoutParams(mPreLayoutParms);
		mPreTextView.setId(PreMonthId);
		mPreTextView.setGravity(Gravity.CENTER);
		mPreTextView.setBackgroundResource(R.drawable.icon_month_pre);
		mPreTextView.setTextColor(mResources.getColor(R.color.txt_black_tab));
		mPreTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDayTextSize);
		mPreTextView.setOnClickListener(mClickListener);
		mNextTextView = new TextView(mContext);
		LayoutParams mNextLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mNextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mNextLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		mNextTextView.setLayoutParams(mNextLayoutParams);
		mNextTextView.setId(NextMonthId);
		mNextTextView.setGravity(Gravity.CENTER);
		mNextTextView.setBackgroundResource(R.drawable.icon_month_next);
		mNextTextView.setTextColor(mResources.getColor(R.color.txt_black_tab));
		mNextTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDayTextSize);
		mNextTextView.setOnClickListener(mClickListener);
	}
	
	//更新上下月的内容
	private void updateMonth(boolean isAdd){
		if(isAdd){
			mExpandedCalendar.add(Calendar.MONTH, 1);
		}else {
			mExpandedCalendar.add(Calendar.MONTH, -1);
		}
		int month = mExpandedCalendar.get(Calendar.MONTH);
		mPreTextView.setText(String.format(mResources.getString(R.string.calendar_month), month != 0 ? month : 12));
		mNextTextView.setText(String.format(mResources.getString(R.string.calendar_month), (month + 2) != 13 ? (month + 2) : 1));
	}
	
	//获取选中的日期
	private Calendar getSelectCalendar(){
		Calendar mSelectCalendar = Calendar.getInstance();
		int dayCount = mExpandedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		mSelectCalendar.set(Calendar.YEAR, mExpandedCalendar.get(Calendar.YEAR));
		mSelectCalendar.set(Calendar.MONTH, mExpandedCalendar.get(Calendar.MONTH));
		if(mSelectedTextIndex > dayCount)
			mSelectedTextIndex = dayCount;
		mSelectCalendar.set(Calendar.DAY_OF_MONTH, mSelectedTextIndex);
		if(!mExpandMode && mDayOfMonth < 7 && mSelectedTextIndex > dayCount -7){
			mSelectCalendar.add(Calendar.MONTH, -1);
			mSelectCalendar.set(Calendar.DAY_OF_MONTH, mSelectedTextIndex);
		}
		return mSelectCalendar;
	}
	
	public void setUpdateDateListener(UpdateDateListener updateDateListener){
		mUpdateDataListener = updateDateListener;
	}
	
	//设置日期的点击事件
	private OnClickListener mDayClickListener = new OnClickListener() {
		@SuppressLint("NewApi") @Override
		public void onClick(View view) {
			TextView dayText = (TextView)view;
			String textString = dayText.getText().toString();
			if(TextUtils.isEmpty(textString))return;
			dayText.setBackgroundResource(R.drawable.icon_day_select);
			int currentIndex = Integer.valueOf(textString);
			if(mSelectedTextIndex != currentIndex){
				if(mDayNumList.indexOf(mSelectedTextIndex) != -1)
					mDayList.get(mDayNumList.indexOf(mSelectedTextIndex)).setBackgroundDrawable(null);
				mSelectedTextIndex = currentIndex;
			}
			if(mUpdateDataListener != null)
				mUpdateDataListener.update(getSelectCalendar(),true);
		}
	};
	
	//设置箭头及上下月的监听
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case ButtomArrowsId:
				if(!mExpandMode){
					mSelectedTextIndex = mDayOfMonth;
					mExpandedCalendar = Calendar.getInstance();
					mExpandedCalendar.add(Calendar.DAY_OF_MONTH, -1);
					getExpandDayLayout(mExpandedCalendar);
					mPreTextView.setText(String.format(mResources.getString(R.string.calendar_month), mMonth));
					mNextTextView.setText(String.format(mResources.getString(R.string.calendar_month), mMonth + 2));
					CustomCalendarView.this.addView(mPreTextView);
					CustomCalendarView.this.addView(mNextTextView);
				}else {
					getSingleDayLayout();
					mArrowsView.setImageResource(R.drawable.icon_arrows_down);
				}
				if(mUpdateDataListener != null)
					mUpdateDataListener.update(mCalendar,false);
				break;
			case PreMonthId:
				updateMonth(false);
				getExpandDayLayout(mExpandedCalendar);
				if(mUpdateDataListener != null)
					mUpdateDataListener.update(getSelectCalendar(),false);
				break;
			case NextMonthId:
				updateMonth(true);
				getExpandDayLayout(mExpandedCalendar);
				if(mUpdateDataListener != null)
					mUpdateDataListener.update(getSelectCalendar(),false);
				break;
			default:
				break;
			}
		}
	};
}
