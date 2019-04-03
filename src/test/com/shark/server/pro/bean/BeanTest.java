package com.shark.server.pro.bean;

import com.shark.server.annotation.Pit;
import com.shark.server.container.MainContainer;
import com.shark.server.xml.Dom4jUtil;
import com.shark.server.xml.SharkXmlNode;
import org.testng.annotations.Test;

public class BeanTest {
	@Pit
	private Teacher myStr;

	public Teacher getMyStr() {
		return myStr;
	}

	public void setMyStr(Teacher myStr) {
		this.myStr = myStr;
	}

	@Test
	public void testBean(){
		MainContainer mainContainer=MainContainer.get();
		mainContainer.init();

		BeanTest beanTest=mainContainer.getOrCreateBean(BeanTest.class);
		beanTest.getMyStr().setName("FeiFei");
		System.out.println(beanTest);
	}

	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("pro.bean.Teacher");
	}

	@Test
	public void nodeTest(){
		System.out.println(Dom4jUtil.searchSingleNode(SharkXmlNode.SERVER_CONFIG,Dom4jUtil.readDoc("SharkConfig.xml")));
	}

	@Override
	public String toString() {
		return "BeanTest{" +
				"myStr=" + myStr +
				'}';
	}
}
