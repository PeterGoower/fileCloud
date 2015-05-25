package com.guu.file.views;

import com.guu.file.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
	private OnBannerClickListener listener;
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
	 * ����ͼƬ·��
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
	 * ����ÿ��ͼƬ����¼�
	 * 
	 * @param listener
	 */
	public void OnBannerClickListener(OnBannerClickListener listener) {
		this.listener = listener;
	}

	/**
	 * ����ͼƬ����������
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
	 * ����������������ɫ
	 * 
	 * @param color
	 */
	public void setDescTextColor(int color) {
		if (image_desc.getVisibility() == View.VISIBLE) {
			image_desc.setTextColor(color);
		}
	}

	/**
	 * ���õײ�������ɫ
	 * 
	 * @param color
	 */
	public void setBottomBkColor(int color) {
		ll_bottom.setBackgroundColor(color);
	}

	/**
	 * ����Բ��ָʾ����selector
	 * 
	 * @param color
	 */
	public void setCircleSelector(int res) {
		circle_res = res;
	}
/**
 * ����ͼƬ����
 * @param imageurl ͼƬ·��
 * @param imagedesc ͼƬ����
 * @param listener ͼƬ����ص�
 */
	public void setImageResouce(String imageurl[],String imagedesc[],OnBannerClickListener listener){
		setImageUrl(imageurl);
		setImageDesc(imagedesc);
		OnBannerClickListener(listener);
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
				// ����һ������Ϊfalse
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
			// �� container ���һ��view
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			ImageLoader il = ImageLoader.getInstance();
//			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
//	                .memoryCache(new LruMemoryCache(200 * 1024 * 104))  
//	                .memoryCacheSize(200 * 1024 * 1024)  
//	                .memoryCacheSizePercentage(13).build();
//			il.init(configuration);  
			 
			DisplayImageOptions options;  
			 options = new DisplayImageOptions.Builder()
			 .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
			 .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中   
			 .build();
			
			
			il.displayImage(imageUrl[position],
					imageView, options);
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
		 * ���ٶ�Ӧλ���ϵ�object
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
			// ���ָʾ��
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

	public interface OnBannerClickListener {
		void onclick(int position);
	}
}
