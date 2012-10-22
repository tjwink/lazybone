package com.jlusoft.lazybone.inject;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.jlusoft.lazybone.activity.LazyFragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.lang.Object;
/**
 * 负责Annotation解析和进行注射
 * @author tingjin
 *
 */

public class LazyInjector {
	
	private Class<?> mClass;
	private Context mInstance;
	
	public static void inject(Context activity){
		LazyInjector injector = new LazyInjector(activity);
		injector.inject();
	}
	
	public LazyInjector(Context activity){
		try {
			mClass = Class.forName(activity.getClass().getName());
			mInstance = activity;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void process(){
		Field[] allFields = mClass.getDeclaredFields();
		for (Field field: allFields){
//			System.out.println(field);
			Annotation[] annotations = field.getAnnotations(); 
			for(Annotation annotation : annotations){
				InjectField(annotation,field);
			}
		}
	}

	
	private void InjectField(Annotation annotation,Field field) {
		
		field.setAccessible(true);
		if (annotation instanceof InjectView){
			InjectView injectview = (InjectView) annotation;
			
			if(mInstance instanceof Activity){
				int viewid = injectview.id();
				//找到View,并赋值.
				final View targetView = ((Activity) mInstance).findViewById(viewid);
				try {
					field.set(mInstance, targetView);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return;
				}
				
				//设置点击方法。
				final String click = ((InjectView) annotation).click();
				if (click.length()!=0){
					try {
						final Method onclick = mClass.getDeclaredMethod(click, View.class);
						targetView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								try {
									onclick.invoke(mInstance, v);
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						});
						
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					
				}
				
				//设置item点击
				if (targetView instanceof AbsListView){
					final String itemclick = ((InjectView) annotation).itemclick();
					if (itemclick.length()!=0){
						try {
							final Method onitemclick = mClass.getDeclaredMethod(itemclick, AdapterView.class,View.class,int.class,long.class);
							((AbsListView) targetView).setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									try {
										onitemclick.invoke(mInstance, arg0,arg1,arg2,arg3);
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									} catch (IllegalAccessException e) {
										e.printStackTrace();
									} catch (InvocationTargetException e) {
										e.printStackTrace();
									}
								}
								
							});
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
				}
				
				
				//设置long click
				final String longclick = ((InjectView) annotation).longclick();
				if (longclick.length()!=0){
					try {
						final Method onlongclick = mClass.getDeclaredMethod(longclick, View.class);
						targetView.setOnLongClickListener(new OnLongClickListener() {
							
							@Override
							public boolean onLongClick(View v) {
								try {
									return (Boolean) onlongclick.invoke(mInstance, v);
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								return false;
							}
						});
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
				
				
			}
		}else if (annotation instanceof InjectResource){
			//注射资源
			
			//注射Drawable
			if(field.getType().equals(Drawable.class)){
				Drawable target = mInstance.getResources().getDrawable(((InjectResource) annotation).id());
				try {
					field.set(mInstance, target);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else if(field.getType().equals(String.class)){
				//注册String
				String target = mInstance.getResources().getString(((InjectResource) annotation).id());
				try {
					field.set(mInstance, target);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}else if(annotation instanceof InjectManager){
			
				try {
					String servicename = ((InjectManager) annotation).value();
					if(servicename.equals(Context.WINDOW_SERVICE)){
						field.set(mInstance, mInstance.getSystemService(Context.WINDOW_SERVICE));
					}else if((servicename.equals(Context.LAYOUT_INFLATER_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
					}else if((servicename.equals(Context.ACTIVITY_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.POWER_SERVICE));
					}else if((servicename.equals(Context.ACTIVITY_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.POWER_SERVICE));
					}else if((servicename.equals(Context.ALARM_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.ALARM_SERVICE));
					}else if((servicename.equals(Context.NOTIFICATION_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.NOTIFICATION_SERVICE));
					}else if((servicename.equals(Context.KEYGUARD_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.KEYGUARD_SERVICE));
					}else if((servicename.equals(Context.LOCATION_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.LOCATION_SERVICE));
					}else if((servicename.equals(Context.SEARCH_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.SEARCH_SERVICE));
					}else if((servicename.equals(Context.VIBRATOR_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.VIBRATOR_SERVICE));
					}else if((servicename.equals(Context.CONNECTIVITY_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.CONNECTIVITY_SERVICE));
					}else if((servicename.equals(Context.WIFI_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.WIFI_SERVICE));
					}else if((servicename.equals(Context.INPUT_METHOD_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.INPUT_METHOD_SERVICE));
					}else if((servicename.equals(Context.UI_MODE_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.UI_MODE_SERVICE));
					}else if((servicename.equals(Context.DOWNLOAD_SERVICE))){
						field.set(mInstance, mInstance.getSystemService(Context.DOWNLOAD_SERVICE));
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else if (annotation instanceof InjectFragment){
				//暂时不支持fragment
//				if (mInstance instanceof LazyFragmentActivity){
//					try {
//						FragmentManager fm = ((FragmentActivity)mInstance).getFragmentManager();
////						field.set(mInstance, );
//					} catch (IllegalArgumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			}else if (annotation instanceof InjectExtra) {
				if (mInstance instanceof Activity){
				//注入Intent携带的数据
					Intent intent = ((Activity)mInstance).getIntent();
					if (intent!=null){
						Bundle extra =intent.getExtras();
							if (extra!=null){
							Object target = extra.get(((InjectExtra) annotation).value());
								if(target == null&&((InjectExtra) annotation).optional()){
									return;
								}
								try {
									field.set(mInstance, target);
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
					}
				}
					
			}
		
	}
	
	public void inject(){
		process();
	}
}
