package com.shark.server.xml;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author: SuLiang
 * @Date: 2018/10/10 0010
 * @Description:
 */
public enum NameSpace {

	DEFAULT("shark","http://www.sharkchili.com/schema/all");

	private String prefix;
	private String url;

	NameSpace(String prefix, String url) {
		this.prefix = prefix;
		this.url = url;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * Put all name space to map.
	 */
	public static Map<String,String> all(){
		Map<String,String> nameSpace= Maps.newHashMap();
		for (NameSpace ns : NameSpace.values()) {
			nameSpace.put(ns.prefix,ns.url);
		}
		return nameSpace;
	}
}
