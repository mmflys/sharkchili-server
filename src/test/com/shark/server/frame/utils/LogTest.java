package com.shark.server.frame.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

	public static final Logger LOGGER=LoggerFactory.getLogger(LogTest.class);

	public static void main(String[] args) {
		LogTest.LOGGER.info("Hello World!");
	}

}
