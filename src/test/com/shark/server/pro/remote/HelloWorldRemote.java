package com.shark.server.pro.remote;

import com.shark.server.rpc.RemoteService;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description:
 */
public class HelloWorldRemote implements RemoteService {

	public void hello(){
		System.out.println("Hello");

	}
}
