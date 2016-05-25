package com.example.asus.bluetooth.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.asus.bluetooth.MainActivity;
import com.example.asus.bluetooth.R;

import com.example.asus.bluetooth.wheelview.OnWheelScrollListener;
import com.example.asus.bluetooth.wheelview.WheelView;
import com.example.asus.bluetooth.wheelview.adapter.NumericWheelAdapter;

public class EndTimePopWindow extends PopupWindow{
	private Context context;
	private LayoutInflater mInflater;
	private View dateView;
	private WheelView hourView;
	private WheelView minView;
	private int n_hour;
	private int n_min;
	public EndTimePopWindow(Context context){
		this.context=context;
		initWindow();
	}
	private void initWindow() {
		mInflater=LayoutInflater.from(context);
		dateView=mInflater.inflate(R.layout.wheel_date_picker, null);
		hourView=(WheelView) dateView.findViewById(R.id.year);
		minView=(WheelView) dateView.findViewById(R.id.month);
		initWheel();
	}
	private void initWheel() {
		n_hour = 0;
		n_min = 0;
		NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(context,0, 23);
		numericWheelAdapter1.setLabel("点");
		hourView.setViewAdapter(numericWheelAdapter1);
		hourView.setCyclic(true);
		hourView.addScrollingListener(scrollListener);
		
		NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(context,0, 59);
		numericWheelAdapter2.setLabel("分");
		minView.setViewAdapter(numericWheelAdapter2);
		minView.setCyclic(true);
		minView.addScrollingListener(scrollListener);

		hourView.setVisibleItems(7);//设置时分显示的条目
		minView.setVisibleItems(7);
		setContentView(dateView);
		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
		setBackgroundDrawable(dw);
		setFocusable(true);
	}
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			n_hour = hourView.getCurrentItem();
			n_min = minView.getCurrentItem();
			if (MainActivity.message[4] != 0){
				switch (MainActivity.message[4]){
					case 1:
						MainActivity.message[7] = (byte)n_hour;
						MainActivity.message[8] = (byte)n_min;
						break;
					case 2:
						MainActivity.message[11] = (byte)n_hour;
						MainActivity.message[12] = (byte)n_min;
						break;
					case 3:
						MainActivity.message[15] = (byte)n_hour;
						MainActivity.message[16] = (byte)n_min;
						break;
					case 4:
						MainActivity.message[19] = (byte)n_hour;
						MainActivity.message[20] = (byte)n_min;
						break;
				}
                Log.e("message_end_hour",(byte) (n_hour)+"");
                Log.e("message_end_min",(byte) (n_min)+"");
			}else {
				Toast.makeText(context,"请先选择通道",Toast.LENGTH_SHORT).show();
			}

		}
	};
}
