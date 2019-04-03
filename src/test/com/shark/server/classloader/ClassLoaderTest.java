package com.shark.server.classloader;

import com.shark.server.classloder.ClassLoaderFactory;
import com.shark.server.classloder.FileSystemClassLoader;
import org.testng.annotations.Test;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/11/27
 */
public class ClassLoaderTest {

	@Test
	public void testLoadFile() throws ClassNotFoundException {
		String url="E:\\WorkSpace\\Git\\java\\sharkchili\\sharkchili-feifei\\target\\classes";

		FileSystemClassLoader classLoader=new FileSystemClassLoader(url);
		Class<?> feiFeiBootStrap=classLoader.loadClass("FeiFeiBootStrap");
		System.out.println(feiFeiBootStrap);
	}

	@Test
	public void testLoadJar() throws ClassNotFoundException {
		String url="E:\\Sys\\apache-maven-3.5.4\\repository\\com\\google\\guava\\guava\\26.0-jre";

		FileSystemClassLoader classLoader=new FileSystemClassLoader(url);
		Class c=classLoader.loadClass("common.collect.AbstractBiMap");
		System.out.println(c);
	}

	@Test
	public void testLoadNetWorkFile1() throws ClassNotFoundException {
		String url="http://sharkchili-dev.oss-cn-beijing.aliyuncs.com/test/";

		ClassLoader classloader= ClassLoaderFactory.classLoader(url);
		Class c=classloader.loadClass("ReflectTest");
		System.out.println(c);
	}
}
