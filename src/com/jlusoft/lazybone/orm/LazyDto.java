package com.jlusoft.lazybone.orm;


public abstract class LazyDto {
	//id=-1时代表还该对象还没添加到数据库中
	private int _id = -1;

	/**
	 * 获取数据库id
	 * @return
	 */
	public int getSqliteId() {
		return _id;
	}

	/**
	 * 设置该实体的数据库id
	 * 最好不要手动设置ID,否则可能会出错
	 * @param _id
	 */
	public void setSqliteId(int _id) {

		if (_id >= 1) {
			this._id = _id;
		}
	}

	 
	public LazyDto() {
		//什么也不做,构造器用于反射新建对象.
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LazyDto other = (LazyDto) obj;
		if (_id != other._id)
			return false;
		return true;
	}
}
