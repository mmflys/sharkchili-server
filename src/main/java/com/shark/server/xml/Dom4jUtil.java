package com.shark.server.xml;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/8/31 0031
 * @Description:
 */
public class Dom4jUtil {
	private static final Logger LOGGER= LoggerFactory.getLogger(Dom4jUtil.class);

	/**
	 * Read target document.
	 */
	public static Document readDoc(){
		Document document = null;
		URL url= Dom4jUtil.class.getResource("/SharkConfig.xml");
		LOGGER.info("read document from path: {}",url);
		SAXReader reader=new SAXReader();
		try {
			document=reader.read(url);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static Document readDoc(String fileName){
		Document document = null;
		URL url= Dom4jUtil.class.getResource("/"+fileName);
		LOGGER.info("read document from path: {}",url);
		SAXReader reader=new SAXReader();
		try {
			document=reader.read(url);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * search all node for the node name.
	 * @param node
	 * @return
	 */
	public static List searchNodes(SharkXmlNode node, Document document){
		String xpathExpression="//"+node.prefix()+":"+node.getName();
		XPath xPath=document.createXPath(xpathExpression);
		xPath.setNamespaceURIs(NameSpace.all());
		List<Node> nodes=xPath.selectNodes(document);
		LOGGER.debug("search node: {} for xpathExpression: {} from document: {}",nodes,xpathExpression,document);
		return nodes;
	}

	/**
	 * search single node for the node name.
	 * @param node
	 * @param document
	 * @return
	 */
	public static Node searchSingleNode(SharkXmlNode node, Document document){
		String xpathExpression="//"+node.prefix()+":"+node.getName();
		XPath xPath=document.createXPath(xpathExpression);
		xPath.setNamespaceURIs(NameSpace.all());
		Node result=xPath.selectSingleNode(document);
		LOGGER.debug("search node: {} for xpathExpression: {} from document: {}",result,xpathExpression,document);
		return result;
	}

	public static Element searchRoot(Document document){
		return document.getRootElement();
	}
}
