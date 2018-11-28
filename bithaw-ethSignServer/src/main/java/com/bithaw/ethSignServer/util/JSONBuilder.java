package com.bithaw.ethSignServer.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description 构造者模式构造JSONObject
 * @author WangWei
 * @date 2018年9月10日 上午9:47:41
 * @version V 1.0
 */
public class JSONBuilder {
	private JSONObject jsonObject;

	public JSONBuilder() {
		this.jsonObject = new JSONObject();
	}

	public JSONBuilder put(String key, String value) {
		this.jsonObject.put(key, value);
		return this;
	}

	public JSONBuilder put(String key, int value) {
		this.jsonObject.put(key, value);
		return this;
	}

	public JSONObject build() {
		return this.jsonObject;
	}
}