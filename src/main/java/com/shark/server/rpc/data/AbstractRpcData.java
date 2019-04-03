package com.shark.server.rpc.data;

import com.shark.server.container.MainContainer;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description:
 */
public class AbstractRpcData implements RpcData {
	/**THe name of remote method*/
	private String method;
	/**The parameters of remote method*/
	private Object[] parameters;
	/**The name of remote object*/
	private Class origin;

	public static RpcData create(Class origin, String method, Object... parameters) {
		AbstractRpcData rpcData = new AbstractRpcData();
		rpcData.setOrigin(origin);
		rpcData.setMethod(method);
		rpcData.setParameters(parameters);
		return rpcData;
	}

	public String getMethod() {
		return method;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public Class getOrigin() {
		return origin;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setOrigin(Class origin) {
		this.origin = origin;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	@Override
	public String method() {
		return this.method;
	}

	@Override
	public Class origin() {
		return this.origin;
	}

	@Override
	public Object[] parameters() {
		return this.parameters;
	}

	public Object invoke() throws Throwable {
		Class<?>[] parameterTypes = new Class<?>[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterTypes[i] = parameters[i].getClass();
		}
		try {
			Object proxy = MainContainer.get().getBean(origin);
			Method method = MainContainer.get().getRemoteMethod(origin, method(), parameterTypes);
			return method.invoke(proxy, parameterTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "AbstractRpcData{" +
				"method='" + method + '\'' +
				", parameters=" + Arrays.toString(parameters) +
				", origin=" + origin +
				'}';
	}
}
