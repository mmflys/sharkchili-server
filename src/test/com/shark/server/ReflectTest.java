package com.shark.server;

import com.shark.server.pro.bean.Student;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description:
 * 结论:
 * 	反射调用时, time(getMethod) > time(invoke) >time(instance)
 * 	为了加速反射时调用速度,可以先缓存反射方法
 */
public class ReflectTest {
	private static Method method;

	static {
		try {
			method = Student.class.getDeclaredMethod("speak");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private static Student student=new Student();

	/**
	 * 运行时间: 1.381s
	 * @throws NoSuchMethodException
	 */
	@Test(invocationCount = 10)
	public void getMethod() throws NoSuchMethodException {
		Method method= Student.class.getDeclaredMethod("speak");
	}

	/**
	 * 运行时间: 591ms
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@Test(invocationCount = 10)
	public void invoke() throws InvocationTargetException, IllegalAccessException {
		ReflectTest.method.invoke(ReflectTest.student);
	}

	/**
	 * 555ms
	 */
	@Test(invocationCount = 10)
	public void normal(){
		ReflectTest.student.speak();
	}
}
