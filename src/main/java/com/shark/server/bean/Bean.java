package com.shark.server.bean;

import com.shark.server.annotation.consts.Origin;

/**
 * A object of a specific class.
 */
public interface Bean {

	void setObject(Object obj);

	void setName(String name);

	String getName();

	Object getProxy();

	Object getObject();

	String getPath();

	void setPath(String path);

	void setProxy(Object proxy);

	Origin getOrigin();

	void setOrigin(Origin origin);

	long getCreateTime();

	void setCreateTime(long createTime);

	long getUpdateTime();

	void setUpdateTime(long updateTime);
}
