package com.shark.server.xml;

import com.google.common.collect.Lists;
import com.shark.server.util.StringUtil;
import com.shark.util.util.FileUtil;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @Author: SuLiang
 * @Date: 2018/9/26 0026
 * @Description:
 */
public class XmlParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);

	private String fileName;
	private Document document;
	private List<BeanCreate> beanCreates;
	private List<SharkXmlNode> nodes;

	public XmlParser(String fileName) {
		this.beanCreates = Lists.newArrayList();
		this.nodes = SharkXmlNode.beanNode();
		this.fileName = fileName;
	}

	public XmlParser() {
		this.beanCreates = Lists.newArrayList();
		this.nodes = SharkXmlNode.beanNode();
		this.fileName = "SharkConfig.xml";
	}

	public XmlParser readDoc() {
		this.document = Dom4jUtil.readDoc(fileName);
		return this;
	}

	public XmlParser parseAndCreateBean() {
		nodes.forEach(node -> beanCreates.addAll(node.parseNode(this.document)));
		parseSymbol(beanCreates, FileUtil.readProperties("db.properties"));
		beanCreates.forEach(beanCreate -> {
			try {
				beanCreate.getBeanType().createBean(beanCreates, beanCreate);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
		return this;
	}

	public XmlParser parseSymbol(Properties properties) {
		return this;
	}

	/**
	 * Parse node include symbol(eg: ${})
	 *
	 * @param beanCreates
	 * @param properties
	 */
	public XmlParser parseSymbol(List<BeanCreate> beanCreates, Properties properties) {
		for (BeanCreate beanCreate : beanCreates) {
			for (String name : beanCreate.getFileds().keySet()) {
				String value = beanCreate.getFileds().get(name);
				String key=StringUtil.clearSymbol(value, SharkXmlNode.SYMBOL_PROPERTY.getName());
				if (!key.equals(value)){
					value= (String) properties.get(key);
				}
				beanCreate.getFileds().put(name,value);
			}
			beanCreate.getElement().stream().filter(e->{
				String key=StringUtil.clearSymbol(e,SharkXmlNode.SYMBOL_PROPERTY.getName());
				return !e.equals(key);
			}).collect(Collectors.toList()).replaceAll(s -> StringUtil.clearSymbol(s, SharkXmlNode.SYMBOL_PROPERTY.getName()));
		}
		return this;
	}
}
