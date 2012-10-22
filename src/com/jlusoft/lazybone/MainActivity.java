package com.jlusoft.lazybone;

import com.jlusoft.lazybone.activity.LazyActivity;
import com.jlusoft.lazybone.inject.InjectExtra;
import com.jlusoft.lazybone.inject.InjectManager;
import com.jlusoft.lazybone.inject.InjectResource;
import com.jlusoft.lazybone.inject.InjectView;
import com.jlusoft.lazybone.inject.LazyInjector;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MainActivity extends LazyActivity implements OnItemClickListener{

	@InjectView(id = R.id.textview, click ="btnClick", longclick="btnLongClick")
	TextView mTextView;
	@InjectView(id=R.id.listview, itemclick="onItemClick")
	ListView mListView;
	@InjectView(id=R.id.imageview)
	ImageView mImageView;
	@InjectResource(id=R.drawable.ic_launcher)
	Drawable icon;
	@InjectResource(id = R.string.hello_world)
	String text;
	@InjectManager(Context.WINDOW_SERVICE)
	WindowManager mWM;
	@InjectExtra(value = "none")
	int num = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView.setText(text+num);
        mListView.setAdapter(new adapter());
        mImageView.setImageDrawable(icon);
        mWM.toString();
        Intent intent = new Intent(this, TestFragmentActivity.class);
        intent.putExtra("test", num);
        intent.putExtra("text", "测试");
        Book book = new Book();
        book.setAuthor("张志远");
        intent.putExtra("book", book);
        startActivity(intent);
    }


    public void btnClick(View v){
    	mTextView.setText("点击方法");
    	
    }
    
    public boolean btnLongClick(View v){
    	mTextView.setText("长点击");
    	return true;
    }
    
    private class adapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textview = null;
			if (convertView==null){
				textview = new TextView(MainActivity.this);
			}else{
				textview = (TextView) convertView;
			}
			textview.setText(""+position);
			return textview;
		}
    	
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		mTextView.setText("点击"+arg2);
		
	}
}
