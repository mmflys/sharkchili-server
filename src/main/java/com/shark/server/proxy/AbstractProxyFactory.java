package com.shark.server.proxy;

import com.shark.server.annotation.Asyn;
import com.shark.server.annotation.Remote;
import com.shark.server.annotation.Syn;
import com.shark.server.annotation.behavior.Behavior;
import com.shark.server.annotation.behavior.Reinforce;
import com.shark.server.container.MainContainer;
import com.shark.server.exception.SystemException;
import com.shark.server.rpc.RemoteService;
import com.shark.server.rpc.data.AbstractRpcData;
import com.shark.server.rpc.data.RpcData;
import com.shark.server.socket.message.Message;
import com.shark.server.socket.message.Request;
import com.shark.server.socket.message.Type;
import com.shark.server.socket.socket.SharkSocket;
import com.shark.server.socket.socket.SocketIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

public abstract class AbstractProxyFactory implements ProxyFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CgLibProxyFactory.class);

	/**
	 * 原对象
	 */
	private Object origin;

	@Override
	public void setOrigin(Object object) {
		this.origin = object;
	}

	@Override
	public Object getOrigin() {
		return this.origin;
	}

	/**
	 * Pre reinforce for method
	 *
	 * @param method
	 */
	void preReinforce(Object object, Method method, Set<Behavior> reinforceSet) {
		//增强器的方法不用代理
		if (object instanceof Behavior) return;
		else {
			reinforceSet.addAll(MainContainer.get().getClassContainer().getAnnotationBehaviorMap().values());
			//根据注解做相应增强
			for (Reinforce reinforce : reinforceSet) {
				//判断是否打开该增强
				if (reinforce.isOpened(object, method)) {
					LOGGER.debug("Pre reinforce {} for method: {}", reinforce, method.getName());
					reinforce.preReinforce(object, method);
				}
			}
		}
	}

	/**
	 * Rear reinforce for method
	 *
	 * @param method
	 */
	void rearReinforce(Object object, Method method, Set<Behavior> reinforceSet) {
		//增强器的方法不用代理
		if (object instanceof Behavior) return;
		//根据注解做相应增强
		for (Reinforce reinforce : reinforceSet) {
			//判断该增强是否打开
			if (reinforce.isOpened(object, method)) {
				LOGGER.debug("Rear reinforce {} for method: {}", reinforce, method.getName());
				reinforce.rearReinforce(object, method);
			}
		}
	}

	/**
	 * Remote request method.
	 *
	 * @param object
	 * @param method
	 * @param args
	 * @return
	 */
	Object remoteInvoke(Object object, Method method, Object[] args) {
		RpcData rpcData = AbstractRpcData.create(object.getClass(), method.getName(), args);
		Remote remote = object.getClass().getDeclaredAnnotation(Remote.class);
		if (remote == null) {
			throw new SystemException("Remote service have no @Remote %s", object);
		}
		SocketIdentity remoteIdentity = SocketIdentity.create(remote.name(), remote.publicAddress(), remote.port());
		SocketIdentity localIdentity = MainContainer.get().getLocalServer().identity();
		Message request = Request.create(rpcData, localIdentity, remoteIdentity).type(Type.BLOCK);
		Syn syn = method.getAnnotation(Syn.class);
		Asyn asyn = method.getAnnotation(Asyn.class);
		if (syn != null) {
			return MainContainer.get().synRequest(remoteIdentity, request);
		}
		if (asyn != null) {
			return MainContainer.get().asynRequest(remoteIdentity, request);
		}
		// 默认同步请求
		return MainContainer.get().synRequest(remoteIdentity, request);
	}

	/**
	 * Whether method is a remote request method or not.
	 *
	 * @param object
	 * @return
	 */
	boolean isRemote(Object object) {
		if (RemoteService.class.isAssignableFrom(object.getClass())) {
			// 是远程请求
			Remote remote = object.getClass().getDeclaredAnnotation(Remote.class);
			if (remote == null) {
				throw new SystemException("Remote service have no @Remote %s", object);
			}
			SocketIdentity socketIdentity = SocketIdentity.create(remote.name(), remote.publicAddress(), remote.port());
			SharkSocket localServer = MainContainer.get().getLocalServer();
			boolean isLocal = localServer.identity().equals(socketIdentity);
			return !isLocal;
		} else return false;
	}
}
