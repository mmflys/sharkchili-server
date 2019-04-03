package com.shark.server.bean;

import com.shark.server.annotation.consts.Origin;

public class DefaultBean implements Bean {
	/**原生对象*/
	private Object object;
	/**对象名*/
	private String name;
	/**代理对象*/
	private Object proxy;
	/**bean路径*/
	private String path;
	/**来源*/
	private Origin origin;
	/**创建时间*/
	private long createTime;
	/**更新时间(使用时间)*/
	private long updateTime;

	@Override
	public void setObject(Object obj) {
		this.object=obj;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	public void setProxy(Object proxy) {
		this.proxy= proxy;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "DefaultBean{" +
				"object=" + object +
				", name='" + name + '\'' +
				", path='" + path + '\'' +
				", origin=" + origin +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
