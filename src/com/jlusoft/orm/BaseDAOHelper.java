package com.jlusoft.orm;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public abstract class BaseDAOHelper<T extends DAOable> extends SQLiteOpenHelper implements DataBaseConstants,
		BaseColumns {

	/**
	 * 本类测试基本通过.使用前请认真阅读以下提示:
	 * 使用完本类之后记得使用close()方法关闭数据库,通常在Activity的onDestroy()方法中执行.
	 * 继承本类时必须把泛型T的类确定.
	 * 使用单例模式和工厂模式,以便关闭数据库.
	 * 本类根据T类的成员域自动生成相应的表,并提供基础的数据库操作方法 
	 * 因为并不是所有域都需要保存到数据库中,所以
	 * 要在保存的域的名字中加上"C_",如:
	 * private String C_Name;
	 * 只能保存基本的数据类型如int,long,float等等,对象中能保存String;
	 * 注意:Id域会自动作为主键生成数据库字段,无需添加"C_"后缀,但你需要为你的目标类添加id成员域.表中id的字段名为:"_id",你也可以继承DAOable类;
	 * 这个类已经自动添加id字段.
	 * DTO必须要有一个空参数的构造器
	 * 目前仅支持基本类型和String的转换,若出现其他类型,一律当无数据类型处理.
	 * boolean类型在数据库中用integer表示.0代表假,1代表真.
	 * 数据库中的字段名等于目标类除去后缀"C_",比如有一个成员域为private int C_points,那么该域在数据库中的字段名为points;
	 * @return 字符数组
	 */
	
	private String TAG  = this.getClass().getSimpleName();
	private final static String IDENTIFIER = "C_";
	private Class<T> targetClass;
	protected String TableName;
	protected String[] THIS_ALL_COLUMNS;
	protected HashMap<String, Field> columsMap;
	private Field[] fields;
	private boolean isCreated;
	private SQLiteDatabase db;
	@SuppressWarnings("unchecked")
	public BaseDAOHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// 获得泛型T的Class对象,用于反射获得T的成员变量
		targetClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		TableName = getTableName();
		fields = targetClass.getDeclaredFields();
		Field.setAccessible(fields, true);
		CreateTable(context, name, factory, version);
		columsMap = getAllColumnsMap(fields);
		THIS_ALL_COLUMNS = getAllColumnStrings(fields);
		isCreated = false;
		
	}

	/**因为每个dao对象都有自己维护的一张表，所以需要
	 * 干预数据库重新建表
	 * @param context
	 * @param name
	 * @param factory
	 * @param NewVersion
	 */
	protected synchronized void CreateTable(Context context, String name, CursorFactory factory, int NewVersion) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = null;
		if (name == null) {
			db = SQLiteDatabase.create(null);
		} else {
			db = context.openOrCreateDatabase(name, 0, factory);
		}
//		db.setVersion(NewVersion);
		this.db = db;
		
		int version = db.getVersion();

		db.beginTransaction();
		try {
			//如果还没建表,则进行建表
			//			if (version != 0) {
			if (!isCreated) {
				onCreate(db);
				Log.v("Create DB by SQL::", "自身干预数据库创建");
				Log.v("数据库版本:",""+version );
			}
			
			//升降级特殊处理
			if (version > NewVersion) {
//                onDowngrade(db, version, NewVersion);
            } else if (version < NewVersion){
                onUpgrade(db, version, NewVersion);
            }
			
			//			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			if (db != null)
				db.close();
			db = null;
		}

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS " + TableName + "(" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");
		ArrayList<String> columns = createSQLStringfromFields(fields);
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i));
			if (i != columns.size() - 1) {
				sql.append(",");
			} else {
				sql.append(");");
			}
		}
		Log.v("Create DB by SQL::", sql.toString());
		db.execSQL(sql.toString());
		
		isCreated = true;
	}
	/**
	 * 每个表都有自己的一个dao实例
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
//		db.execSQL("DROP TABLE IF EXISTS " + TableName);
//		onCreate(db);
		switch(oldVersion){
		case 1:
			//最初的版本，重新建表
			db.execSQL("DROP TABLE IF EXISTS SHOPGOODSDTOS");
			db.execSQL("DROP TABLE IF EXISTS SHOPDTOS");
		}
		onCreate(db);
	}

	public T save(T object) {
		
		if (object.get_Id() == -1) {
			SQLiteDatabase db = getWritableDatabase();
			return createNewObject(db, object);
		} else {
			String msg = "该对象已经拥有了id,证明其已经插入数据库,不能重复插入.";
			throw new RuntimeException(msg);
		}
	}

	/**
	 * 根据条件查找对象表
	 */

	public ArrayList<T> findObjectListByCondition(String where) {
		Cursor cursor = null;
		try {
			cursor = findObjectCursorByCondition(where);
			return findObjectListByCursor(cursor);
		} finally {
			if(cursor!=null){
			cursor.close();
			}
		}
	}
	
	/**Deprecated
	 * 主要用于排序和分页查找（排序或分页可为空）
	 * @param where
	 * @return cursor游标数据集
	 */
	public ArrayList<T> findObjectListByCondition(String orderBy,String limit) {
		Cursor cursor = null;
		try {
			cursor = findCursorByOrderByLimit(orderBy, limit);
			return findObjectListByCursor(cursor);
		} finally {
			if(cursor!=null){
			cursor.close();
			}
		}
	}

	/**Deprecated
	 * 根据SQL条件语句判断查找数据比如想查找id=1的数据,尽量使用findObjectListByCondition()
	 * 以后此方法会变为私有方法
	 * 则传入string="id=1",即刻
	 * @param where
	 * @return cursor游标数据集
	 */
	@Deprecated
	public Cursor findObjectCursorByCondition(String where) {
		SQLiteDatabase db = getReadableDatabase();

		return db.query(TableName, THIS_ALL_COLUMNS, where, null, null, null, null);

	}
	
	public boolean isDbLockedByCurrentThread(){
		if (db != null){
			return db.isDbLockedByCurrentThread();
		}
		return false;
	}
	
	public boolean isDbLockedByOtherThreads(){
		if (db != null){
			return db.isDbLockedByOtherThreads();
		}
		return false;
	}
	
	//分页参数，我就需要分页参数！
	public Cursor findCursorByOrderByLimit(String orderBy,String limit){
		SQLiteDatabase db = getReadableDatabase();
		
		return db.query(TableName, THIS_ALL_COLUMNS, null, null, null, null, orderBy, limit);
	}

	public T findObjectById(int id) {
		T object = null;
		Cursor cursor = null;

		try {

			cursor = findObjectCursorByCondition(_ID + " = " + id);
			if (cursor.getCount() == 1) {
				if (cursor.moveToFirst()) {
					object = createObjectFromCursorData(cursor);
				}
			}
			//			db.close();
		} finally {
			// TODO Auto-generated catch block
			closeCursor(cursor);

		}
		return object;
	}
	
	
	/**
	 * 根据条件查找对象
	 * @param where
	 * @return 无数据则返回null;
	 */
	public T findObjectByCondition(String where) {
		T object = null;
		Cursor cursor = null;

		try {

			cursor = findObjectCursorByCondition(where);
			if (cursor.getCount() == 1) {
				if (cursor.moveToFirst()) {
					object = createObjectFromCursorData(cursor);
				}
			}
			//			db.close();
		} finally {
			// TODO Auto-generated catch block
			closeCursor(cursor);

		}
		return object;
	}

	/**
	 * 从Cursor数据集转换成对象列表
	 * 
	 * @return 返回一个列表，如果找不到数据，就返回一个空的列表
	 */

	public ArrayList<T> findObjectListByCursor(Cursor cursor) {

		ArrayList<T> list = new ArrayList<T>();
		T objectT = null;
		try {

			while (cursor.moveToNext()) {
				objectT = createObjectFromCursorData(cursor);
				list.add(objectT);
			}
		} finally {
			// TODO: handle exception
			closeCursor(cursor);
			objectT = null;
		}
		//如果为空集,即返回一个空的列表

		return list;
	}

	public void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TableName, null, null);
		//		db.close();
	}
	public void deleteByEntity(T object) {
		deleteById(object.get_Id());
	}

	public void deleteById(int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TableName, _ID + " = ?", new String[] { String.valueOf(id) });
		//		db.close();
	}

	public void deleteByCondition(String where) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TableName, where, new String[] {});
	}

	public void updateObjectById(T object) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = createContentValues(object);
		db.update(TableName, cv, _ID + " = ?", new String[] { String.valueOf(object.get_Id()) });
		//		db.close();
	}

	//	public synchronized void close() {
	//		try {
	//			getWritableDatabase().close();
	//			getReadableDatabase().close();
	//		} catch (Exception e) {
	//		}
	//	}

	private T createObjectFromCursorData(Cursor cursor) {
		/**
		 * 如果发生异常,则返回null
		 * T类必须有一个无参数构造器
		 */
		T newobjecT = null;
		//把id字段在THIS_ALL_COLUMNS数组中去掉
		final String[] columns = new String[THIS_ALL_COLUMNS.length - 1];
		System.arraycopy(THIS_ALL_COLUMNS, 0, columns, 0, THIS_ALL_COLUMNS.length - 1);
		try {
			newobjecT = targetClass.newInstance();
			for (int i = 0; i < cursor.getColumnCount(); i++)
				for (String column : columns) {
					Field field = columsMap.get(column);
					field.setAccessible(true);
					field.set(newobjecT, createCursorValueByField(field, cursor, column));

				}
			//因为id这个域不在fields数组里,所以必须另外处理
			newobjecT.set_Id(cursor.getInt(cursor.getColumnIndex(_ID)));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newobjecT;
	}

	private Object createCursorValueByField(Field field, Cursor cursor, String columnname) {
		Object fValue = null;
		// System.out.println(field.getType());
		if (field.getType() == int.class) {
			fValue = Integer.valueOf(cursor.getInt(cursor.getColumnIndex(columnname)));
		} else if (field.getType() == long.class) {
			fValue = Long.valueOf(cursor.getLong(cursor.getColumnIndex(columnname)));
		} else if (field.getType() == String.class) {
			fValue = String.valueOf(cursor.getString(cursor.getColumnIndex(columnname)));
		} else if (field.getType() == double.class) {
			fValue = Double.valueOf(cursor.getDouble(cursor.getColumnIndex(columnname)));
		} else if (field.getType() == float.class) {
			fValue = Float.valueOf(cursor.getFloat(cursor.getColumnIndex(columnname)));
		} else if (field.getType() == boolean.class) {
			fValue = Boolean.valueOf(cursor.getInt(cursor.getColumnIndex(columnname)) == 1 ? true : false);
		}
		return fValue;
	}

	private T createNewObject(SQLiteDatabase db, T object) {
		ContentValues values = createContentValues(object);
		long id = db.insertOrThrow(TableName, null, values);
		object.set_Id((int) id);
		return object;
	}

	private ContentValues createContentValues(T object) {
		ContentValues values = new ContentValues();
		for (Field field : fields) {
			if (field.toString().contains(IDENTIFIER)) {
				final String columnname = field.getName().substring(IDENTIFIER.length());

				Class<?> ftype = field.getType();
				try {
					if (ftype == int.class) {
						int integer = field.getInt(object);
						values.put(columnname, Integer.valueOf(integer));
					} else if (ftype == long.class) {
						long mlong = field.getLong(object);

						values.put(columnname, Long.valueOf(mlong));
					} else if (ftype == String.class) {
						String mstring = (String) field.get(object);
						values.put(columnname, mstring);
					} else if (ftype == double.class) {
						double mdouble = field.getDouble(object);
						values.put(columnname, Double.valueOf(mdouble));
					} else if (ftype == float.class) {
						float mfloat = field.getFloat(object);
						values.put(columnname, Float.valueOf(mfloat));
					} else if (ftype == boolean.class) {
						boolean mboolean = field.getBoolean(object);

						values.put(columnname, mboolean ? 1 : 0);
					}

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return values;
	}

	/*private Object getFieldValue(Field field, T object) {
		// TODO Auto-generated method stub
		Class<?> ftype = field.getType();

		try {
			if (ftype == int.class) {
				int integer = field.getInt(object);
				return (Integer) Integer.valueOf(integer);

			} else if (ftype == long.class) {
				long mlong = field.getLong(object);
				return (Long) Long.valueOf(mlong);
			}else if (ftype==String.class) {
				String mstring=(String) field.get(object);
				return mstring;
			}else if (ftype==double.class) {
				double mdouble = field.getDouble(object);
				return (Double) Double.valueOf(mdouble);
			}else if (ftype==float.class) {
				float mfloat = field.getFloat(object);
				return (Float) Float.valueOf(mfloat);
			}else if (ftype==boolean.class) {
				boolean mboolean = field.getBoolean(object);
				return (Boolean) Boolean.valueOf(mboolean);
			}
				
		
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//否则返回空
		return null;
	}*/

	private String getTableName() {
		/**
		 * 根据目标类的类名转化为所要创建的表的表名
		 */
		return targetClass.getSimpleName().toUpperCase() + "S";

	}

	private ArrayList<String> createSQLStringfromFields(Field[] fields) {
		/**
		 * 该函数根据反射回来的域制造数据库字段字符,
		 * 并根据域的类型选择数据库中的字段类型,
		 * 因为并不是所有域都需要保存到数据库中,所以
		 * 要在保存的域的名字中加上"C_",如:
		 * private String C_Name;
		 * 注意:Id域会自动作为主键生成数据库字段,无需添加"C_"后缀.
		 * 目前仅支持基本类型和String的转换,若出现其他类型,一律当无数据类型处理.
		 * @return 字符数组
		 */

		ArrayList<String> list = new ArrayList<String>();
		for (Field field : fields) {
			if (field.toString().contains(IDENTIFIER)) {
				//去掉"C_"前缀
				final StringBuffer sb = new StringBuffer(field.getName().substring(IDENTIFIER.length()));
				Class<?> ftype = field.getType();
				if (ftype == int.class || ftype == long.class || ftype == short.class || ftype == boolean.class) {
					sb.append(" INTEGER");
				} else if (ftype == String.class) {
					sb.append(" TEXT");
				} else if (ftype == float.class) {
					sb.append(" FLOAT");
				} else if (ftype == double.class) {
					sb.append(" DOUBLE");
				}

				list.add(sb.toString());
			}
		}

		return list;

	}

	private HashMap<String, Field> getAllColumnsMap(Field[] fields) {
		HashMap<String, Field> columns = new HashMap<String, Field>();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].toString().contains(IDENTIFIER)) {
				final String column = new String(fields[i].getName().substring(IDENTIFIER.length()));
				columns.put(column, fields[i]);
			}
		}
		return columns;
	}

	private String[] getAllColumnStrings(Field[] fields) {

		ArrayList<String> list = new ArrayList<String>();
		int columnindex = 0;
		for (int i = 0; i < fields.length; i++) {
			//判断是否含有"C_"
			if (fields[i].toString().contains(IDENTIFIER)) {
				list.add(new String(fields[i].getName().substring(IDENTIFIER.length())));
			}
		}
		//数组最后一个元素必定是id,有些地方可能需要把id去掉.
		list.add(_ID);
		String[] columns = (String[]) list.toArray(new String[list.size()]);

		return columns;
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}
}
