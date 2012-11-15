package com.jlusoft.lazybone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.jlusoft.lazybone.orm.LazyDbOpenHelper;

public class TestDAO extends LazyDbOpenHelper<TestDto>{

	public TestDAO(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public TestDAO(Context context){
		super(context, "test.db", null, 1);
	}
}
