package com.factory.dao.impl;

import java.util.ArrayList;
import java.util.List;

public class NVPairList {
	private List < NVPair > pairList;

	public NVPairList() {
		this.pairList = new ArrayList < NVPair > ();
	}

	public void addValue(String name, Object value) {
		this.pairList.add(new NVPair(name, value));
	}

	public void addNullableNumValue(String name, Object value) {
		if((int)value == -1){
			this.pairList.add(new NVPair(name, null));
		}else{
			this.pairList.add(new NVPair(name, value));
		}
	}
	
	public void addValueNotNull(String paramName, Object value) throws IllegalArgumentException {
		if (value != null) {
			this.addValue(paramName, value);
		} else {
			throw new IllegalArgumentException(paramName + " must not be null");
		}
	}

	public void addAll(NVPairList otherList) {
		this.pairList.addAll(otherList.getList());
	}

	public List < NVPair > getList() {
		return this.pairList;
	}

	public int size() {
		return this.pairList.size();
	}
}
