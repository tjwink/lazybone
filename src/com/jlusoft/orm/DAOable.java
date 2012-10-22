package com.jlusoft.orm;


public abstract class DAOable {
	//id=-1时代表还该对象还没添加到数据库中
	private int _id = -1;

	/**
	 * 获取数据库id
	 * @return
	 */
	public int get_Id() {
		return _id;
	}

	/**
	 * 设置该实体的数据库id
	 * 最好不要手动设置ID,否则可能会出错
	 * @param _id
	 */
	public void set_Id(int _id) {

		if (_id >= 1) {
			this._id = _id;
		}
	}

	 
	public DAOable() {
		//什么也不做,构造器用于反射新建对象.
	}
}
