package com.shark.server.socket.consts;

/**
 * @Author: SuLiang
 * @Date: 2018/9/15 0015
 * @Description:
 */
public class CodecConst {
	/**Transport message delimiter string*/
	public static final String DELIMITER="\n";
	/**Max length of transport message frame length*/
	public static final int FRAME_LENGTH_MAX=64*1024;
	/**Fixed length of transported message*/
	public static final int FIXED_LENGTH=1024;
	/**Request type property name */
	public static final String REQUEST_TYPE="requestType";
	/**Request from http*/
	public static final String REQUEST_TYPE_HTTP="http";
	/**Request nor from http*/
	public static final String REQUEST_TYPE_NORMAL="normal";
}
