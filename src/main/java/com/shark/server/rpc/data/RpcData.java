package com.shark.server.rpc.data;

import java.io.Serializable;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description:
 */
public interface RpcData extends Serializable {
	String method();
	Class origin();
	Object[] parameters();
	Object invoke() throws Throwable ;
}
