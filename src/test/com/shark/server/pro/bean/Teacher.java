package com.shark.server.pro.bean;

import java.io.Serializable;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description:
 */
public class Teacher implements Serializable {
	private Student student;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public String toString() {
		return "Teacher{" +
				"student=" + student +
				", name='" + name + '\'' +
				'}';
	}
}
