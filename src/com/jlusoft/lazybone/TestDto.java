package com.jlusoft.lazybone;


import java.util.ArrayList;

import com.jlusoft.lazybone.orm.Ignore;
import com.jlusoft.lazybone.orm.LazyDto;

public class TestDto extends LazyDto{
	private int num;
	@Ignore
	private String name;
	private ArrayList<String> work;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getWork() {
		return work;
	}
	public void setWork(ArrayList<String> work) {
		this.work = work;
	}
	
}
