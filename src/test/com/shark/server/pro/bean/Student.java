package com.shark.server.pro.bean;

import java.io.Serializable;

/**
 * @Author: SuLiang
 * @Date: 2018/8/30 0030
 * @Description:
 */
public class Student implements Serializable {
	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void speak(){
		System.out.println(this.toString()+" speak...");
	}

	@Override
	public String toString() {
		return "Student{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
