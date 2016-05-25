package com.example.asus.bluetooth.popupwindow;


import com.example.asus.bluetooth.wheelview.OnWheelScrollListener;
import com.example.asus.bluetooth.wheelview.WheelView;
import com.example.asus.bluetooth.wheelview.adapter.NumericWheelAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.example.asus.bluetooth.MainActivity;
import com.example.asus.bluetooth.R;

/**
 * 通道选项
 */

public class AccessPopWindow extends PopupWindow{
	private Context context;
	private LayoutInflater mInflater;
	private View dateView;
	public WheelView accessView;
	public AccessPopWindow(Context context){
		this.context=context;
		
		initWindow();
	}
	private void initWindow() {
		// TODO Auto-generated method stub
		mInflater=LayoutInflater.from(context);
		dateView=mInflater.inflate(R.layout.wheel_access_picker, null);
		accessView=(WheelView) dateView.findViewById(R.id.access_num);
		initWheel();
	}
	private void initWheel() {
		// TODO Auto-generated method stub
		
		NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(context,1,4);
		numericWheelAdapter1.setLabel("通道");
		accessView.setViewAdapter(numericWheelAdapter1);
		accessView.setCyclic(true);
		accessView.addScrollingListener(scrollListener);
		
		
		accessView.setVisibleItems(7);
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
			int n_access = accessView.getCurrentItem();
			Log.e("n_access",n_access+"");
			if (MainActivity.message[3] != 0){
				MainActivity.message[4] = (byte)(n_access + 1);
				Log.e("message[4]",(byte) (n_access + 1)+"");
			}else {
				Toast.makeText(context,"请选择星期",Toast.LENGTH_SHORT).show();
			}
		}
	};
}
