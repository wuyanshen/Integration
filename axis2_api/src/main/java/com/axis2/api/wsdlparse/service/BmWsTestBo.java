package com.axis2.api.wsdlparse.service;

import java.util.List;

import com.axis2.api.wsdlparse.bean.ParameterInfo;
import com.axis2.api.wsdlparse.bean.WebServiceMethod;

/**
 * @author zhengtian
 * 
 * @date 2011-8-4 下午10:53:34
 */
@SuppressWarnings("all")
public interface BmWsTestBo {
	/**
	 * 根据URL得到所有的方法
	 * 
	 * @param webserviceUrl
	 * @return
	 * @throws Exception
	 */
	public List<WebServiceMethod> getAllMethodByServiceUrl(String webserviceUrl) throws Exception;

	/**
	 * 根据方法名称和webserviceUrl得到参数
	 * 
	 * @param methodName
	 * @param webserviceUrl
	 * @return
	 * @throws Exception
	 */
	public List<ParameterInfo> getParamByMethodNameAndWsUrl(String methodName, String webserviceUrl) throws Exception;

	/**
	 * 执行方法
	 * 
	 * @param webserviceUrl
	 * @param methodName
	 * @param paramStr
	 * @return
	 * @throws Exception
	 */
	public String executionMethod(String webserviceUrl, String methodName, String paramStr) throws Exception;
}
