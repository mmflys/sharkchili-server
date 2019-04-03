package com.shark.server.container;

import com.google.common.collect.Maps;
import com.shark.container.Container;
import com.shark.feifei.db.FeifeiPoolDatasource;
import com.shark.job.factory.JobFactory;
import com.shark.job.job.AbstractScheduleJob;
import com.shark.job.job.ScheduleJob;
import com.shark.server.bean.Bean;
import com.shark.server.exception.SystemException;
import com.shark.server.rpc.RequestQueue;
import com.shark.server.socket.factory.SharkSocketFactory;
import com.shark.server.socket.message.Message;
import com.shark.server.socket.message.Request;
import com.shark.server.socket.socket.SharkSocket;
import com.shark.server.socket.socket.SocketIdentity;
import com.shark.server.socket.socket.Status;
import com.shark.server.xml.SharkXmlNode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class MainContainer implements Container {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainContainer.class);

	/**
	 * Bean容器
	 */
	private BeanContainer beanContainer;
	/**
	 * Class容器
	 */
	private ClassContainer classContainer;
	/**
	 * Task容器
	 */
	private ServerTaskContainer serverTaskContainer;
	/**
	 * My server
	 */
	private SharkSocket server;
	/**
	 * Remote server
	 */
	private Map<SocketIdentity, SharkSocket> clients;
	/**Request queue*/
	private RequestQueue requestQueue;

	private MainContainer() {
		beanContainer = new BeanContainer();
		classContainer = new ClassContainer();
		serverTaskContainer = new ServerTaskContainer();
		clients = Maps.newHashMap();
		requestQueue=new RequestQueue();
	}

	@Override
	public MainContainer init() {
		LOGGER.info(MainContainer.class.getSimpleName() + " init");
		beanContainer.init();
		classContainer.init();
		serverTaskContainer.init();
		//程序结束时调用钩子线程
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
		return this;
	}

	@Override
	public Container start() {
		LOGGER.info(MainContainer.class.getSimpleName() + " start");
		mainStart();
		beanContainer.start();
		classContainer.start();
		serverTaskContainer.start();
		return this;
	}

	@Override
	public Container stop() {
		LOGGER.info(MainContainer.class.getSimpleName() + " stop");
		beanContainer.stop();
		classContainer.stop();
		serverTaskContainer.stop();
		return this;
	}

	@Override
	public void containerInit() {

	}

	@Override
	public void containerStart() {

	}

	@Override
	public void containerStop() {

	}

	/**
	 * MainContainer start.
	 */
	private void mainStart() {
		bindLocalAndConnectRemote();
	}

	/**
	 * Bind local server and connect to remote server.
	 */
	private void bindLocalAndConnectRemote() {
		SocketIdentity localIdentity = getBean(SharkXmlNode.LOCAL_SERVER.getName());
		Collection<SocketIdentity> remoteServer = getBean(SharkXmlNode.REMOTE_SERVER.getName());
		// 创建本地服务器
		Job localStartJob = new AbstractScheduleJob() {
			@Override
			public void execute(JobExecutionContext context) throws JobExecutionException {
				try {
					MainContainer.this.server = SharkSocketFactory.server(localIdentity.getPort());
					MainContainer.this.server.identity(localIdentity);
					server.start();
					fireFinish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		ScheduleJob localJob = JobFactory.onceImmediatelyTask(localStartJob);
		schedule(localJob);
		// 创建远程连接
		if (!remoteServer.isEmpty()) {
			for (SocketIdentity identity : remoteServer) {
				Job connectsRemoter = new AbstractScheduleJob() {
					@Override
					public void execute(JobExecutionContext context) throws JobExecutionException {
						try {
							SharkSocket client = SharkSocketFactory.client(identity.getPublicAddress(), identity.getPort());
							client.identity(identity);
							MainContainer.get().getAllRemoteServer().put(identity, client);
							client.start();
							fireFinish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				ScheduleJob remoteJob = JobFactory.onceImmediatelyTask(connectsRemoter);
				schedule(remoteJob);
			}
		}
	}

	public BeanContainer getBeanContainer() {
		return beanContainer;
	}

	public ClassContainer getClassContainer() {
		return classContainer;
	}

	public ServerTaskContainer getServerTaskContainer() {
		return serverTaskContainer;
	}

	/**
	 * Get a message for specific name
	 *
	 * @param name
	 * @return
	 */
	public <T> T getBean(String name) {
		try {
			return (T) beanContainer.existBean(name).getProxy();
		} catch (NullPointerException e) {
			LOGGER.error("Bean not exist for name {}", name);
		}
		return null;
	}

	/**
	 * Get a message that it`s name is class name firstLowercase.
	 *
	 * @param C
	 * @param <T>
	 * @return
	 */
	public <T> T getBean(Class<? extends T> C) {
		Bean bean = beanContainer.existBean(C);
		if (bean == null) {
			LOGGER.error("get message failed,not exist message for class: {}", C.getSimpleName());
		} else {
			try {
				return (T) bean.getProxy();
			} catch (NullPointerException e) {
				LOGGER.error("Bean not exist for class {}", C.getSimpleName());
			}
		}
		return null;
	}

	/**
	 * Get a message for specific name or create a message
	 *
	 * @param C
	 * @param name
	 * @return
	 */
	public <T> T getOrCreateBean(Class<? extends T> C, String name) {
		return (T) beanContainer.getOrCreateBean(C, name).getProxy();
	}

	/**
	 * Get a message for Class firstLowercase name or create a message
	 *
	 * @param C
	 * @param <T>
	 * @return
	 */
	public <T> T getOrCreateBean(Class<? extends T> C) {
		return (T) beanContainer.getOrCreateBean(C).getProxy();
	}

	/**
	 * Get a scheduler,default its name is 'tourists'
	 *
	 * @return
	 */
	public Scheduler getScheduler() {
		return serverTaskContainer.getScheduler();
	}

	/**
	 * Schedule a com.shark.job.job
	 *
	 * @param job
	 */
	public void schedule(ScheduleJob job) {
		serverTaskContainer.schedule(job);
	}

	/**
	 * Get remote class method
	 *
	 * @param C
	 * @param method
	 * @param parameters
	 * @return
	 */
	public Method getRemoteMethod(Class<?> C, String method, Class... parameters) {
		return getClassContainer().getRemoteMethod(C, method, parameters);
	}

	/**
	 * Block to get remote server socket.
	 *
	 * @param identity
	 * @return
	 */
	public SharkSocket getRemoteServer(SocketIdentity identity) {
		for (SocketIdentity socketIdentity : clients.keySet()) {
			if (socketIdentity.equals(identity)) {
				SharkSocket sharkSocket;
				while (true) {
					sharkSocket = clients.get(socketIdentity);
					if (sharkSocket != null && sharkSocket.getStatus() == Status.RUNNING) return sharkSocket;
				}
			}
		}
		throw new SystemException("Remote server not exist for given identity {}, remoteServer", identity, clients);
	}

	/**
	 * Block to get local server.
	 *
	 * @return
	 */
	public SharkSocket getLocalServer() {
		while (true) {
			if (server != null) return server;
		}
	}

	private	Map<SocketIdentity, SharkSocket> getAllRemoteServer(){
		return clients;
	}

	/**
	 * Remote request synchronously
	 * @param socketIdentity
	 * @param request
	 * @return
	 */
	public Object synRequest(SocketIdentity socketIdentity, Message request){
		SharkSocket sharkSocket = getRemoteServer(socketIdentity);
		sharkSocket.request(request);
		LOGGER.info("Remote syn request: {}",request);
		return getRequestQueue().blockRequest((Request) request).getBody();
	}

	/**
	 * Remote request asynchronously
	 * @param socketIdentity
	 * @param request
	 */
	public Object asynRequest(SocketIdentity socketIdentity, Message request){
		SharkSocket sharkSocket = getRemoteServer(socketIdentity);
		sharkSocket.request(request);
		LOGGER.info("Remote asyn request: {}",request);
		return getRequestQueue().noBlockRequest((Request)request);
	}

	/**
	 * Get a singleton
	 *
	 * @return
	 */
	public static MainContainer get() {
		return InstanceHolder.MAIN_CONTAINER;
	}

	/**
	 * Inner class for hold external class instance
	 */
	private static class InstanceHolder {

		private static final MainContainer MAIN_CONTAINER = new MainContainer();

		private void InvocationHandler() {
		}
	}

	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public Connection getConnection() throws SQLException {
		FeifeiPoolDatasource dataSource= (FeifeiPoolDatasource) getOrCreateBean(DataSource.class);
		return dataSource.getConnection();
	}
}
