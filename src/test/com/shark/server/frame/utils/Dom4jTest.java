package com.shark.server.frame.utils;

import com.shark.server.container.MainContainer;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Iterator;

/**
 * @Author: SuLiang
 * @Date: 2018/8/31 0031
 * @Description:
 */
public class Dom4jTest {

	public Document findDoc(){
		URL url=Dom4jTest.class.getResource("/SharkConfig.xml");
		System.out.println(url);
		SAXReader reader=new SAXReader();
		try {
			return reader.read(url);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void barIterator() {
		Document document=findDoc();
		Element root = document.getRootElement();
		// iterate through child elements of root
		for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element element = it.next();
			System.out.println(element.getName());
			// do something
		}
		// iterate through child elements of root with element name "foo"
		for (Iterator<Element> it = root.elementIterator("message-config"); it.hasNext();) {
			Element foo = it.next();
			System.out.println(foo.getName());
			// do something
		}
		// iterate through attributes of root
		for (Iterator<Attribute> it = root.attributeIterator(); it.hasNext();) {
			Attribute attribute = it.next();
			// do something
		}
	}

	@Test
	public void barXPath(){
		//启动容器
		MainContainer mainContainer=MainContainer.get();
		mainContainer.init();

		/*Document document=findDoc();
		List<Node> list = document.selectNodes("//message");
		NodeParser nodeParser=new NodeParser();
		List<BeanCreate> beanCreates=nodeParser.parseBean(list);
		System.out.println(beanCreates);
		nodeParser.registerBean(beanCreates);*/
	}

}
