package com.shark.server.util;

import com.shark.server.xml.SharkXmlNode;

public class StringUtil {
	/**
	 * Parses and removes symbols from a string
	 * eg: ${url}->url
	 *
	 * @param str a String object
	 * @param symbol include String object eg: ${url}
	 * @return
	 */
	public static String clearSymbol(String str, String symbol) {
		String result = null;
		if (symbol.equals(SharkXmlNode.SYMBOL_PROPERTY.getName())) {
			int headIndex = str.indexOf("${");
			int tailIndex = str.indexOf('}');
			if (headIndex != -1 && tailIndex != -1 && headIndex < tailIndex) {
				result = str.substring(0, headIndex) + str.substring(headIndex + 2, tailIndex) + str.substring(tailIndex + 1);
			}
		}
		return result == null ? str : result;
	}
}
