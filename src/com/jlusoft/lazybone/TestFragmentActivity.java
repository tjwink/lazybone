package com.jlusoft.lazybone;

import android.os.Bundle;
import android.widget.TextView;

import com.jlusoft.lazybone.activity.LazyFragmentActivity;
import com.jlusoft.lazybone.inject.InjectExtra;
import com.jlusoft.lazybone.inject.InjectView;

public class TestFragmentActivity extends LazyFragmentActivity{

	@InjectView(id=R.id.textview)
	TextView textview;
	@InjectExtra(value = "test2", optional = true)
	int test=2;
	@InjectExtra(value = "text1",optional =true)
	String text = "tingjin";
	@InjectExtra("book")
	Book book;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_test);
		textview.setText("ceshi"+test+text+book.getAuthor());
	}

}
