package com.shark.server.pro.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description:
 */
public class Response implements Serializable {
	private Map<String,Object> valus;

	public Map<String, Object> getValus() {
		return valus;
	}

	public void setValus(Map<String, Object> valus) {
		this.valus = valus;
	}

	@Override
	public String toString() {
		return "Response{" +
				"valus=" + valus +
				'}';
	}
}
