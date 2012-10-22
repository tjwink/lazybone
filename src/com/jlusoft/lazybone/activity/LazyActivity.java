package com.jlusoft.lazybone.activity;

import com.jlusoft.lazybone.inject.LazyInjector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class LazyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		LazyInjector.inject(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		LazyInjector.inject(this);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		LazyInjector.inject(this);
	}

}
