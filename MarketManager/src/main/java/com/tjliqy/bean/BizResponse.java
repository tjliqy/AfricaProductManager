package com.tjliqy.bean;

import java.util.HashMap;

public class BizResponse extends HashMap {

	private final String SUCCESS = "success";
	private final String MSG = "msg";

	public BizResponse() {
		this.put(SUCCESS, true);
		this.put(MSG, "operation success.");
	}

	public BizResponse(String key, Object value) {
		this();
		this.add(key, value);
	}

	public BizResponse add(String key, Object value) {
		this.put(key, value);
		return this;
	}

	public BizResponse fail(String msg) {
		this.clear();
		this.put(SUCCESS, false);
		this.put(this.MSG, msg);
		return this;
	}
}
