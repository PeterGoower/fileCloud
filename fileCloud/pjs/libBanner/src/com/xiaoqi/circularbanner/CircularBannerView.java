package com.xiaoqi.circularbanner;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class CircularBannerView extends RelativeLayout {
	private Context context;
	private String[] imageUrl = null;
	private String[] imageDesc = null;
	private MyAdapter adapter;
	private TextView image_desc;
	private LinearLayout pointGroup;
	protected int lastPosition;
	private OnItemClickListener listener;
	private LinearLayout ll_bottom;
	private int circle_res = R.drawable.point_bg;
	private ViewPager vp;

	public CircularBannerView(Context context) {
		super(context);
		this.context = context;
		initImageLoader();
		initView(context);
	}

	public CircularBannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initImageLoader();
		initView(context);
	}

	public CircularBannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initImageLoader();
		initView(context);
	}

	/**
	 * 设置图片路径
	 * 
	 * @param imageUrl
	 */
	public void setImageUrl(String[] imageUrl) {
		if (imageUrl != null && imageUrl.length > 0) {
			this.imageUrl = imageUrl;
			setPoints(imageUrl.length);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 设置每张图片点击事件
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置图片描述性文字
	 * 
	 * @param imageDesc
	 */
	public void setImageDesc(String[] imageDesc) {
		if (imageDesc != null && imageDesc.length > 0) {
			image_desc.setVisibility(View.VISIBLE);
			this.imageDesc = imageDesc;
			adapter.notifyDataSetChanged();
			image_desc.setText(imageDesc[0]);
		}
	}

	/**
	 * 设置描述性文字颜色
	 * 
	 * @param color
	 */
	public void setDescTextColor(int color) {
		if (image_desc.getVisibility() == View.VISIBLE) {
			image_desc.setTextColor(color);
		}
	}

	/**
	 * 设置底部背景颜色
	 * 
	 * @param color
	 */
	public void setBottomBkColor(int color) {
		ll_bottom.setBackgroundColor(color);
	}

	/**
	 * 设置圆点指示器的selector
	 * 
	 * @param color
	 */
	public void setCircleSelector(int res) {
		circle_res = res;
	}
/**
 * 设置图片属性
 * @param imageurl 图片路径
 * @param imagedesc 图片描述
 * @param listener 图片点击回调
 */
	public void setImageResouce(String imageurl[],String imagedesc[],OnItemClickListener listener){
		setImageUrl(imageurl);
		setImageDesc(imagedesc);
		setOnItemClickListener(listener);
	}
	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.circularbanner, null);
		vp = (ViewPager) view.findViewById(R.id.viewpager);
		image_desc = (TextView) view.findViewById(R.id.image_desc);
		pointGroup = (LinearLayout) view.findViewById(R.id.point_group);
		ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
		imageUrl = new String[] {};
		imageDesc = new String[] {};
		adapter = new MyAdapter();
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (imageDesc.length > 0) {
					image_desc.setText(imageDesc[position]);
				}
				pointGroup.getChildAt(position).setEnabled(true);
				// 把上一个点设为false
				pointGroup.getChildAt(lastPosition).setEnabled(false);
				lastPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		addView(view);
	}

	private class MyAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageUrl.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			// 给 container 添加一个view
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			ImageLoader.getInstance().displayImage(imageUrl[position],
					imageView);
			container.addView(imageView);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(listener!=null)
					listener.onclick(position);
				}
			});
			return imageView;
		}

		@Override
		/**
		 * 销毁对应位置上的object
		 */
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			object = null;
		}
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)// Remove
				// app
				.build();
		ImageLoader.getInstance().init(config);
	}

	private void setPoints(int count) {
		for (int i = 0; i < count; i++) {
			// 添加指示点
			ImageView point = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = 5;
			params.leftMargin = 5;
			point.setLayoutParams(params);
			point.setBackgroundResource(circle_res);
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
			pointGroup.addView(point);
		}
	}

	public interface OnItemClickListener {
		void onclick(int position);
	}
}
