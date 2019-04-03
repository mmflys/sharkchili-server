package com.shark.server.socket.message;

import java.io.Serializable;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description:
 */
public class School implements Serializable {
	private String name;
	private int size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "School{" +
				"name='" + name + '\'' +
				", size=" + size +
				'}';
	}
}
