package com.shark.server.socket.message.http;

import io.netty.handler.codec.http.HttpVersion;

import java.util.regex.Pattern;

/**
 * @Author: SuLiang
 * @Date: 2018/9/16
 * @Description:
 */
public class RequestLine {
	public static final String METHOD_NAME = "method";
	public static final String METHOD_VALUE_GET = "GET";
	public static final String METHOD_VALUE_POST = "POST";

	public static final String PATH_TO_RESOURCE_NAME = "path-to-resource";

	public static final String VERSION_NUMBER_NAME = "version-number";

	/**
	 * Judge whether str is a http request line.
	 * eg: GET http://www.baidu.com/ HTTP/1.1
	 * @param str
	 * @return
	 */
	public static boolean isRequestLine(String str) {
		String[] subStr = str.split(" ");
		for (String aSubStr : subStr) {
			if (!isMethod(aSubStr) && !isPathToResource(aSubStr) && !isVersionNumber(aSubStr)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isMethod(String str) {
		return str.equals(METHOD_VALUE_POST) || str.equals(METHOD_VALUE_GET);
	}

	private static boolean isPathToResource(String str) {
		String pattern = "[a-zA-z]+://[^\\s]*";
		return Pattern.matches(pattern, str);
	}

	private static boolean isVersionNumber(String str) {
		return str.equals(HttpVersion.HTTP_1_1.protocolName())||str.equals(HttpVersion.HTTP_1_0.protocolName());
	}
}
