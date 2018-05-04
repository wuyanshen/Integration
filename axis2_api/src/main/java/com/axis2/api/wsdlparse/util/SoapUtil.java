package com.axis2.api.wsdlparse.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import com.axis2.api.wsdlparse.bean.ParameterInfo;
import com.axis2.api.wsdlparse.service.component.ClientWsdlLoader;
import com.axis2.api.wsdlparse.service.component.HttpClientFactory;
import com.axis2.api.wsdlparse.service.component.LRULinkedHashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;


import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;
import com.eviware.soapui.model.iface.Operation;

public class SoapUtil {
	private static final Logger log = Logger.getLogger(SoapUtil.class);

	private DocumentBuilderFactory docBuilderFactory;
	private Map<String, WsdlInterface[]> wsdls = new LRULinkedHashMap<String, WsdlInterface[]>(256);
	private HttpClient client;

	public SoapUtil() {
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setNamespaceAware(true);
		client = HttpClientFactory.createHttpClient();
	}

	/**
	 * 发送请求
	 * 
	 * @param operation
	 * @param paramList
	 * @param wsdlUrl
	 * @return
	 * @throws Exception
	 */
	public List<ParameterInfo> sendRequest(String operation, List<ParameterInfo> paramList, String wsdlUrl) throws Exception {
		// 获取操作
		Operation operationInst = getOperation(wsdlUrl, operation, null);
		// 组装请求消息
		String message = buildRequest(wsdlUrl, operationInst, paramList);
		// 发送请求，得到返回的soap消息
		String address = wsdlUrl.substring(0, wsdlUrl.indexOf("?wsdl"));
		String responseStr = sendRequest(address, message, operationInst.getAction());
		Document soapDocument = getResponseDocument(responseStr);
		// 判断返回的soap消息是否为soap:Fault
		if (isFaultResponseSoap(soapDocument)) {
			processFaultResponseSoap(soapDocument);
		}
		// 解析返回结果
		List<ParameterInfo> outPutParamList = new ArrayList<ParameterInfo>();
		List<Map<String, Object>> wsdlMapSoapList = new ArrayList<Map<String, Object>>();
		String complextTypeName = operation + "Response";

		getOutPutParam(WsdlUtil.getDefinitionDocument(wsdlUrl), soapDocument, complextTypeName, complextTypeName, wsdlMapSoapList, outPutParamList,
				null);

		return outPutParamList;
	}

	/**
	 * 根据wsdl得到相应的operation
	 * 
	 * @param wsdl
	 * @param operation
	 * @param httpClientProps
	 * @return
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 */
	private Operation getOperation(String wsdl, String operation, Properties httpClientProps) throws IOException, UnsupportedOperationException {
		// 加载wsdl文件，找出operation
		WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdl, httpClientProps);
		for (WsdlInterface wsdlInterface : wsdlInterfaces) {
			Operation operationInst = wsdlInterface.getOperationByName(operation);

			if (operationInst != null) {
				return operationInst;
			}
		}
		// 重新加载wsdl文件
		wsdls.remove(wsdl);
		wsdlInterfaces = getWsdlInterfaces(wsdl, httpClientProps);
		for (WsdlInterface wsdlInterface : wsdlInterfaces) {
			Operation operationInst = wsdlInterface.getOperationByName(operation);
			if (operationInst != null) {
				return operationInst;
			}
		}

		throw new UnsupportedOperationException("Operation '" + operation + "' not supported by WSDL '" + wsdl + "'.");
	}

	/**
	 * 重新import wsdl文件，然后放入缓冲中
	 * 
	 * @param wsdl
	 * @param httpClientProps
	 * @return
	 * @throws IOException
	 */
	private WsdlInterface[] getWsdlInterfaces(String wsdl, Properties httpClientProps) throws IOException {
		try {
			WsdlInterface[] wsdlInterfaces = wsdls.get(wsdl);
			if (wsdlInterfaces == null) {
				WsdlProject wsdlProject = new WsdlProject();
				WsdlLoader wsdlLoader = createWsdlLoader(wsdl, httpClientProps);

				wsdlInterfaces = wsdlProject.importWsdl(wsdl, true, wsdlLoader);

				wsdls.put(wsdl, wsdlInterfaces);
			}
			return wsdlInterfaces;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to import WSDL '" + wsdl + "'.");
		}
	}

	/**
	 * 创建WsdlLoader
	 * 
	 * @param wsdl
	 * @param httpClientProps
	 * @return
	 * @throws ConfigurationException
	 */
	private WsdlLoader createWsdlLoader(String wsdl, Properties httpClientProps) throws ConfigurationException {
		HttpClient httpClient = new HttpClient();
		return new ClientWsdlLoader(wsdl, httpClient);
	}

	/**
	 * 发送请求
	 * 
	 * @param address
	 * @param message
	 * @param action
	 * @return
	 * @throws Exception
	 */
	private String sendRequest(String address, String message, String action) throws Exception {
		String responseBodyAsString;
		PostMethod postMethod = new PostMethod(address);
		try {
			postMethod.setRequestHeader("SOAPAction", action);
			postMethod.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(message.getBytes("UTF-8")), "text/xml")

			);
			client.executeMethod(postMethod);
			responseBodyAsString = postMethod.getResponseBodyAsString();
		} finally {
			postMethod.releaseConnection();
		}

		return responseBodyAsString;
	}

	/**
	 * 构建请求
	 * 
	 * @param wsdl
	 * @param operationInst
	 * @param paramList
	 * @return
	 * @throws Exception
	 */
	private String buildRequest(String wsdl, Operation operationInst, List<ParameterInfo> paramList) throws Exception {
		String requestTemplate = operationInst.getRequestAt(0).getRequestContent();
		log.debug(requestTemplate);

		// 根据不同的soap消息版本，调整消息头
		// soap 1.1-->xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"
		// soap 1.2-->xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"
		String soapVersion = ((WsdlInterface) operationInst.getInterface()).getSoapVersion().toString();
		if (StringUtils.isNotEmpty(soapVersion) && "SOAP 1.2".equals(soapVersion)) {
			requestTemplate = requestTemplate.replace("xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"",
					"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"");
		}
		// 将消息模板转换为xml
		Document document = convertStrToXml(requestTemplate);
		Document wsdlDocument = WsdlUtil.getDefinitionDocument(wsdl);

		// 构建soap消息，向消息模板中添加参数
		buildSOAPMessage(wsdlDocument, document, operationInst, paramList, null, new ParameterInfo());

		// 返回请求消息
		String requestSoapMsg = DOMUtil.serialize(document.getChildNodes());
		log.debug(requestSoapMsg);
		return requestSoapMsg;
	}

	/**
	 * 构建soap消息
	 * 
	 * @param wsdlDocument
	 * @param soapDocument
	 * @param operationInst
	 * @param paramList
	 * @param parentNode
	 * @throws Exception
	 */
	private void buildSOAPMessage(Document wsdlDocument, Document soapDocument, Operation operationInst, List<ParameterInfo> paramList,
			Node parentNode, ParameterInfo parentParam) throws Exception {
		// 操作名称
		String operationName = operationInst.getName();
		// 如果是操作方法的根节点，则清空其子节点
		if (parentNode == null) {
			parentNode = getOperationNodeInRequestSoapDom(soapDocument, operationName);
			parentNode.setTextContent("");
		}

		for (int i = 0; i < paramList.size(); i++) {
			// 得到参数的name、type、value
			ParameterInfo param = paramList.get(i);
			String value = param.getValue();
			String name = param.getName();
			String type = param.getType();

			// 判断是否为基本类型
			if (DOMUtil.isDefaultType(type) || (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(type))
					|| ("entry".equals(type) || "JsonEntry".equals(type))) {
				if (StringUtils.isEmpty(name)) {
					if (i > 0) {
						Element ele = soapDocument.createElement(parentNode.getNodeName());
						Text text = soapDocument.createTextNode(value);
						ele.appendChild(text);

						Node grandParentNode = parentNode.getParentNode();
						grandParentNode.appendChild(ele);
					} else {
						if ("entry".equals(type) || "JsonEntry".equals(type)) {
							Element ele = soapDocument.createElement(type);
							parentNode.appendChild(ele);

							// 组装子结点
							if (param.getChildren().size() > 0) {
								List<Node> childList = DOMUtil.getChildElementNodes(parentNode);
								Node lastChildNode = childList.get(childList.size() - 1);
								buildSOAPMessage(wsdlDocument, soapDocument, operationInst, param.getChildren(), lastChildNode, param);
							}
						} else {
							Text text = soapDocument.createTextNode(value);
							parentNode.appendChild(text);
						}
					}
				} else {
					Element ele = soapDocument.createElement(name);
					Text text = soapDocument.createTextNode(value);
					ele.appendChild(text);
					parentNode.appendChild(ele);

					// 组装子结点
					if (param.getChildren().size() > 0) {
						List<Node> childList = DOMUtil.getChildElementNodes(parentNode);
						Node lastChildNode = childList.get(childList.size() - 1);
						buildSOAPMessage(wsdlDocument, soapDocument, operationInst, param.getChildren(), lastChildNode, param);
					}
				}
			} else {// 如果不是基本类型，则直接组装该节点的子结点
				if (i > 0) {
					Element ele = soapDocument.createElement(parentNode.getNodeName());
					Node grandParentNode = parentNode.getParentNode();
					grandParentNode.appendChild(ele);
					// 组装子结点
					if (param.getChildren().size() > 0) {
						List<Node> childList = DOMUtil.getChildElementNodes(grandParentNode);
						Node lastChildNode = childList.get(childList.size() - 1);
						buildSOAPMessage(wsdlDocument, soapDocument, operationInst, param.getChildren(), lastChildNode, param);
					}
				} else {
					// 组装子结点
					if (param.getChildren().size() > 0) {
						buildSOAPMessage(wsdlDocument, soapDocument, operationInst, param.getChildren(), parentNode, param);
					}
				}
			}

		}
	}

	/**
	 * 解析返回的soap消息，然后将结果填充到outPutParamList中
	 * 
	 * @param wsdlDocument
	 * @param soapDocument
	 * @param operationResponseName
	 * @param complextTypeName
	 * @param wsdlMapSoapList
	 * @param outPutParamList
	 * @throws Exception
	 */
	public void getOutPutParam(Document wsdlDocument, Document soapDocument, String operationResponseName, String complextTypeName,
			List<Map<String, Object>> wsdlMapSoapList, List<ParameterInfo> outPutParamList, ParameterInfo parent) throws Exception {
		// 得到返回的参数
		List<Node> outPutNodeList = WsdlUtil.getSequenceElementOfComplexType(wsdlDocument, complextTypeName);

		for (int i = 0; i < outPutNodeList.size(); i++) {
			Map<String, Object> wsdlMapSoap = new HashMap<String, Object>();
			// 组装参数param
			Node outPutNode = outPutNodeList.get(i);
			String name = DOMUtil.getNodeName(outPutNode);
			String type = DOMUtil.getNodeType(outPutNode);

			ParameterInfo currentParam = new ParameterInfo();
			currentParam.setName(name);

			if (DOMUtil.isDefaultType(outPutNode)) {// 该参数为基本类型
				if (DOMUtil.isArray(outPutNode)) {// 数组
					currentParam.setType(WsdlUtil.SchemaDefaulyType.type_array.getType());
					currentParam.setChildType(type);

					// 组装映射关系
					wsdlMapSoap.put("name", name);
					wsdlMapSoap.put("index", new Integer(-1));
					wsdlMapSoapList.add(wsdlMapSoap);

					// 得到该数组在返回soap消息中的个数
					int arrayLength = getArrayLengthFromResponseSoap(soapDocument, operationResponseName, wsdlMapSoapList);

					// 从soap消息中取出值
					for (int j = 1; j <= arrayLength; j++) {
						ParameterInfo param = new ParameterInfo();
						param.setName(name);
						param.setType(type);

						wsdlMapSoap.put("index", new Integer(j));

						String value = getValueFromResponseSoap(soapDocument, wsdlMapSoapList, operationResponseName);
						param.setValue(value);

						currentParam.addChild(param);
					}
				} else {// 不是数组
					currentParam.setType(type);
					// 根据映射关系，从返回的soap消息中取值
					wsdlMapSoap.put("name", name);
					wsdlMapSoap.put("index", new Integer(-1));

					wsdlMapSoapList.add(wsdlMapSoap);
					String value = getValueFromResponseSoap(soapDocument, wsdlMapSoapList, operationResponseName);
					currentParam.setValue(value);
				}
			} else {// 该参数为复杂类型
				if (DOMUtil.isArray(outPutNode)) {// 数组
					currentParam.setType(WsdlUtil.SchemaDefaulyType.type_array.getType());
					currentParam.setChildType(type);

					// 组装映射关系
					wsdlMapSoap.put("name", name);
					wsdlMapSoap.put("index", new Integer(-1));
					wsdlMapSoapList.add(wsdlMapSoap);

					// 得到该数组在返回soap消息中的个数
					int arrayLength = getArrayLengthFromResponseSoap(soapDocument, operationResponseName, wsdlMapSoapList);

					// 从soap消息中取出值
					for (int j = 1; j <= arrayLength; j++) {
						ParameterInfo param = new ParameterInfo();
						param.setType(type);

						wsdlMapSoap.put("index", new Integer(j));

						// 继续查找
						getOutPutParam(wsdlDocument, soapDocument, operationResponseName, type, wsdlMapSoapList, outPutParamList, param);

						currentParam.addChild(param);
					}
				} else {// 不是数组
					currentParam.setType(type);
					// 根据映射关系，从返回的soap消息中取值
					wsdlMapSoap.put("name", name);
					wsdlMapSoap.put("index", new Integer(-1));

					wsdlMapSoapList.add(wsdlMapSoap);

					// 继续查找
					getOutPutParam(wsdlDocument, soapDocument, operationResponseName, type, wsdlMapSoapList, outPutParamList, currentParam);
				}
			}
			// 增加参数
			if (parent == null) {
				outPutParamList.add(currentParam);
			} else {
				parent.addChild(currentParam);
			}
			// 在映射关系中除去当前的结点
			wsdlMapSoapList.remove(wsdlMapSoapList.size() - 1);
		}
	}

	/**
	 * 根据wsdlMapSoapList从返回的soap消息中取值
	 * 
	 * @param soapDocument
	 * @param wsdlMapSoapList
	 * @param operationResponseName
	 * @return
	 * @throws Exception
	 */
	private String getValueFromResponseSoap(Document soapDocument, List<Map<String, Object>> wsdlMapSoapList, String operationResponseName)
			throws Exception {
		// 结果
		String value = "";
		// xpath
		XPath xpath = WsdlUtil.getXpath(soapDocument);
		// 组装xpathStr
		String prefix = getResponseSoapPrefix(soapDocument, operationResponseName);
		StringBuilder xpathStr = new StringBuilder("soap:Envelope/soap:Body/").append(prefix).append(":").append(operationResponseName);
		for (Map<String, Object> map : wsdlMapSoapList) {
			String name = map.get("name").toString();
			Integer index = (Integer) map.get("index");
			if (index == -1) {// 不是数组
				xpathStr.append("/").append(name);
			} else {// 是数组
				xpathStr.append("/").append(name).append("[position()=").append(index).append("]");
			}
		}
		// 获取值
		Node node = (Node) xpath.evaluate(xpathStr.toString(), soapDocument, XPathConstants.NODE);
		if (node != null) {
			value = node.getTextContent();
		}
		return value;
	}

	/**************************************************************************************************
	 * 判断返回的是否为错误的soap消息
	 * 
	 * @param soapDocument
	 * @return
	 * @throws Exception
	 */
	private boolean isFaultResponseSoap(Document soapDocument) throws Exception {
		try {
			Node node = DOMUtil.findNode(soapDocument, "soap:Envelope/soap:Body/soap:Fault");
			return node == null ? false : true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 处理错误的soap消息
	 * 
	 * @param soapDocument
	 * @throws Exception
	 */
	private void processFaultResponseSoap(Document soapDocument) throws Exception {
		String faultcode = DOMUtil.findNode(soapDocument, "soap:Envelope/soap:Body/soap:Fault/faultcode").getTextContent();
		String faultstring = DOMUtil.findNode(soapDocument, "soap:Envelope/soap:Body/soap:Fault/faultstring").getTextContent();
		throw new RuntimeException("<br/>faultcode-->" + faultcode + "<br/>faultstring-->" + faultstring);
	}

	/***************************************************************************************
	 * 得到DocumentBuilder
	 * 
	 * @return
	 * @throws IOException
	 */
	private synchronized DocumentBuilder getDocBuilder() throws IOException {
		try {
			return docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("创建DocumentBuilder失败", e);
		}
	}

	/**
	 * 获取返回的soap消息的Document
	 * 
	 * @param response
	 * @return
	 */
	public Document getResponseDocument(String response) {
		try {
			Document doc = getDocBuilder().parse(new InputSource(new StringReader(response)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取返回的soap消息的Document异常", e);
		}
	}

	/**
	 * 在发送的soap消息中查找operation所在的结点
	 * 
	 * @param soapDocument
	 * @param operationName
	 * @return
	 * @throws Exception
	 */
	private Node getOperationNodeInRequestSoapDom(Document soapDocument, String operationName) throws Exception {
		String prefix = getResponseSoapPrefix(soapDocument);
		StringBuilder operationNodeStr = new StringBuilder("soapenv:Envelope/soapenv:Body/").append(prefix).append(":").append(operationName);
		return DOMUtil.findNode(soapDocument, operationNodeStr.toString());
	}

	/**
	 * 从返回的soap消息中得到数组类型的长度
	 * 
	 * @param soapDocument
	 * @param operationName
	 * @param wsdlMapSoapList
	 * @return
	 * @throws Exception
	 */
	private int getArrayLengthFromResponseSoap(Document soapDocument, String operationName, List<Map<String, Object>> wsdlMapSoapList)
			throws Exception {
		// 得到该数组在返回soap消息中的个数
		String prefix = getResponseSoapPrefix(soapDocument, operationName);
		StringBuilder arrayLengthStr = new StringBuilder("soap:Envelope/soap:Body/").append(prefix).append(":").append(operationName);
		for (Map<String, Object> map : wsdlMapSoapList) {
			String name = map.get("name").toString();
			Integer index = (Integer) map.get("index");
			if (index == -1) {// 不是数组
				arrayLengthStr.append("/").append(name);
			} else {// 是数组
				arrayLengthStr.append("/").append(name).append("[position()=").append(index).append("]");
			}
		}
		int arrayLength = DOMUtil.findNodeList(soapDocument, arrayLengthStr.toString()).getLength();
		return arrayLength;
	}

	/**
	 * 将string转换为xml的dom
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	private Document convertStrToXml(String xmlStr) throws Exception {
		try {
			return getDocBuilder().parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e) {
			throw new RuntimeException("将soapRequest请求模板转换为Dom报错", e);
		}
	}

	/***********************************************************************************************
	 * 得到返回的soap消息的前缀
	 * 
	 * @param soapDocument
	 * @return
	 * @throws Exception
	 */
	private String getResponseSoapPrefix(Document soapDocument) throws Exception {
		NamedNodeMap map = soapDocument.getDocumentElement().getAttributes();
		if (map != null && map.getLength() > 0) {
			for (int i = 0; i < map.getLength(); i++) {
				Node node = map.item(i);
				String name = node.getLocalName();
				if (!"soapenv".equals(name)) {
					return name;
				}
			}
		}
		return "";
	}

	/**
	 * 得到返回的soap消息的前缀
	 * 
	 * @param soapDocument
	 * @param responseName
	 * @return
	 * @throws Exception
	 */
	private String getResponseSoapPrefix(Document soapDocument, String responseName) throws Exception {
		Node node = DOMUtil.findNode(soapDocument, "soap:Envelope/soap:Body");
		if (node != null) {
			NamedNodeMap map = DOMUtil.getFirstChildElementNode(node).getAttributes();
			if (map != null && map.getLength() > 0) {
				return map.item(0).getLocalName();
			}
		}
		return "";
	}
}
