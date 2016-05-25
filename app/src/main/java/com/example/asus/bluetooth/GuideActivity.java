package com.example.asus.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

	private Button button;
	private ViewPager view_pager;

	/**
	 * 导航页面
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏窗体标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//加载布局
		setContentView(R.layout.guide);
		//获取关心的控件
		button = (Button) findViewById(R.id.button);
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		
		//获取view集合
		List<View> lists = new ArrayList<View>();
		//创建imagview对像
		
		ImageView img = new ImageView(this);
		img.setBackgroundResource(R.drawable.loginbg);
		ImageView img2 = new ImageView(this);
		img2.setBackgroundResource(R.drawable.loginbg);
		ImageView img3 = new ImageView(this);
		img3.setBackgroundResource(R.drawable.loginbg);
		//添加到集合中
		lists.add(img);
		lists.add(img2);
		lists.add(img3);
		//创建adapter对象
		ViewPagerAdapter adapter = new ViewPagerAdapter(lists);
		//给viewpager设置匹配器
		view_pager.setAdapter(adapter);
		//设置button的点击事件
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//打开主页面
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				//关闭当前页面
				finish();
			}
		});
		//设置view_pager的点击事件
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * 被选中
			 * @param arg0
			 */
			@Override
			public void onPageSelected(int arg0) {
				// 如果是最后一个引导界面的话，就出现按钮
				//如果不是最后一个的话，就不出现
				if (arg0 == 2) {
					//就显示按钮
					button.setVisibility(View.VISIBLE);
				} else {
					//就隐藏按钮
					button.setVisibility(View.GONE);
				}
			}
			/**
			 * 在滑动
			 * @param arg0
			 * @param arg1
			 * @param arg2
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			/**
			 * 滑动状态改变时
			 * @param arg0
			 */
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	/**
	 * 
	 *viewpager的匹配器
	 */
	public class ViewPagerAdapter extends PagerAdapter {
		//创建页面集合
		private List<View> pages;
		//设置有参构造
		public ViewPagerAdapter(List<View> lists) {
			this.pages = lists;
		}
		/**
		 * 销毁条目的方法
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//销毁选中的条目
			((ViewPager)container).removeView(pages.get(position));
		}
		/**
		 * 加载条目的方法
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//添加条目
			((ViewPager)container).addView(pages.get(position));
			//返回获取到的条目
			return pages.get(position);
		}
		//获取页面数量
		@Override
		public int getCount() {
			return pages.size();
		}
		//判断是来自那个页面的数据
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

}