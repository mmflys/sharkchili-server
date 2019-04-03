package com.shark.server.pro.util;


import com.shark.util.classes.ClassScannerUtil;
import org.testng.annotations.Test;

public class ClassScannerTest {

	@Test
	public void classScanner() {
		String packageName = "util.utils";
		ClassScannerUtil classScanner = new ClassScannerUtil();
		for (Class<?> C : classScanner.scanClass(packageName)) {
			System.out.println(C.getName());
		}
	}

	@Test
	void pathTest() {
		System.out.println(ClassScannerTest.class.getPackage().getName());
	}
}
