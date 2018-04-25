package com.factory.domain.type;

public enum RoleOffsetType {
	
	ONE_LEVEL_DOWN(1);
	
	private int offset;
	
	RoleOffsetType (int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return offset;
	}

}
