package com.shark.server.xml;

import com.google.common.collect.Lists;
import com.shark.server.exception.SystemException;
import com.shark.util.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.Node;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: SuLiang
 * @Date: 2018/8/31 0031
 * @Description:
 */
public enum SharkXmlNode {
	/**symbol**/

	/**
	 * tag a reference object
	 */
	SYMBOL_REF("@"),
	/**
	 * tag object from properties file
	 */
	SYMBOL_PROPERTY("$"),

	/**
	 * component
	 **/
	ROOT("shark-config"),

	SCAN_PATH("scan-path"),
	AO("ao"),
	DAO("dao"),
	SERVICE("service"),

	DATASOURCE("datasource"),

	BEAN_CONFIG("bean-config"),
	BEAN("bean"),
	CLASS("class"),
	ELEMENT_CLASS("elementClass"),
	FIELD_NAME("fieldName"),
	PROPERTY("property"),
	ELEMENT("element"),
	NAME("name"),
	VALUE("value"),
	REF("ref"),

	SERVER_CONFIG("server-config"),
	LOCAL_SERVER("local-server") {
		@Override
		public List<BeanCreate> parseNode(Document document) {
			return parseServerNode(document);
		}
	},
	REMOTE_SERVER("remote-server") {
		@Override
		public List<BeanCreate> parseNode(Document document) {
			return parseServerNode(document);
		}
	},
	REMOTE("remote"),
	PORT("port"),
	publicaddress("publicAddress"),


	SERVER("server"),

	COLLECTION("collection");

	SharkXmlNode(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public NameSpace nameSpace(){
		return NameSpace.DEFAULT;
	}

	public String prefix() {
		return NameSpace.DEFAULT.getPrefix();
	}

	public List<BeanCreate> parseNode(Document document) {
		List<BeanCreate> beanCreates = Lists.newArrayList();
		List<Node> nodes = Dom4jUtil.searchNodes(this, document);
		nodes.stream().filter(node -> !StringUtil.isEmpty(node.getName())).forEach(node -> parseBeanNode(beanCreates, node, null));
		return beanCreates;
	}

	public List<BeanCreate> parseServerNode(Document document) {
		List<BeanCreate> beanCreates = Lists.newArrayList();
		String identityClass;
		Node serverConfigNode=Dom4jUtil.searchSingleNode(SERVER_CONFIG,document);
		if (serverConfigNode==null){
			throw new SystemException("document {} server-config is`t exist.",document);
		}
		Node identityClassNode=serverConfigNode.selectSingleNode("attribute::"+CLASS.getName());
		// 若没有设置identity class路径则使用默认值
		if (identityClassNode==null||StringUtil.isEmpty(identityClassNode.getStringValue())){
			identityClass=XmlConst.SOCKET_SOCKET_IDENTITY;
		}else {
			identityClass=identityClassNode.getStringValue();
		}
		Node remoteNode = Dom4jUtil.searchSingleNode(this, document);
		if (remoteNode==null){
			return beanCreates;
		}
		// 属性
		List<Node> attributes = remoteNode.selectNodes("attribute::*");
		attributes=attributes.stream().filter(a->!StringUtil.isEmpty(a.getName())).collect(Collectors.toList());
		BeanCreate collectionBean = new BeanCreate();
		// 以标签名为bean name.
		collectionBean.setBeanName(remoteNode.getName());
		// 子元素
		List<Node> childs = remoteNode.selectNodes("child::*");
		childs=childs.stream().filter(c->!StringUtil.isEmpty(c.getName())).collect(Collectors.toList());
		if (childs.isEmpty()) {
			// 属性
			attributes.stream().filter(attribute->!attribute.getName().equals(CLASS.getName()))
					.forEach(attribute->collectionBean.getFileds().put(attribute.getName(),attribute.getStringValue()));
			collectionBean.setBeanType(BeanType.NORMAL);
			// 设置类路径
			collectionBean.setClassPath(identityClass);
		} else {
			collectionBean.setBeanType(BeanType.COLLECTION);
			Node collectionClassPath=remoteNode.selectSingleNode("attribute::"+CLASS.getName());
			collectionBean.setElementClassPath(identityClass);
			if (collectionClassPath==null||StringUtil.isEmpty(collectionClassPath.getStringValue())){
				collectionBean.setClassPath(XmlConst.COLLECTION_CLASS);
			}else {
				collectionBean.setClassPath(collectionClassPath.getStringValue());
			}
		}

		for (Node r : childs) {
			BeanCreate remoteBean = new BeanCreate();
			// 设置bean name
			Node name = r.selectSingleNode("attribute::" + NAME.getName());
			remoteBean.setBeanName(name.getStringValue());
			// 设置class path
			remoteBean.setClassPath(identityClass);
			// 属性
			List<Node> remoteAttributes = r.selectNodes("attribute::*");
			remoteAttributes = remoteAttributes.stream().filter(ra -> !StringUtil.isEmpty(ra.getName())).collect(Collectors.toList());
			remoteAttributes.stream().filter(ra -> !ra.getName().equals(CLASS.getName()))
					.forEach(ra -> remoteBean.getFileds().put(ra.getName(), ra.getStringValue()));
			collectionBean.getElement().add(SYMBOL_REF.getName() + remoteBean.getBeanName());
			beanCreates.add(remoteBean);
		}
		beanCreates.add(collectionBean);
		return beanCreates;
	}

	/**
	 * Parse bean node to {@link BeanCreate}
	 *
	 * @param beanCreates
	 * @param node
	 * @param superBean
	 */
	public void parseBeanNode(List<BeanCreate> beanCreates, Node node, BeanCreate superBean) {
		boolean recursive = false;
		BeanCreate beanCreate = null;
		// 1.属性
		List<Node> attributes = node.selectNodes("attribute::*");
		// 若是bean则需要创建一个BeanCreate
		if (isBean(node.getName())) {
			beanCreate = new BeanCreate();
			beanCreate.setBeanType(BeanType.NORMAL);
			beanCreates.add(beanCreate);
			String fieldName = setBeanAttribute(beanCreate, attributes);
			// 该bean被包含在父bean中,fieldName 指明了字段名
			if (superBean != null) {
				superBean.getFileds().put(fieldName, SYMBOL_REF.getName() + beanCreate.getBeanName());
			}
			recursive = true;
		}
		// 若是property元素则设置字段名,值 eg:<property name="name" object="Tu Xiao"/>
		else if (node.getName().equals(PROPERTY.getName())) {
			propertyValue(superBean, attributes);
		}
		// 若是element元素则设置元素值 eg: <element>13</element>
		else if (node.getName().equals(ELEMENT.getName())) {
			String elementValue = elementValue(node);
			superBean.getElement().add(elementValue);
			superBean.setBeanType(BeanType.COLLECTION);
		}
		// eg: <database>${database}</database>
		else {
			superBean.getFileds().put(node.getName(), node.getStringValue());
		}
		// 2.元素
		List<Node> elements = (List<Node>) node.selectNodes("child::node()");
		// element 元素可设置文本值
		if (recursive) {
			for (Node element : elements) {
				if (!StringUtil.isEmpty(element.getName())) {
					parseBeanNode(beanCreates, element, beanCreate);
				}
			}
		}
	}

	private void propertyValue(BeanCreate superBean, List<Node> attributes) {
		String fieldName = null, fieldValue = null;
		for (Node attribute : attributes) {
			if (attribute.getName().equals(NAME.getName())) {
				fieldName = attribute.getStringValue();
			}
			if (attribute.getName().equals(VALUE.getName())) {
				fieldValue = attribute.getStringValue();
			}
			if (attribute.getName().equals(REF.getName())) {
				fieldValue = SYMBOL_REF.getName() + attribute.getStringValue();
			}
		}
		if (!StringUtil.isEmpty(fieldName)) {
			superBean.getFileds().put(fieldName, fieldValue);
		}
	}

	private String setBeanAttribute(BeanCreate beanCreate, List<Node> attributes) {
		String fieldName = null;
		for (Node attribute : attributes) {
			if (attribute.getName().equals(NAME.getName())) {
				beanCreate.setBeanName(attribute.getStringValue());
			}
			if (attribute.getName().equals(CLASS.getName())) {
				beanCreate.setClassPath(attribute.getStringValue());
			}
			if (attribute.getName().equals(ELEMENT_CLASS.getName())) {
				beanCreate.setElementClassPath(attribute.getStringValue());
			}
			if (attribute.getName().equals(FIELD_NAME.getName())) {
				fieldName = attribute.getStringValue();
			}
		}
		return fieldName;
	}

	private String elementValue(Node node) {
		List<Node> attributes = node.selectNodes("attribute::*");
		String elementValue = null;
		for (Node attribute : attributes) {
			if (attribute.getName().equals(VALUE.getName())) {
				elementValue = attribute.getStringValue();
			}
			if (attribute.getName().equals(REF.getName())) {
				elementValue = SYMBOL_REF.getName() + attribute.getStringValue();
			}
		}
		if (elementValue == null && node.getStringValue() != null) {
			elementValue = node.getStringValue();
		}
		return elementValue;
	}

	private boolean isBean(String nodeName) {
		return nodeName.equals(BEAN.getName()) || nodeName.equals(COLLECTION.getName()) || nodeName.equals(DATASOURCE.getName());
	}

	public static List<SharkXmlNode> beanNode() {
		List<SharkXmlNode> sharkXmlNodes = Lists.newArrayList();
		sharkXmlNodes.add(BEAN);
		sharkXmlNodes.add(LOCAL_SERVER);
		sharkXmlNodes.add(REMOTE_SERVER);
		sharkXmlNodes.add(DATASOURCE);
		sharkXmlNodes.add(COLLECTION);
		return sharkXmlNodes;
	}
}
