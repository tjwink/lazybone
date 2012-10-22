package com.jlusoft.lazybone.activity;

import com.jlusoft.lazybone.inject.LazyInjector;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class LazyFragmentActivity extends FragmentActivity {

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		LazyInjector.inject(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
		LazyInjector.inject(this);
	}

	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
		LazyInjector.inject(this);
	}

}
